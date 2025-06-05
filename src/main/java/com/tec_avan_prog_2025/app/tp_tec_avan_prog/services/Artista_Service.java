package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.ArtistaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.ArtistaNoEncontradoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.ArtistaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Artista;

@Service
public class Artista_Service {
    @Autowired
    private Repo_Artista repo_Artista;
    @Autowired
    private Funcion_Service funcion_Service;
    @Autowired
    private ArtistaMapper artistaMapper;

    public List<ArtistaDTO> listarArtistas(){
        return repo_Artista.findAll().stream()
        .map(artistaMapper::artistaToArtistaDTO)
        .collect(Collectors.toList());
    }

    public ArtistaDTO buscarDTOPorId(Integer id){
        Artista artista = repo_Artista.findById(id)
            .orElseThrow(() -> new ArtistaNoEncontradoException("Artista con id: " + id + " no encontrado"));
        return artistaMapper.artistaToArtistaDTO(artista);
    }
    
    public Artista buscarPorId(Integer id){
        return repo_Artista.findById(id)
            .orElseThrow(() -> new ArtistaNoEncontradoException("Artista con id: "+id+" no encontrado"));
    }

    public Artista buscarPorNombre(String nombre){
        return repo_Artista.findByNombre(nombre)
            .orElseThrow(() -> new ArtistaNoEncontradoException("Artista con nombre: "+nombre+" no encontrado"));
    }

    public ArtistaDTO guardar(ArtistaDTO artistaDTO){
        Artista artista = artistaMapper.artistaDTOToArtista(artistaDTO);
        return artistaMapper.artistaToArtistaDTO(repo_Artista.save(artista));
    }

    public ArtistaDTO actualizar(Integer id, ArtistaDTO artistaDTO){
        Artista artistaExistente = buscarPorId(id);
        artistaExistente.setNombre(artistaDTO.getNombre());
        return artistaMapper.artistaToArtistaDTO(repo_Artista.save(artistaExistente));
    }

    public void eliminarPorId(Integer id){
        buscarPorId(id);
        funcion_Service.desvincularFuncionesDeArtista(id);
        repo_Artista.deleteById(id);
    }
}
