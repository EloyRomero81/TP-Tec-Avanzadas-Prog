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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.crearFuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Funcion_Service;

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

    @GetMapping("/proximas")
    public ResponseEntity<List<FuncionDTO>> listarFuncionesProximas(){
        List<FuncionDTO> funciones = funcion_Service.listarFuncionesProximas();
        return new ResponseEntity<>(funciones, HttpStatus.OK);
    }

    @GetMapping("/anteriores")
    public ResponseEntity<List<FuncionDTO>> listarFuncionesAnteriores(){
        List<FuncionDTO> funciones = funcion_Service.listarFuncionesAnteriores();
        return new ResponseEntity<>(funciones, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FuncionDTO> buscarPorId(@PathVariable Integer id){
        FuncionDTO funcion = funcion_Service.buscarDTOPorId(id);
        return new ResponseEntity<>(funcion, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FuncionDTO> insertar(@RequestBody crearFuncionDTO crearFuncionDTO){
        FuncionDTO insertado = funcion_Service.guardar(crearFuncionDTO);
        return new ResponseEntity<>(insertado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionDTO> actualizar(@PathVariable Integer id, @RequestBody FuncionDTO funcion){
        FuncionDTO actualizado = funcion_Service.actualizar(id, funcion); 
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id){
        funcion_Service.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
