package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CuentaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Cuenta_Service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/cuentas")
public class Cuenta_Controller {
    @Autowired
    private Cuenta_Service cuenta_Service;

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listarCuentas(){
        List<CuentaDTO> cuentas = cuenta_Service.listarCuentas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> buscarPorId(@PathVariable Integer id){
        Optional<CuentaDTO> cuenta = cuenta_Service.buscarDTOPorId(id);
        return cuenta.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<CuentaDTO> insertar(@RequestBody CuentaDTO cuenta){
        CuentaDTO insertado = cuenta_Service.guardar(cuenta);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> actualizar(@PathVariable Integer id, @RequestBody CuentaDTO cuenta){
        try {
            CuentaDTO actualizado = cuenta_Service.actualizar(id, cuenta); 
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        try{
            cuenta_Service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
