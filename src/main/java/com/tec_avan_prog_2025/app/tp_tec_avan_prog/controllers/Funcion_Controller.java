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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Funcion_Service;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/funciones")
public class Funcion_Controller {
    @Autowired
    private Funcion_Service funcion_Service;

    @GetMapping
    public ResponseEntity<List<FuncionDTO>> listarFunciones(){
        List<FuncionDTO> funciones = funcion_Service.listarFunciones();
        return new ResponseEntity<>(funciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionDTO> buscarPorId(@PathVariable Integer id){
        Optional<FuncionDTO> funcion = funcion_Service.buscarDTOPorId(id);
        return funcion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<FuncionDTO> insertar(@RequestBody FuncionDTO funcion){
        FuncionDTO insertado = funcion_Service.guardar(funcion);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionDTO> actualizar(@PathVariable Integer id, @RequestBody FuncionDTO funcion){
        try {
            FuncionDTO actualizado = funcion_Service.actualizar(id, funcion); 
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        try{
            funcion_Service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
