package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CuentaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CuentaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.CuentaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Cuenta;

@Service
public class Cuenta_Service {
    @Autowired
    private Repo_Cuenta repo_Cuenta;
    @Autowired
    private CuentaMapper cuentaMapper;

    public List<CuentaDTO> listarCuentas(){
        return repo_Cuenta.findAll().stream()
        .map(cuentaMapper::cuentaToCuentaDTO)
        .collect(Collectors.toList());
    }

    public CuentaDTO buscarDTOPorId(Integer id){
        Cuenta cuenta = repo_Cuenta.findById(id)
            .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta con id: "+id+" no encontrado"));
        return cuentaMapper.cuentaToCuentaDTO(cuenta);  
    }

    public Cuenta buscarPorId(Integer id){
        return repo_Cuenta.findById(id)
        .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta con id: "+id+" no encontrado"));
    }

    public CuentaDTO guardar(CuentaDTO cuentaDTO){
        Cuenta cuenta = cuentaMapper.cuentaDTOToCuenta(cuentaDTO);
        return cuentaMapper.cuentaToCuentaDTO(repo_Cuenta.save(cuenta));
    }

    public CuentaDTO actualizar(Integer id, CuentaDTO cuentaDTO){
        Cuenta cuentaExistente = buscarPorId(id);
        cuentaExistente.setNombre(cuentaDTO.getNombre());
        cuentaExistente.setEmail(cuentaDTO.getEmail());
        return cuentaMapper.cuentaToCuentaDTO(repo_Cuenta.save(cuentaExistente));
    }

    public void eliminarPorId(Integer id){
        buscarPorId(id);
        repo_Cuenta.deleteById(id);
    }
}
