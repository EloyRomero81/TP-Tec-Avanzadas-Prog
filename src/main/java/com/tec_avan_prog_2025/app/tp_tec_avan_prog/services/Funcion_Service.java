package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CrearFuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.AccesoDenegadoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.SuperposicionFuncionException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.FuncionMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Funcion;

@Service
public class Funcion_Service {
    @Autowired
    private Repo_Funcion repo_Funcion;
    @Autowired
    private FuncionMapper funcionMapper;
    @Autowired
    @Lazy
    private Sala_Service sala_Service;
    @Autowired
    @Lazy
    private Artista_Service artista_Service;
    @Autowired
    @Lazy
    private Cuenta_Service cuenta_Service;

    private FuncionDTO mapeoDeFuncionDTO(Funcion funcion){
        FuncionDTO funcionDTO = funcionMapper.funcionToFuncionDTO(funcion);
        funcionDTO.setEntradasVendidas(funcion.getEntradas().size());
        funcionDTO.setEntradasDisponibles(funcion.getSala().getCapacidad() - funcion.getEntradas().size());
        return funcionDTO;
    }

    public List<FuncionDTO> listarFunciones(){
        return repo_Funcion.findAll().stream().map(funcion -> mapeoDeFuncionDTO(funcion))
        .collect(Collectors.toList());
    }

    public List<FuncionDTO> listarFuncionesProximas() {
        return repo_Funcion.findAll().stream()
            .filter(f -> !f.getFecha().isBefore(LocalDate.now())) 
            .map(funcion -> mapeoDeFuncionDTO(funcion))
            .collect(Collectors.toList());
    }

    public List<FuncionDTO> listarFuncionesAnteriores() {
        return repo_Funcion.findAll().stream()
            .filter(f -> f.getFecha().isBefore(LocalDate.now())) 
            .map(funcion -> mapeoDeFuncionDTO(funcion))
            .collect(Collectors.toList());
    }

    public FuncionDTO buscarDTOPorId(Integer id){
        Funcion funcion = repo_Funcion.findById(id)
            .orElseThrow(() -> new FuncionNoEncontradaException("Funcion con id: "+id+" no encontrado"));
        return mapeoDeFuncionDTO(funcion);
    }

    public Funcion buscarPorId(Integer id){
        return repo_Funcion.findById(id)
            .orElseThrow(() -> new FuncionNoEncontradaException("Funcion con id: "+id+" no encontrado"));
    }

    public FuncionDTO guardar(CrearFuncionDTO crearFuncionDTO){
        Cuenta cuenta = cuenta_Service.buscarPorId(crearFuncionDTO.getIdCuenta());
        if (cuenta.getTipoCuenta()!=TipoCuenta.ADMINISTRADOR){
            throw new AccesoDenegadoException("Solo los administradores pueden crear funciones");
        } 
        Funcion funcion = funcionMapper.crearFuncionDTOToFuncion(crearFuncionDTO);
        funcion.setSala(sala_Service.buscarPorId(crearFuncionDTO.getNroSala()));
        funcion.setArtista(artista_Service.buscarPorNombre(crearFuncionDTO.getNombreArtista()));
        validarSuperposicionFuncion(funcion);
        return mapeoDeFuncionDTO(repo_Funcion.save(funcion));
    }

    public FuncionDTO actualizar(Integer id, FuncionDTO funcionDTO){
        Funcion funcionExistente = buscarPorId(id);
        funcionExistente.setSala(sala_Service.buscarPorId(funcionDTO.getNroSala()));
        funcionExistente.setArtista(artista_Service.buscarPorNombre(funcionDTO.getNombreArtista()));
        funcionExistente.setFecha(funcionDTO.getFecha());
        funcionExistente.setHora(funcionDTO.getHora());
        funcionExistente.setDuracion(funcionDTO.getDuracion());
        funcionExistente.setTipoFuncion(funcionDTO.getTipoFuncion());
        funcionExistente.setPrecioBaseEntrada(funcionDTO.getPrecioBaseEntrada());
        validarSuperposicionFuncion(funcionExistente);
        return mapeoDeFuncionDTO(repo_Funcion.save(funcionExistente));
    }

    public void eliminarPorId(Integer id){
        buscarPorId(id);
        repo_Funcion.deleteById(id);
    }

    private void validarSuperposicionFuncion(Funcion nuevaFuncion) {
        List<Funcion> funcionesEnSala = repo_Funcion.findBySalaAndFecha(nuevaFuncion.getSala(), nuevaFuncion.getFecha());

        LocalDateTime inicioNuevo = LocalDateTime.of(nuevaFuncion.getFecha(), nuevaFuncion.getHora());
        LocalDateTime finNuevo = inicioNuevo.plusMinutes(nuevaFuncion.getDuracion()).plusHours(1);
        for (Funcion f : funcionesEnSala) {
            if(f.getIdFuncion() == nuevaFuncion.getIdFuncion()) continue; // Ignorar misma función en update
            LocalDateTime inicioExistente = LocalDateTime.of(f.getFecha(), f.getHora());
            LocalDateTime finExistente = inicioExistente.plusMinutes(f.getDuracion()).plusHours(1);
            boolean seSolapan = inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente);
            if(seSolapan) {
                throw new SuperposicionFuncionException("La función se superpone con otra función en la sala");
            }
        }
    }

    public void desvincularFuncionesDeSala(Integer idSala) {
        List<Funcion> funcionesAsociadas = repo_Funcion.findAll().stream()
            .filter(f -> f.getSala() != null && f.getSala().getNroSala().equals(idSala))
            .collect(Collectors.toList());
        for (Funcion f : funcionesAsociadas) {
            f.setSala(null);
        }
        repo_Funcion.saveAll(funcionesAsociadas);
    }

    public void desvincularFuncionesDeArtista(Integer idArtista){
        List<Funcion> funciones = repo_Funcion.findAll().stream()
            .filter(f -> f.getArtista() != null && f.getArtista().getIdArtista() == idArtista)
            .collect(Collectors.toList());
        for (Funcion f : funciones) {
            f.setArtista(null);
        }
        repo_Funcion.saveAll(funciones);
    }
}
