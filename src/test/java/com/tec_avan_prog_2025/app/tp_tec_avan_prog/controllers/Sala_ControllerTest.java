package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.SalaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.SalaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Sala_Service;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Sala_Controller.class)
@AutoConfigureMockMvc
class Sala_ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private Sala_Service sala_Service;

    private SalaDTO salaDTOPreparada;

    @BeforeEach
    void setUp() {
        salaDTOPreparada = SalaDTO.builder()
                .nroSala(1)
                .tipoSala("Sala Principal")
                .capacidad(70)
                .build();
    }

    @Test
    void testListarSalas() throws Exception {
        Mockito.when(sala_Service.listarSalas()).thenReturn(List.of(salaDTOPreparada));
        mockMvc.perform(MockMvcRequestBuilders.get("/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nroSala").value(1))
                .andExpect(jsonPath("$[0].tipoSala").value("Sala Principal"));
    }

    @Test
    void testBuscarSalaPorId() throws Exception {
        Mockito.when(sala_Service.buscarDTOPorId(1)).thenReturn(salaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.get("/salas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nroSala").value(1))
                .andExpect(jsonPath("$.tipoSala").value("Sala Principal"));
    }

    @Test
    void testBuscarSalaPorIdException() throws Exception {
        Mockito.when(sala_Service.buscarDTOPorId(99)).thenThrow(new SalaNoEncontradaException("Sala con id: 99 no encontrada"));
        mockMvc.perform(MockMvcRequestBuilders.get("/salas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Sala con id: 99 no encontrada"));
    }

    @Test
    void testInsertarSala() throws Exception {
        Mockito.when(sala_Service.guardar(salaDTOPreparada)).thenReturn(salaDTOPreparada);
        mockMvc.perform(MockMvcRequestBuilders.post("/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaDTOPreparada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nroSala").value(1))
                .andExpect(jsonPath("$.tipoSala").value("Sala Principal"));
    }

    @Test
    void testActualizarSala() throws Exception {
        SalaDTO salaActualizada = SalaDTO.builder()
                .nroSala(1)
                .tipoSala("Anfiteatro")
                .capacidad(120)
                .build();
        Mockito.when(sala_Service.actualizar(1, salaActualizada)).thenReturn(salaActualizada);
        mockMvc.perform(MockMvcRequestBuilders.put("/salas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoSala").value("Anfiteatro"))
                .andExpect(jsonPath("$.capacidad").value(120));
    }
    @Test
    void testBorrarSala() throws Exception {
        Mockito.doNothing().when(sala_Service).eliminarPorId(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/salas/1"))
                .andExpect(status().isNoContent());
    }
}
