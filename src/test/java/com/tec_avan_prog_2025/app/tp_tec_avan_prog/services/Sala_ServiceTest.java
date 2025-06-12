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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.SalaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.SalaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.SalaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Sala;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class Sala_ServiceTest {
    @InjectMocks
    private Sala_Service sala_Service;
    @Mock
    private Repo_Sala repo_Sala;
    @Mock
    private SalaMapper salaMapper;
    @Mock
    private Funcion_Service funcion_Service;

    public Sala salaPreparada;
    public SalaDTO salaDTOPreparada;

    @BeforeEach
    void setUp(){
        salaPreparada = Sala.builder()
            .nroSala(1)
            .tipoSala("Anfiteatro")
            .capacidad(120)
            .build();
        salaDTOPreparada = SalaDTO.builder()
            .nroSala(salaPreparada.getNroSala())
            .tipoSala(salaPreparada.getTipoSala())
            .capacidad(salaPreparada.getCapacidad())
            .build();
    }

    @Test
    @DisplayName("Sala_Service Test - Encontrar SalaDTO por Id")
    void testBuscarSalaDTOPorId() {
        Mockito.when(repo_Sala.findById(1)).thenReturn(Optional.of(salaPreparada));
        Mockito.when(salaMapper.salaToSalaDTO(salaPreparada)).thenReturn(salaDTOPreparada);
        SalaDTO sala = sala_Service.buscarDTOPorId(salaDTOPreparada.getNroSala());
        assertEquals(salaPreparada.getNroSala(), sala.getNroSala());
    }

    @Test
    @DisplayName("Sala_Service Test - Encontrar SalaDTO por Id")
    void testBuscarSalaDTOPorIdException() {
        Integer idInexistente = 200;
        Mockito.when(repo_Sala.findById(idInexistente)).thenReturn(Optional.empty());
        SalaNoEncontradaException ex = assertThrows(
            SalaNoEncontradaException.class,
            () -> sala_Service.buscarDTOPorId(idInexistente)
        );
        assertEquals("Sala con id: "+idInexistente+" no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Sala_Service Test - Guardar Sala")
    void testGuardarSala() {
        Mockito.when(salaMapper.salaDTOToSala(salaDTOPreparada)).thenReturn(salaPreparada);
        Mockito.when(salaMapper.salaToSalaDTO(salaPreparada)).thenReturn(salaDTOPreparada);
        Mockito.when(repo_Sala.save(salaPreparada)).thenReturn(salaPreparada);
        SalaDTO guardado = sala_Service.guardar(salaDTOPreparada);
        assertNotNull(guardado);
        assertEquals(salaPreparada.getNroSala(), guardado.getNroSala());
    }

    @Test
    @DisplayName("Sala_Service Test - Listar Salas")
    void testListarSalas() {
        Sala sala2 = Sala.builder()
            .nroSala(2)
            .tipoSala("Sala Principal")
            .capacidad(70)
            .build();
        SalaDTO salaDTO2 = SalaDTO.builder()
            .nroSala(sala2.getNroSala())
            .tipoSala(sala2.getTipoSala())
            .capacidad(sala2.getCapacidad())
            .build();
        List<Sala> salas = List.of(salaPreparada, sala2);
        Mockito.when(salaMapper.salaToSalaDTO(sala2)).thenReturn(salaDTO2);
        Mockito.when(salaMapper.salaToSalaDTO(salaPreparada)).thenReturn(salaDTOPreparada);
        Mockito.when(repo_Sala.findAll()).thenReturn(salas);
        List<SalaDTO> resultado = sala_Service.listarSalas();
        assertEquals(2, resultado.size());
        assertEquals(salaPreparada.getNroSala(), resultado.get(0).getNroSala());
        assertEquals(sala2.getNroSala(), resultado.get(1).getNroSala());
    }

    @Test
    @DisplayName("Sala_Service Test - Actualizar Sala")
    void testActualizarSala() {
        Integer id = 1;
        Sala salaActualizada = Sala.builder()
            .nroSala(id)
            .tipoSala("Sala Principal Actualizada")
            .capacidad(80)
            .build();
        SalaDTO salaDTOActualizada = SalaDTO.builder()
            .nroSala(salaActualizada.getNroSala())
            .tipoSala(salaActualizada.getTipoSala())
            .capacidad(salaActualizada.getCapacidad())
            .build();
        Mockito.when(repo_Sala.findById(id)).thenReturn(Optional.of(salaPreparada));
        Mockito.when(repo_Sala.save(salaActualizada)).thenReturn(salaActualizada);
        Mockito.when(salaMapper.salaToSalaDTO(salaActualizada)).thenReturn(salaDTOActualizada);
        SalaDTO resultado = sala_Service.actualizar(id, salaDTOActualizada);
        assertNotNull(resultado);
        assertEquals("Sala Principal Actualizada", resultado.getTipoSala());
    }

    @Test
    @DisplayName("Sala_Service Test - Eliminar Sala por Id")
    void testEliminarSalaPorId() {
        Integer id = 1;
        Mockito.when(repo_Sala.findById(id)).thenReturn(Optional.of(salaPreparada));
        sala_Service.eliminarPorId(id);
        Mockito.verify(repo_Sala).findById(id); //verifica que se haga el llamado del método
        Mockito.verify(repo_Sala).deleteById(id);
    }
}
