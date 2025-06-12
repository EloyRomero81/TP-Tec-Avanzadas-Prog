package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.ArtistaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.ArtistaNoEncontradoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.ArtistaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Artista;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class Artista_ServiceTest {
    @InjectMocks
    private Artista_Service artista_Service;
    @Mock
    private Repo_Artista repoArtista;
    @Mock
    private ArtistaMapper artistaMapper;
    @Mock
    private Funcion_Service funcionService;

    public ArtistaDTO artistaDTOPreparado;
    public Artista artistaPreparado;

    @BeforeEach
    void setUp() {
        artistaPreparado = Artista.builder()
            .idArtista(1)
            .nombre("Metallica")
            .build();   
        artistaDTOPreparado = ArtistaDTO.builder()
            .idArtista(artistaPreparado.getIdArtista())
            .nombre(artistaPreparado.getNombre())
            .build();  
    }

    @Test
    @DisplayName("Artista_Service Test - Encontrar ArtistaDTO por Id")
    void testBuscarArtistaDTOPorId(){
        Mockito.when(repoArtista.findById(1)).thenReturn(Optional.of(artistaPreparado));
        Mockito.when(artistaMapper.artistaToArtistaDTO(artistaPreparado)).thenReturn(artistaDTOPreparado);
        ArtistaDTO artista = artista_Service.buscarDTOPorId(artistaDTOPreparado.getIdArtista());
        assertEquals(artistaPreparado.getIdArtista(), artista.getIdArtista());
    }

    @Test
    @DisplayName("Artista_Service Test - NO encontrar ArtistaDTO por Id")
    void testBuscarArtistaDTOPorIdException(){
        Integer idInexistente = 200;
        Mockito.when(repoArtista.findById(idInexistente)).thenReturn(Optional.empty());
        ArtistaNoEncontradoException ex = assertThrows(
            ArtistaNoEncontradoException.class,
            () -> artista_Service.buscarDTOPorId(idInexistente)
        );
        assertEquals("Artista con id: "+idInexistente+" no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Artista_Service Test - Listar artistas")
    void testListarArtistas(){
        Artista artista2 = Artista.builder()
            .idArtista(2)
            .nombre("Katy Perry")
            .build();
        ArtistaDTO artistaDTO2 = ArtistaDTO.builder()
            .idArtista(artista2.getIdArtista())
            .nombre(artista2.getNombre())
            .build();
        List<Artista> artistas = List.of(artistaPreparado, artista2);
        Mockito.when(artistaMapper.artistaToArtistaDTO(artista2)).thenReturn(artistaDTO2);
        Mockito.when(artistaMapper.artistaToArtistaDTO(artistaPreparado)).thenReturn(artistaDTOPreparado);
        Mockito.when(repoArtista.findAll()).thenReturn(artistas);
        List<ArtistaDTO> resultado = artista_Service.listarArtistas();
        assertEquals(2, resultado.size());
        assertEquals("Metallica", resultado.get(0).getNombre());
        assertEquals("Katy Perry", resultado.get(1).getNombre());
    }

    @Test
    @DisplayName("Artista_Service Test - Guardar artista")
    void testGuardarArtista(){
        Mockito.when(artistaMapper.artistaDTOToArtista(artistaDTOPreparado)).thenReturn(artistaPreparado);
        Mockito.when(artistaMapper.artistaToArtistaDTO(artistaPreparado)).thenReturn(artistaDTOPreparado);
        Mockito.when(repoArtista.save(artistaPreparado)).thenReturn(artistaPreparado);
        ArtistaDTO guardado = artista_Service.guardar(artistaDTOPreparado);
        assertNotNull(guardado);
        assertEquals("Metallica", guardado.getNombre());
    }


    @Test
    @DisplayName("Artista_Service Test - Actualizar artista")
    void testActualizarArtista() {
        Integer id = 1;
        Artista artistaActualizado = Artista.builder()
            .idArtista(id)
            .nombre("Metallica Actualizado")
            .build();
        ArtistaDTO artistaActualizadoDTO = ArtistaDTO.builder()
            .idArtista(artistaActualizado.getIdArtista())
            .nombre(artistaActualizado.getNombre())
            .build();
        Mockito.when(repoArtista.findById(id)).thenReturn(Optional.of(artistaPreparado));
        Mockito.when(repoArtista.save(artistaActualizado)).thenReturn(artistaActualizado);
        Mockito.when(artistaMapper.artistaToArtistaDTO(artistaActualizado)).thenReturn(artistaActualizadoDTO);
        ArtistaDTO resultado = artista_Service.actualizar(id, artistaActualizadoDTO);
        assertNotNull(resultado);
        assertEquals("Metallica Actualizado", resultado.getNombre());
    }

    @Test
    @DisplayName("Artista_Service Test - Eliminar artista por id")
    void testEliminarArtistaPorId() {
        Integer id = 1;
        Mockito.when(repoArtista.findById(id)).thenReturn(Optional.of(artistaPreparado));
        artista_Service.eliminarPorId(id);
        Mockito.verify(repoArtista).findById(id); //verifica que se haga el llamado del método
        Mockito.verify(funcionService).desvincularFuncionesDeArtista(id);
        Mockito.verify(repoArtista).deleteById(id);
    }
}
