package com.tec_avan_prog_2025.app.tp_tec_avan_prog.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.ArtistaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.ArtistaNoEncontradoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.services.Artista_Service;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 

@WebMvcTest(Artista_Controller.class)
@AutoConfigureMockMvc
public class Artista_ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private Artista_Service artista_Service;
    
    private ArtistaDTO artistaDTOPreparado;

    @BeforeEach
    void setUp(){
        artistaDTOPreparado = ArtistaDTO.builder()
            .idArtista(1)
            .nombre("Metallica")
            .build();
    }

    @Test
    void testInsertarArtista() throws Exception {
        ArtistaDTO artistaDTOPost = ArtistaDTO.builder()
            .nombre(artistaDTOPreparado.getNombre())
            .build();
        Mockito.when(artista_Service.guardar(artistaDTOPost)).thenReturn(artistaDTOPreparado);
        mockMvc.perform(MockMvcRequestBuilders.post("/artistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(artistaDTOPost)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Metallica"));
    }

    @Test
    void testBuscarArtistaPorId() throws Exception {
        Mockito.when(artista_Service.buscarDTOPorId(1)).thenReturn(artistaDTOPreparado);
        mockMvc.perform(MockMvcRequestBuilders.get("/artistas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idArtista").value(1))
            .andExpect(jsonPath("$.nombre").value("Metallica"));
    }

    @Test
    void testBuscarArtistaPorId_NoEncontrado() throws Exception {
        Mockito.when(artista_Service.buscarDTOPorId(99)).thenThrow(new ArtistaNoEncontradoException("Artista con id: 99 no encontrado"));
        mockMvc.perform(MockMvcRequestBuilders.get("/artistas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Artista con id: 99 no encontrado"));
    }

    @Test
    void testListarArtistas() throws Exception {
        Mockito.when(artista_Service.listarArtistas()).thenReturn(List.of(artistaDTOPreparado));
        mockMvc.perform(MockMvcRequestBuilders.get("/artistas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Metallica"));
    }

    @Test
    void testActualizarArtista() throws Exception {
        ArtistaDTO artistaDTOPut = ArtistaDTO.builder()
            .nombre("Metallica actualizada")
            .build();
        Mockito.when(artista_Service.actualizar(1, artistaDTOPut)).thenReturn(artistaDTOPut);
        mockMvc.perform(MockMvcRequestBuilders.put("/artistas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(artistaDTOPut)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Metallica actualizada"));
    }

    @Test
    void testBorrarArtista() throws Exception {
        Mockito.doNothing().when(artista_Service).eliminarPorId(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/artistas/1"))
            .andExpect(status().isNoContent());
    }
}
