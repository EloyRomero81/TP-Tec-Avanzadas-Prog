package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.SalaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.SalaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Sala;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Sala_Service {
    @Autowired
    private Repo_Sala repo_Sala;
    @Autowired
    private SalaMapper salaMapper;
    @Autowired
    @Lazy
    private Funcion_Service funcion_Service;


    public List<SalaDTO> listarSalas(){
        return repo_Sala.findAll().stream()
        .map(salaMapper::salaToSalaDTO)
        .collect(Collectors.toList());
    }

    public Optional<SalaDTO> buscarDTOPorId(Integer id){
        return repo_Sala.findById(id)
        .map(salaMapper::salaToSalaDTO); 
    }

    public Sala buscarPorId(Integer id) {
        return repo_Sala.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sala con id: " + id + " no encontrada"));
    }

    public SalaDTO guardar(SalaDTO salaDTO){
        Sala sala = salaMapper.salaDTOToSala(salaDTO);
        return salaMapper.salaToSalaDTO(repo_Sala.save(sala));
    }

    public SalaDTO actualizar(Integer id, SalaDTO salaDTO){
        Sala salaExistente = buscarPorId(id);
        salaExistente.setTipoSala(salaDTO.getTipoSala());
        salaExistente.setCapacidad(salaDTO.getCapacidad());
        return salaMapper.salaToSalaDTO(repo_Sala.save(salaExistente));
    }

    public void eliminarPorId(Integer id){
        buscarPorId(id);
        funcion_Service.desvincularFuncionesDeSala(id);
        repo_Sala.deleteById(id);
        
    }
}
