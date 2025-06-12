package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CrearFuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.AccesoDenegadoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.SuperposicionFuncionException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Funcion_Service;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 

@WebMvcTest(Funcion_Controller.class)
@AutoConfigureMockMvc
class Funcion_ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private Funcion_Service funcion_Service;

    private FuncionDTO funcionDTOPreparada;

    @BeforeEach
    void setUp() {
        funcionDTOPreparada = FuncionDTO.builder()
            .idFuncion(1)
            .nombreArtista("Artista Test")
            .tipoFuncion("Infantil")
            .nroSala(1)
            .fecha(LocalDate.now().plusDays(5))
            .hora(LocalTime.of(20, 0))
            .duracion(90)
            .precioBaseEntrada(1000.0)
            .entradasVendidas(0)
            .entradasDisponibles(120)
            .build();
    }

    @Test
    void testListarFunciones() throws Exception {
        Mockito.when(funcion_Service.listarFunciones()).thenReturn(List.of(funcionDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/funciones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArtista").value("Artista Test"));
    }

    @Test
    void testListarFuncionesProximas() throws Exception {
        Mockito.when(funcion_Service.listarFuncionesProximas()).thenReturn(List.of(funcionDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/funciones/proximas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArtista").value("Artista Test"));
    }

    @Test
    void testListarFuncionesAnteriores() throws Exception {
        Mockito.when(funcion_Service.listarFuncionesAnteriores()).thenReturn(List.of(funcionDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/funciones/anteriores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArtista").value("Artista Test"));
    }

    @Test
    void testBuscarFuncionPorId() throws Exception {
        Mockito.when(funcion_Service.buscarDTOPorId(1)).thenReturn(funcionDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.get("/funciones/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idFuncion").value(1))
            .andExpect(jsonPath("$.nombreArtista").value("Artista Test"));
    }

    @Test
    void testBuscarFuncionPorIdException() throws Exception {
        Mockito.when(funcion_Service.buscarDTOPorId(99)).thenThrow(new FuncionNoEncontradaException("Funcion con id: 99 no encontrado"));
        mockMvc.perform(MockMvcRequestBuilders.get("/funciones/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Funcion con id: 99 no encontrado"));
    }

    @Test
    void testInsertarFuncion() throws Exception {
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(1)
            .nombreArtista("Artista Test")
            .nroSala(1)
            .fecha(LocalDate.now().plusDays(5))
            .hora(LocalTime.of(20, 0))
            .duracion(90)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(1000.0)
            .build();
        Mockito.when(funcion_Service.guardar(crearFuncionDTO)).thenReturn(funcionDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.post("/funciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(crearFuncionDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombreArtista").value("Artista Test"));
    }

    @Test
    void testInsertarFuncionException_AccesoDenegado() throws Exception {
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(99)
            .build();
        Mockito.when(funcion_Service.guardar(crearFuncionDTO)).thenThrow(new AccesoDenegadoException("Solo los administradores pueden crear funciones"));
        mockMvc.perform(MockMvcRequestBuilders.post("/funciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(crearFuncionDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Solo los administradores pueden crear funciones"));
    }

    @Test
    void testInsertarFuncionException_SuperposicionFuncion() throws Exception {
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(1)
            .fecha(LocalDate.now().plusDays(5))
            .hora(LocalTime.of(20, 0))
            .build();
        Mockito.when(funcion_Service.guardar(crearFuncionDTO)).thenThrow(new SuperposicionFuncionException("La función se superpone con otra función en la sala"));
        mockMvc.perform(MockMvcRequestBuilders.post("/funciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(crearFuncionDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("La función se superpone con otra función en la sala"));
    }

    @Test
    void testActualizarFuncion() throws Exception {
        FuncionDTO funcionDTOPut = FuncionDTO.builder()
            .nroSala(2)
            .nombreArtista("Artista Actualizado")
            .fecha(LocalDate.now().plusDays(10))
            .hora(LocalTime.of(21, 0))
            .duracion(120)
            .tipoFuncion("Obra de Teatro")
            .precioBaseEntrada(1500.0)
            .build();
        Mockito.when(funcion_Service.actualizar(1, funcionDTOPut)).thenReturn(funcionDTOPut);
        mockMvc.perform(MockMvcRequestBuilders.put("/funciones/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(funcionDTOPut)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreArtista").value("Artista Actualizado"));
    }

    @Test
    void testBorrarFuncion() throws Exception {
        Mockito.doNothing().when(funcion_Service).eliminarPorId(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/funciones/1"))
            .andExpect(status().isNoContent());
    }
}