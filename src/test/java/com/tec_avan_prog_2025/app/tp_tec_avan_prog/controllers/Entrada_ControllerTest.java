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
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.EntradaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CapacidadExcedidaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.EntradaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionPasadaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.TipoEntradaInvalidoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Entrada_Service;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 
import java.util.List;


@WebMvcTest(Entrada_Controller.class)
@AutoConfigureMockMvc
public class Entrada_ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private Entrada_Service entrada_Service;

    private EntradaDTO entradaDTOPreparada;

    @BeforeEach
    void setUp() {
        entradaDTOPreparada = EntradaDTO.builder()
            .idEntrada(1)
            .tipoEntrada("unica")
            .precio(1000.0)
            .build();
    }

    @Test
    void testInsertarEntrada() throws Exception {
        EntradaDTO entradaDTOPost = EntradaDTO.builder()
            .idCuenta(entradaDTOPreparada.getIdCuenta())
            .idFuncion(entradaDTOPreparada.getIdFuncion())
            .tipoEntrada(entradaDTOPreparada.getTipoEntrada())
            .precio(entradaDTOPreparada.getPrecio())
            .build();
        Mockito.when(entrada_Service.guardar(entradaDTOPost)).thenReturn(entradaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOPost)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idEntrada").value(1))
            .andExpect(jsonPath("$.precio").value(1000.0));
    }

    @Test
    void testInsertarEntradaException_SalaPrincipal() throws Exception {
        EntradaDTO entradaDTOInvalida = EntradaDTO.builder()
            .idEntrada(2)
            .tipoEntrada("z")
            .build();
        Mockito.when(entrada_Service.guardar(entradaDTOInvalida)).thenThrow(new TipoEntradaInvalidoException("Tipo de entrada inválido para Sala Principal. Use A o B."));
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOInvalida)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Tipo de entrada inválido para Sala Principal. Use A o B."));
    }

    @Test
    void testInsertarEntradaException_Anfiteatro() throws Exception {
        EntradaDTO entradaDTOInvalida = EntradaDTO.builder()
            .idEntrada(2)
            .tipoEntrada("A")
            .build();
        Mockito.when(entrada_Service.guardar(entradaDTOInvalida)).thenThrow(new TipoEntradaInvalidoException("Tipo de entrada inválido para Anfiteatro. Solo Unica disponible."));
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOInvalida)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Tipo de entrada inválido para Anfiteatro. Solo Unica disponible."));
    }

    @Test
    void testInsertarEntradaException_SalaDesconocida() throws Exception {
        EntradaDTO entradaDTOInvalida = EntradaDTO.builder()
            .idEntrada(2)
            .tipoEntrada("A")
            .build();
        Mockito.when(entrada_Service.guardar(entradaDTOInvalida)).thenThrow(new TipoEntradaInvalidoException("Tipo de sala desconocido"));
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOInvalida)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Tipo de sala desconocido"));
    }

    @Test
    void testInsertarEntrada_FuncionPasada() throws Exception {
        EntradaDTO entradaDTOInvalida = EntradaDTO.builder()
            .idEntrada(2)
            .idFuncion(2) //Funcion con fecha pasada
            .build();
        Mockito.when(entrada_Service.guardar(entradaDTOInvalida)).thenThrow(new FuncionPasadaException("No se pueden comprar entradas para funciones ya realizadas"));
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOInvalida)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("No se pueden comprar entradas para funciones ya realizadas"));
    }

    @Test
    void testInsertarEntrada_CapacidadExcedida() throws Exception {
        EntradaDTO entradaDTOInvalida = EntradaDTO.builder()
            .idEntrada(2)
            .idFuncion(2) //Funcion con todas entradas vendidas
            .build();        
        Mockito.when(entrada_Service.guardar(entradaDTOInvalida)).thenThrow(new CapacidadExcedidaException("No hay más capacidad disponible para esta función"));
        mockMvc.perform(MockMvcRequestBuilders.post("/entradas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOInvalida)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("No hay más capacidad disponible para esta función"));
    }

    @Test
    void testBuscarEntradaPorId() throws Exception {
        Mockito.when(entrada_Service.buscarDTOPorId(1)).thenReturn(entradaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.get("/entradas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEntrada").value(1))
            .andExpect(jsonPath("$.precio").value(1000.0));
    }

    @Test
    void testBuscarEntradaPorId_NoEncontrada() throws Exception {
        Mockito.when(entrada_Service.buscarDTOPorId(99)).thenThrow(new EntradaNoEncontradaException("Entrada con id: 99 no encontrada"));
        mockMvc.perform(MockMvcRequestBuilders.get("/entradas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Entrada con id: 99 no encontrada"));
    }

    @Test
    void testListarEntradas() throws Exception {
        Mockito.when(entrada_Service.listarEntradas()).thenReturn(List.of(entradaDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/entradas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idEntrada").value(1))
            .andExpect(jsonPath("$[0].tipoEntrada").value("unica"));
    }

    @Test
    void testActualizarEntrada() throws Exception {
        EntradaDTO entradaDTOPut = EntradaDTO.builder()
            .tipoEntrada("B")
            .precio(1500.0)
            .build();
        Mockito.when(entrada_Service.actualizar(1, entradaDTOPut)).thenReturn(entradaDTOPut);
        mockMvc.perform(MockMvcRequestBuilders.put("/entradas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entradaDTOPut)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.precio").value(1500.0))
            .andExpect(jsonPath("$.tipoEntrada").value("B"));
    }

    @Test
    void testBorrarEntrada() throws Exception {
        Mockito.doNothing().when(entrada_Service).eliminarPorId(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/entradas/1"))
            .andExpect(status().isNoContent());
    }
}