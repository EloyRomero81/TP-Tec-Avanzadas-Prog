package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.FuncionMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Funcion;

import jakarta.persistence.EntityNotFoundException;

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
    private Entrada_Service entrada_Service;
    @Autowired
    @Lazy
    private Artista_Service artista_Service;

    private FuncionDTO seteoDeFuncionDTO(Funcion funcion){
        FuncionDTO funcionDTO = funcionMapper.funcionToFuncionDTO(funcion);
        funcionDTO.setEntradasVendidas(funcion.getEntradas().size());
        funcionDTO.setEntradasDisponibles(funcion.getSala().getCapacidad() - funcion.getEntradas().size());
        return funcionDTO;
    }

    public List<FuncionDTO> listarFunciones(){
        return repo_Funcion.findAll().stream().map(funcion -> seteoDeFuncionDTO(funcion))
        .collect(Collectors.toList());
    }

    public List<FuncionDTO> listarFuncionesProximas() {
        return repo_Funcion.findAll().stream()
            .filter(f -> !f.getFecha().isBefore(LocalDate.now())) 
            .map(funcion -> seteoDeFuncionDTO(funcion))
            .collect(Collectors.toList());
    }

    public List<FuncionDTO> listarFuncionesAnteriores() {
        return repo_Funcion.findAll().stream()
            .filter(f -> f.getFecha().isBefore(LocalDate.now())) 
            .map(funcion -> seteoDeFuncionDTO(funcion))
            .collect(Collectors.toList());
    }

    public Optional<FuncionDTO> buscarDTOPorId(Integer id){
        return repo_Funcion.findById(id).map(funcion -> seteoDeFuncionDTO(funcion));
    }

    public Funcion buscarPorId(Integer id){
        return repo_Funcion.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Funcion con id: "+id+" no encontrado"));
    }

    public FuncionDTO guardar(FuncionDTO funcionDTO){
        Funcion funcion = funcionMapper.funcionDTOToFuncion(funcionDTO);
        funcion.setSala(sala_Service.buscarPorId(funcionDTO.getNroSala()));
        funcion.setArtista(artista_Service.buscarPorNombre(funcionDTO.getNombreArtista()));
        validarSuperposicionFuncion(funcion);
        return funcionMapper.funcionToFuncionDTO(repo_Funcion.save(funcion));
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
        return funcionMapper.funcionToFuncionDTO(repo_Funcion.save(funcionExistente));
    }

    public void eliminarPorId(Integer id){
        buscarPorId(id);
        entrada_Service.desvincularEntradasDeFuncion(id);
        repo_Funcion.deleteById(id);
    }

    private void validarSuperposicionFuncion(Funcion nuevaFuncion) {
        List<Funcion> funcionesEnSala = repo_Funcion.findBySalaAndFecha(nuevaFuncion.getSala(), nuevaFuncion.getFecha());

        LocalTime inicioNuevo = nuevaFuncion.getHora();
        LocalTime finNuevo = inicioNuevo.plusMinutes(nuevaFuncion.getDuracion()).plusHours(1); // + 1 hora limpieza

        for (Funcion f : funcionesEnSala) {
            if(f.getIdFuncion() == nuevaFuncion.getIdFuncion()) continue; // Ignorar misma función en update

            LocalTime inicioExistente = f.getHora();
            LocalTime finExistente = inicioExistente.plusMinutes(f.getDuracion()).plusHours(1);

            boolean seSolapan = inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente);
            if(seSolapan) {
                throw new IllegalArgumentException("La función se superpone con otra función en la sala");
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
