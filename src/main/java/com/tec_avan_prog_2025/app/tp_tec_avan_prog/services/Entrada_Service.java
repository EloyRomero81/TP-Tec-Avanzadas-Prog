package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.EntradaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CapacidadExcedidaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.EntradaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionPasadaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.TipoEntradaInvalidoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.EntradaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Entrada;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Entrada_Service {
    @Autowired
    private Repo_Entrada repo_Entrada;
    @Autowired
    private EntradaMapper entradaMapper;
    @Autowired
    @Lazy
    private Cuenta_Service cuenta_Service;
    @Autowired
    @Lazy
    private Funcion_Service funcion_Service;

    public List<EntradaDTO> listarEntradas(){
        return repo_Entrada.findAll().stream()
        .map(entradaMapper::entradaToEntradaDTO)
        .collect(Collectors.toList());
    }

    public EntradaDTO buscarDTOPorId(Integer id){
        Entrada entrada = repo_Entrada.findById(id)
            .orElseThrow(() -> new EntradaNoEncontradaException("Entrada con id: "+id+" no encontrada")); 
        return entradaMapper.entradaToEntradaDTO(entrada);
    }

    public Entrada buscarPorId(Integer id){
        return repo_Entrada.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entrada con id: "+id+" no encontrada"));
    }

    public EntradaDTO guardar(EntradaDTO entradaDTO){
        Entrada entrada = entradaMapper.entradaDTOToEntrada(entradaDTO);
        Cuenta cuenta = cuenta_Service.buscarPorId(entradaDTO.getIdCuenta());
        Funcion funcion = funcion_Service.buscarPorId(entradaDTO.getIdFuncion());

        validarFecha(funcion.getFecha());  // No permitir comprar entradas para funciones anteriores
        validarCapacidad(funcion); // No sobrepasar capacidad

        entrada.setCuenta(cuenta);
        entrada.setFuncion(funcion);
        entrada.setPrecio(calcularPrecioEntrada(funcion, entradaDTO.getTipoEntrada()));
        
        return entradaMapper.entradaToEntradaDTO(repo_Entrada.save(entrada));
    }
    
    public EntradaDTO actualizar(Integer id, EntradaDTO entradaDTO){
        Entrada entradaExistente = buscarPorId(id);
        entradaExistente.setCuenta(cuenta_Service.buscarPorId(entradaDTO.getIdCuenta()));
        entradaExistente.setFuncion(funcion_Service.buscarPorId(entradaDTO.getIdFuncion()));
        entradaExistente.setTipoEntrada(entradaDTO.getTipoEntrada());
        entradaExistente.setPrecio(entradaDTO.getPrecio());
        return entradaMapper.entradaToEntradaDTO(repo_Entrada.save(entradaExistente));
    }

    public void eliminarPorId(Integer id){
        repo_Entrada.findById(id)
        .orElseThrow(() -> new EntradaNoEncontradaException("Entrada con id: "+id+" no encontrada"));
        repo_Entrada.deleteById(id);
    }

    private Double calcularPrecioEntrada(Funcion funcion, String tipoEntrada) {
        String tipoSala = funcion.getSala().getTipoSala();
        Double base = funcion.getPrecioBaseEntrada();

        if (tipoSala.equalsIgnoreCase("Sala Principal")) {
            if (tipoEntrada.equalsIgnoreCase("A")) return base * 2;
            if (tipoEntrada.equalsIgnoreCase("B")) return base;
            throw new TipoEntradaInvalidoException("Tipo de entrada inválido para Sala Principal. Use A o B.");
        }
        if (tipoSala.equalsIgnoreCase("Anfiteatro")) {
            if (tipoEntrada.equalsIgnoreCase("Unica")) return base;
            throw new TipoEntradaInvalidoException("Tipo de entrada inválido para Anfiteatro. Solo Unica disponible.");
        }
        throw new TipoEntradaInvalidoException("Tipo de sala desconocido");
    }

    private void validarFecha(LocalDate fecha){
        if (fecha.isBefore(LocalDate.now())) {
            throw new FuncionPasadaException("No se pueden comprar entradas para funciones ya realizadas");
        }
    }

    private void validarCapacidad(Funcion funcion) { 
        long entradasVendidas = repo_Entrada.countByFuncion_IdFuncion(funcion.getIdFuncion());
        int capacidad = funcion.getSala().getCapacidad();
        if (entradasVendidas >= capacidad) {
            throw new CapacidadExcedidaException("No hay más capacidad disponible para esta función");
        }
    }
}
