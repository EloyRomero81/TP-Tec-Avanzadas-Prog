package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import java.util.List;

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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.EntradaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Entrada_Service;

@RestController
@RequestMapping("/entradas")
public class Entrada_Controller {
    @Autowired
    private Entrada_Service entrada_Service;

    @GetMapping
    public ResponseEntity<List<EntradaDTO>> listarEntradas(){
        List<EntradaDTO> entradas = entrada_Service.listarEntradas();
        return new ResponseEntity<>(entradas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaDTO> buscarPorId(@PathVariable Integer id){
        EntradaDTO entrada = entrada_Service.buscarDTOPorId(id);
        return new ResponseEntity<>(entrada, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EntradaDTO> insertar(@RequestBody EntradaDTO entrada){
        EntradaDTO insertado = entrada_Service.guardar(entrada);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntradaDTO> actualizar(@PathVariable Integer id, @RequestBody EntradaDTO entrada){
        EntradaDTO actualizado = entrada_Service.actualizar(id, entrada);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        entrada_Service.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
