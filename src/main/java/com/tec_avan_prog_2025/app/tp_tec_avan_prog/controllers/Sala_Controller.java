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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.SalaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Sala_Service;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/salas")
public class Sala_Controller {
    @Autowired
    private Sala_Service sala_Service;

    @GetMapping
    public ResponseEntity<List<SalaDTO>> listarSalas(){
        List<SalaDTO> salas = sala_Service.listarSalas();
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalaDTO> buscarPorId(@PathVariable Integer id){
        Optional<SalaDTO> sala = sala_Service.buscarDTOPorId(id);
        return sala.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<SalaDTO> insertar(@RequestBody SalaDTO sala){
        SalaDTO insertado = sala_Service.guardar(sala);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaDTO> actualizar(@PathVariable Integer id, @RequestBody SalaDTO sala){
        try {
            SalaDTO actualizado = sala_Service.actualizar(id, sala); 
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        try{
            sala_Service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
