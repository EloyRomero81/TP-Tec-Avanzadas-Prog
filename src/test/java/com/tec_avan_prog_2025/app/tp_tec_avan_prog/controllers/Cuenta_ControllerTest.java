package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CuentaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CuentaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Cuenta_Service;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 


@WebMvcTest(Cuenta_Controller.class)
@AutoConfigureMockMvc
public class Cuenta_ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private Cuenta_Service cuenta_Service;

    public CuentaDTO cuentaDTOPreparada;
    
    @BeforeEach
    void setUp(){
        cuentaDTOPreparada = CuentaDTO.builder()
            .idCuenta(1)
            .tipoCuenta(TipoCuenta.ADMINISTRADOR)
            .nombre("Admin")
            .email("admin@gmail.com")
            .build();
    }
    @Test
    void testInsertar() throws Exception {
        CuentaDTO cuentaDTOPost = CuentaDTO.builder()
            .nombre(cuentaDTOPreparada.getNombre())
            .email(cuentaDTOPreparada.getEmail())
            .build();
        Mockito.when(cuenta_Service.guardar(cuentaDTOPost)).thenReturn(cuentaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.post("/cuentas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cuentaDTOPost)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Admin"));
    }

    @Test
    void testBuscarPorId() throws Exception {
        Mockito.when(cuenta_Service.buscarDTOPorId(1)).thenReturn(cuentaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idCuenta").value(1))
            .andExpect(jsonPath("$.nombre").value("Admin"));
    }

    @Test
    void testBuscarPorId_NoEncontrado() throws Exception {
        Mockito.when(cuenta_Service.buscarDTOPorId(99)).thenThrow(new CuentaNoEncontradaException("Cuenta con id: 99 no encontrada"));
        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Cuenta con id: 99 no encontrada"));
    }    

    @Test
    void testListarCuentas() throws Exception {
        Mockito.when(cuenta_Service.listarCuentas()).thenReturn(List.of(cuentaDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Admin"));
    }
    
    @Test
    void testActualizar() throws Exception {
        CuentaDTO cuentaDTOPut = CuentaDTO.builder()
            .nombre("CuentaActualizada")
            .email("actualizada@cuenta.com")
            .build();
        Mockito.when(cuenta_Service.actualizar(1, cuentaDTOPut)).thenReturn(cuentaDTOPut);
        mockMvc.perform(MockMvcRequestBuilders.put("/cuentas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cuentaDTOPut)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("CuentaActualizada"));
    }

    @Test
    void testBorrar() throws Exception {
        Mockito.doNothing().when(cuenta_Service).eliminarPorId(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/cuentas/1"))
            .andExpect(status().isNoContent());
    }
}
