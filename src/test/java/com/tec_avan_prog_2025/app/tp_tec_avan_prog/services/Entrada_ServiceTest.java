package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.EntradaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CapacidadExcedidaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.EntradaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionPasadaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.TipoEntradaInvalidoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.EntradaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Entrada;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class Entrada_ServiceTest {
    @InjectMocks
    private Entrada_Service entrada_Service;
    @Mock
    private Repo_Entrada repo_Entrada;
    @Mock
    private EntradaMapper entradaMapper;
    @Mock
    private Cuenta_Service cuenta_Service;
    @Mock
    private Funcion_Service funcion_Service;

    public EntradaDTO entradaDTOPreparada;
    public Entrada entradaPreparada;
    public Funcion funcionPreparada;
    public Sala salaPreparada;
    public Cuenta cuentaPreparada;


    @BeforeEach
    void setUp() {
        salaPreparada = Sala.builder()
            .nroSala(1)
            .tipoSala("Anfiteatro")
            .capacidad(120)
            .build();
        funcionPreparada = Funcion.builder()
            .idFuncion(3)
            .sala(salaPreparada)
            .artista(null)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.now())
            .duracion(60)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(850.0)
            .build();
        cuentaPreparada = Cuenta.builder()
            .idCuenta(4)
            .tipoCuenta(TipoCuenta.USUARIO)
            .build();
        entradaPreparada = Entrada.builder()
            .cuenta(cuentaPreparada)
            .idEntrada(1)
            .funcion(funcionPreparada)
            .tipoEntrada("unica")
            .precio(850.0)
            .build();  
        entradaDTOPreparada = EntradaDTO.builder()
            .idCuenta(cuentaPreparada.getIdCuenta())
            .idEntrada(entradaPreparada.getIdEntrada())
            .idFuncion(funcionPreparada.getIdFuncion())
            .tipoEntrada(entradaPreparada.getTipoEntrada())
            .precio(entradaPreparada.getPrecio())
            .build(); 
    }
    @Test
    @DisplayName("Entrada_Service Test - Encontrar EntradaDTO por Id")
    void testBuscarEntradaDTOPorId() {
        Mockito.when(repo_Entrada.findById(1)).thenReturn(Optional.of(entradaPreparada));
        Mockito.when(entradaMapper.entradaToEntradaDTO(entradaPreparada)).thenReturn(entradaDTOPreparada);
        EntradaDTO entrada = entrada_Service.buscarDTOPorId(entradaDTOPreparada.getIdEntrada());
        assertEquals(entradaPreparada.getIdEntrada(), entrada.getIdEntrada());
    }

    @Test
    @DisplayName("Entrada_Service Test - No encontrar EntradaDTO por Id")
    void testBuscarEntradaDTOPorIdException() {
        Integer idInexistente = 200;
        Mockito.when(repo_Entrada.findById(idInexistente)).thenReturn(Optional.empty());
        EntradaNoEncontradaException ex = assertThrows(
            EntradaNoEncontradaException.class,
            () -> entrada_Service.buscarDTOPorId(idInexistente)
        );
        assertEquals("Entrada con id: "+idInexistente+" no encontrada", ex.getMessage());
    }
    
    @Test
    @DisplayName("Entrada_Service Test - Listar Entradas")
    void testListarEntradas() {
        Entrada entrada2 = Entrada.builder()
            .cuenta(cuentaPreparada)
            .idEntrada(2)
            .funcion(funcionPreparada)
            .build();
        EntradaDTO entradaDTO2 = EntradaDTO.builder()
            .idCuenta(entrada2.getCuenta().getIdCuenta())
            .idEntrada(entrada2.getIdEntrada())
            .idFuncion(entrada2.getFuncion().getIdFuncion())
            .build();  
        List<Entrada> entradas = List.of(entradaPreparada, entrada2);
        Mockito.when(entradaMapper.entradaToEntradaDTO(entrada2)).thenReturn(entradaDTO2);
        Mockito.when(entradaMapper.entradaToEntradaDTO(entradaPreparada)).thenReturn(entradaDTOPreparada);
        Mockito.when(repo_Entrada.findAll()).thenReturn(entradas);
        List<EntradaDTO> resultado = entrada_Service.listarEntradas();
        assertEquals(2, resultado.size());
        assertEquals(entradaPreparada.getIdEntrada(), resultado.get(0).getIdEntrada());
        assertEquals(entrada2.getIdEntrada(), resultado.get(1).getIdEntrada());
    } 

    @Test
    @DisplayName("Entrada_Service Test - Guardar Entrada")
    void testGuardarEntrada() {
        Mockito.when(entradaMapper.entradaDTOToEntrada(entradaDTOPreparada)).thenReturn(entradaPreparada);
        Mockito.when(entradaMapper.entradaToEntradaDTO(entradaPreparada)).thenReturn(entradaDTOPreparada);
        Mockito.when(cuenta_Service.buscarPorId(entradaDTOPreparada.getIdCuenta())).thenReturn(cuentaPreparada);
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(repo_Entrada.save(entradaPreparada)).thenReturn(entradaPreparada);
        EntradaDTO guardado = entrada_Service.guardar(entradaDTOPreparada);
        assertNotNull(guardado);
        assertEquals(entradaPreparada.getIdEntrada(), guardado.getIdEntrada());
        assertEquals(entradaPreparada.getPrecio(), guardado.getPrecio());
    }   

    @Test
    @DisplayName("Entrada_Service Test - Error al guardar Entrada - Fecha pasada")
    void testGuardarEntradaException_FechaInvalida() {
        funcionPreparada.setFecha(LocalDate.now().minusDays(1));
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Exception ex = assertThrows(FuncionPasadaException.class, () -> {
            entrada_Service.guardar(entradaDTOPreparada);
        });
        assertEquals("No se pueden comprar entradas para funciones ya realizadas", ex.getMessage());
    }

    @Test
    @DisplayName("Entrada_Service Test - Error al guardar Entrada - Capacidad superada")
    void testGuardarEntradaException_CapacidadExcedida() {
        salaPreparada.setCapacidad(50);
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(repo_Entrada.countByFuncion_IdFuncion(funcionPreparada.getIdFuncion())).thenReturn(50L);  //Capacidad llena
        Exception ex = assertThrows(CapacidadExcedidaException.class, () -> {
            entrada_Service.guardar(entradaDTOPreparada);
        });
        assertEquals("No hay más capacidad disponible para esta función", ex.getMessage());
    }

    @Test
    @DisplayName("Entrada_Service Test - Error al guardar Entrada - Entrada Inválida - Anfiteatro")
    void testGuardarEntradaException_EntradaInvalida_Anfiteatro() {
        entradaDTOPreparada.setTipoEntrada("A");  // Solo "Unica" es válida para Anfiteatro
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(entradaMapper.entradaDTOToEntrada(entradaDTOPreparada)).thenReturn(entradaPreparada);
        Exception ex = assertThrows(TipoEntradaInvalidoException.class, () -> {
            entrada_Service.guardar(entradaDTOPreparada);
        });
        assertEquals("Tipo de entrada inválido para Anfiteatro. Solo Unica disponible.", ex.getMessage());
    }  

    @Test
    @DisplayName("Entrada_Service Test - Error al guardar Entrada - Entrada Inválida - Sala Principal")
    void testGuardarEntradaException_EntradaInvalida_SalaPrincipal() {
        salaPreparada.setTipoSala("sala principal");
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(entradaMapper.entradaDTOToEntrada(entradaDTOPreparada)).thenReturn(entradaPreparada);
        Exception ex = assertThrows(TipoEntradaInvalidoException.class, () -> {
            entrada_Service.guardar(entradaDTOPreparada);
        });
        assertEquals("Tipo de entrada inválido para Sala Principal. Use A o B.", ex.getMessage());
    }  

    @Test
    @DisplayName("Entrada_Service Test - Error al guardar Entrada - Entrada Inválida - Sala desconocida")
    void testGuardarEntradaException_EntradaInvalida_SalaDesconocida() {
        salaPreparada.setTipoSala("sala");
        Mockito.when(funcion_Service.buscarPorId(entradaDTOPreparada.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(entradaMapper.entradaDTOToEntrada(entradaDTOPreparada)).thenReturn(entradaPreparada);
        Exception ex = assertThrows(TipoEntradaInvalidoException.class, () -> {
            entrada_Service.guardar(entradaDTOPreparada);
        });
        assertEquals("Tipo de sala desconocido", ex.getMessage());
    }  

    @Test
    @DisplayName("Entrada_Service Test - Actualizar Entrada")
    void testActualizarEntrada() {
        Integer id = 1;
        Entrada entradaActualizada = Entrada.builder()
            .cuenta(cuentaPreparada)
            .idEntrada(id)
            .funcion(funcionPreparada)
            .tipoEntrada("unica actualizada")
            .precio(1700.0)
            .build();
        EntradaDTO entradaActualizadaDTO = EntradaDTO.builder()
            .idCuenta(entradaActualizada.getCuenta().getIdCuenta())
            .idEntrada(id)
            .idFuncion(entradaActualizada.getFuncion().getIdFuncion())
            .tipoEntrada(entradaActualizada.getTipoEntrada())
            .precio(entradaActualizada.getPrecio())
            .build();
        Mockito.when(repo_Entrada.findById(id)).thenReturn(Optional.of(entradaPreparada));
        Mockito.when(cuenta_Service.buscarPorId(entradaActualizadaDTO.getIdCuenta())).thenReturn(cuentaPreparada);
        Mockito.when(funcion_Service.buscarPorId(entradaActualizadaDTO.getIdFuncion())).thenReturn(funcionPreparada);
        Mockito.when(repo_Entrada.save(entradaActualizada)).thenReturn(entradaActualizada);
        Mockito.when(entradaMapper.entradaToEntradaDTO(entradaActualizada)).thenReturn(entradaActualizadaDTO);
        EntradaDTO resultado = entrada_Service.actualizar(id, entradaActualizadaDTO);
        assertNotNull(resultado);
        assertEquals("unica actualizada", resultado.getTipoEntrada());
    }

    @Test
    @DisplayName("Entrada_Service Test - Borrar Entrada por Id")
    void testEliminarEntradaPorId() {
        Integer id = 1;
        Mockito.when(repo_Entrada.findById(id)).thenReturn(Optional.of(entradaPreparada));
        entrada_Service.eliminarPorId(id);
        Mockito.verify(repo_Entrada).findById(id); //verifica que se haga el llamado del método
        Mockito.verify(repo_Entrada).deleteById(id);
    }
}
