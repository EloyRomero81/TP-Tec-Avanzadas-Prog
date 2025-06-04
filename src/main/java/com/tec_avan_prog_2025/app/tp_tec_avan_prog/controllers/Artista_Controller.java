package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.ArtistaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Artista_Service;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/artistas")
public class Artista_Controller {
    @Autowired
    private Artista_Service artista_Service;

    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> listarArtistas(){
        List<ArtistaDTO> artistas = artista_Service.listarArtistas();
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> buscarPorId(@PathVariable Integer id){
        Optional<ArtistaDTO> artista = artista_Service.buscarDTOPorId(id);
        return artista.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<ArtistaDTO> insertar(@RequestBody ArtistaDTO artista){
        ArtistaDTO insertado = artista_Service.guardar(artista);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaDTO> actualizar(@PathVariable Integer id, @RequestBody ArtistaDTO artista){
        try {
            ArtistaDTO actualizado = artista_Service.actualizar(id, artista); 
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        try{
            artista_Service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
