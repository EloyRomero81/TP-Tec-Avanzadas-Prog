package com.tec_avan_prog_2025.app.tp_tec_avan_prog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CrearFuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.AccesoDenegadoException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.FuncionNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.SuperposicionFuncionException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.FuncionMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class Funcion_ServiceTest {
    @InjectMocks
    private Funcion_Service funcion_Service;
    @Mock
    private Repo_Funcion repo_Funcion;
    @Mock
    private FuncionMapper funcionMapper;
    @Mock
    private Sala_Service sala_Service;
    @Mock
    private Artista_Service artista_Service;
    @Mock
    private Cuenta_Service cuenta_Service;

    public Funcion funcionPreparada;
    public FuncionDTO funcionDTOPreparada;
    public Sala salaPreparada;
    public Artista artistaPreparado;
    public Cuenta cuentaPreparada;
    public CrearFuncionDTO crearFuncionDTO;

    @BeforeEach
    void setUp() {
        artistaPreparado = Artista.builder()
            .idArtista(1)
            .nombre("Metallica")
            .build();
        salaPreparada = Sala.builder()
            .nroSala(1)
            .tipoSala("Anfiteatro")
            .capacidad(120)
            .build();
        funcionPreparada = Funcion.builder()
            .idFuncion(3)
            .sala(salaPreparada)
            .artista(artistaPreparado)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.of(19, 0))
            .duracion(60)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(850.0)
            .entradas(new ArrayList<>())
            .build();
        funcionDTOPreparada = FuncionDTO.builder()
            .idFuncion(funcionPreparada.getIdFuncion())
            .nroSala(salaPreparada.getNroSala())
            .nombreArtista(artistaPreparado.getNombre())
            .fecha(funcionPreparada.getFecha())
            .hora(funcionPreparada.getHora())
            .duracion(funcionPreparada.getDuracion())
            .tipoFuncion(funcionPreparada.getTipoFuncion())
            .precioBaseEntrada(funcionPreparada.getPrecioBaseEntrada())
            .entradasVendidas(funcionPreparada.getEntradas().size())
            .entradasDisponibles(funcionPreparada.getSala().getCapacidad() - funcionPreparada.getEntradas().size())
            .build();
    }

    @Test
    @DisplayName("Funcion_Service Test - Encontrar FuncionDTO por Id")
    void testBuscarFuncionDTOPorId() {
        Mockito.when(repo_Funcion.findById(3)).thenReturn(Optional.of(funcionPreparada));
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionPreparada)).thenReturn(funcionDTOPreparada);
        FuncionDTO funcion = funcion_Service.buscarDTOPorId(funcionDTOPreparada.getIdFuncion());
        assertEquals(funcionPreparada.getIdFuncion(), funcion.getIdFuncion());
    }

    @Test
    @DisplayName("Funcion_Service Test - No encontrar FuncionDTO por Id")
    void testBuscarFuncionDTOPorIdException() {
        Integer idInexistente = 200;
        Mockito.when(repo_Funcion.findById(idInexistente)).thenReturn(Optional.empty());
        FuncionNoEncontradaException ex = assertThrows(
            FuncionNoEncontradaException.class,
            () -> funcion_Service.buscarDTOPorId(idInexistente)
        );
        assertEquals("Funcion con id: "+idInexistente+" no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Funcion_Service Test - Listar funciones")
    void testListarFunciones() {
        Funcion funcion2 = Funcion.builder()
            .idFuncion(4)
            .sala(salaPreparada)
            .artista(artistaPreparado)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.now())
            .duracion(60)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(850.0)
            .entradas(new ArrayList<>())
            .build();        
        FuncionDTO funcionDTO2 = FuncionDTO.builder()
            .idFuncion(funcion2.getIdFuncion())
            .nroSala(salaPreparada.getNroSala())
            .nombreArtista(artistaPreparado.getNombre())
            .fecha(funcion2.getFecha())
            .hora(funcion2.getHora())
            .duracion(funcion2.getDuracion())
            .tipoFuncion(funcion2.getTipoFuncion())
            .precioBaseEntrada(funcion2.getPrecioBaseEntrada())
            .entradasVendidas(funcion2.getEntradas().size())
            .entradasDisponibles(funcion2.getSala().getCapacidad() - funcion2.getEntradas().size())
            .build();  
        List<Funcion> funciones = List.of(funcionPreparada, funcion2);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcion2)).thenReturn(funcionDTO2);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionPreparada)).thenReturn(funcionDTOPreparada);
        Mockito.when(repo_Funcion.findAll()).thenReturn(funciones);
        List<FuncionDTO> resultado = funcion_Service.listarFunciones();
        assertEquals(2, resultado.size());
        assertEquals(funcionPreparada.getIdFuncion(), resultado.get(0).getIdFuncion());
        assertEquals(funcion2.getIdFuncion(), resultado.get(1).getIdFuncion());
    } 

    @Test
    @DisplayName("Funcion_Service Test - Listar funciones anteriores")
    void testListarFuncionesAnteriores() {
        Funcion funcionPasada = Funcion.builder()
            .idFuncion(1)
            .sala(salaPreparada)
            .fecha(LocalDate.now().minusDays(5))
            .build();
        FuncionDTO funcionPasadaDTO = FuncionDTO.builder()
            .idFuncion(funcionPasada.getIdFuncion())
            .nroSala(salaPreparada.getNroSala())
            .fecha(funcionPasada.getFecha())
            .build();
        Funcion funcionFutura = Funcion.builder()
            .idFuncion(2)
            .sala(salaPreparada)
            .fecha(LocalDate.now().plusDays(5))
            .build();
        List<Funcion> funciones = Arrays.asList(funcionPasada, funcionFutura);
        Mockito.when(repo_Funcion.findAll()).thenReturn(funciones);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionPasada)).thenReturn(funcionPasadaDTO);
        List<FuncionDTO> resultado = funcion_Service.listarFuncionesAnteriores();
        assertEquals(1, resultado.size());
        assertEquals(funcionPasada.getIdFuncion(), resultado.get(0).getIdFuncion());
    }

    @Test
    @DisplayName("Funcion_Service Test - Listar funciones próximas")
    void testListarFuncionesProximas() {
        Funcion funcionPasada = Funcion.builder()
            .idFuncion(1)
            .sala(salaPreparada)
            .fecha(LocalDate.now().minusDays(5))
            .build();
        Funcion funcionFutura = Funcion.builder()
            .idFuncion(2)
            .sala(salaPreparada)
            .fecha(LocalDate.now().plusDays(5))
            .build();
        FuncionDTO funcionFuturaDTO = FuncionDTO.builder()
            .idFuncion(funcionFutura.getIdFuncion())
            .nroSala(salaPreparada.getNroSala())
            .fecha(funcionFutura.getFecha())
            .build();
        List<Funcion> funciones = Arrays.asList(funcionPasada, funcionFutura);
        Mockito.when(repo_Funcion.findAll()).thenReturn(funciones);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionFutura)).thenReturn(funcionFuturaDTO);
        List<FuncionDTO> resultado = funcion_Service.listarFuncionesProximas();
        assertEquals(1, resultado.size());
        assertEquals(funcionFutura.getIdFuncion(), resultado.get(0).getIdFuncion());
    }

    @Test
    @DisplayName("Funcion_Service Test - Guardar funcion")
    void testGuardarFuncion() {
        cuentaPreparada = Cuenta.builder()
            .idCuenta(4)
            .tipoCuenta(TipoCuenta.ADMINISTRADOR)
            .build();
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(4)
            .build();         
        Mockito.when(funcionMapper.crearFuncionDTOToFuncion(crearFuncionDTO)).thenReturn(funcionPreparada);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionPreparada)).thenReturn(funcionDTOPreparada);
        Mockito.when(cuenta_Service.buscarPorId(crearFuncionDTO.getIdCuenta())).thenReturn(cuentaPreparada);
        Mockito.when(sala_Service.buscarPorId(crearFuncionDTO.getNroSala())).thenReturn(salaPreparada);
        Mockito.when(artista_Service.buscarPorNombre(crearFuncionDTO.getNombreArtista())).thenReturn(artistaPreparado);
        Mockito.when(repo_Funcion.save(funcionPreparada)).thenReturn(funcionPreparada);
        FuncionDTO guardado = funcion_Service.guardar(crearFuncionDTO);
        assertNotNull(guardado);
        assertEquals(funcionPreparada.getIdFuncion(), guardado.getIdFuncion());
    }
    
    @Test
    @DisplayName("Funcion_Service Test - Error al guardar funcion - Usuario no es ADMIN")
    void testGuardarFuncionException_CuentaInvalida() {
        cuentaPreparada = Cuenta.builder()
            .idCuenta(5)
            .tipoCuenta(TipoCuenta.USUARIO)
            .build();
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(5)
            .build();         
        Mockito.when(cuenta_Service.buscarPorId(crearFuncionDTO.getIdCuenta())).thenReturn(cuentaPreparada);
        AccesoDenegadoException ex = assertThrows(
            AccesoDenegadoException.class, 
            () -> funcion_Service.guardar(crearFuncionDTO)
        );
        assertEquals("Solo los administradores pueden crear funciones", ex.getMessage());
    }

    @Test
    @DisplayName("Funcion_Service Test - Error al guardar Funcion - Funcion con superposición")
    void testGuardarFuncionException_FuncionConSuperposicion() {
        cuentaPreparada = Cuenta.builder()
            .idCuenta(4)
            .tipoCuenta(TipoCuenta.ADMINISTRADOR)
            .build();
        Funcion nuevaFuncion = Funcion.builder()
            .idFuncion(6)
            .sala(salaPreparada)
            .artista(artistaPreparado)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.of(20, 0))
            .duracion(60)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(850.0)
            .entradas(new ArrayList<>())
            .build();
        CrearFuncionDTO crearFuncionDTO = CrearFuncionDTO.builder()
            .idCuenta(4)
            .idFuncion(6)
            .nroSala(salaPreparada.getNroSala())
            .nombreArtista(artistaPreparado.getNombre())
            .fecha(nuevaFuncion.getFecha())
            .hora(nuevaFuncion.getHora())
            .duracion(nuevaFuncion.getDuracion())
            .tipoFuncion(nuevaFuncion.getTipoFuncion())
            .precioBaseEntrada(nuevaFuncion.getPrecioBaseEntrada())
            .build();
        Mockito.when(funcionMapper.crearFuncionDTOToFuncion(crearFuncionDTO)).thenReturn(nuevaFuncion);
        Mockito.when(cuenta_Service.buscarPorId(4)).thenReturn(cuentaPreparada);
        Mockito.when(sala_Service.buscarPorId(salaPreparada.getNroSala())).thenReturn(salaPreparada);
        Mockito.when(artista_Service.buscarPorNombre(artistaPreparado.getNombre())).thenReturn(artistaPreparado);
        Mockito.when(repo_Funcion.findBySalaAndFecha(salaPreparada, nuevaFuncion.getFecha())).thenReturn(List.of(funcionPreparada));
        SuperposicionFuncionException ex = assertThrows(
            SuperposicionFuncionException.class, 
            () -> funcion_Service.guardar(crearFuncionDTO)
        );
        assertEquals("La función se superpone con otra función en la sala", ex.getMessage());
    }    
    
    @Test
    @DisplayName("Funcion_Service Test - Actualizar funcion")
    void testActualizarFuncion() {
        Integer id = 3;
        Funcion funcionActualizada = Funcion.builder()
            .idFuncion(id)
            .sala(salaPreparada)
            .artista(artistaPreparado)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.now())
            .duracion(60)
            .tipoFuncion("Infantil Actualizada")
            .precioBaseEntrada(850.0)
            .entradas(new ArrayList<>())
            .build();
        FuncionDTO funcionDTOActualizada = FuncionDTO.builder()
            .idFuncion(id)
            .nroSala(funcionActualizada.getSala().getNroSala())
            .nombreArtista(funcionActualizada.getArtista().getNombre())
            .fecha(funcionActualizada.getFecha())
            .hora(funcionActualizada.getHora())
            .duracion(funcionActualizada.getDuracion())
            .tipoFuncion(funcionActualizada.getTipoFuncion())
            .precioBaseEntrada(funcionActualizada.getPrecioBaseEntrada())
            .entradasVendidas(funcionActualizada.getEntradas().size())
            .entradasDisponibles(funcionActualizada.getSala().getCapacidad() - funcionActualizada.getEntradas().size())
            .build();
        Mockito.when(sala_Service.buscarPorId(funcionDTOActualizada.getNroSala())).thenReturn(salaPreparada);
        Mockito.when(artista_Service.buscarPorNombre(funcionDTOActualizada.getNombreArtista())).thenReturn(artistaPreparado);
        Mockito.when(repo_Funcion.findById(id)).thenReturn(Optional.of(funcionPreparada));
        Mockito.when(repo_Funcion.save(Mockito.any(Funcion.class))).thenReturn(funcionActualizada);
        Mockito.when(funcionMapper.funcionToFuncionDTO(funcionActualizada)).thenReturn(funcionDTOActualizada);
        FuncionDTO resultado = funcion_Service.actualizar(id, funcionDTOActualizada);
        assertNotNull(resultado);
        assertEquals("Infantil Actualizada", resultado.getTipoFuncion());        
    }


    @Test
    @DisplayName("Funcion_Service Test - Eliminar Funcion por Id")
    void testEliminarFuncionPorId() {
        Integer id = 1;
        Mockito.when(repo_Funcion.findById(id)).thenReturn(Optional.of(funcionPreparada));
        funcion_Service.eliminarPorId(id);
        Mockito.verify(repo_Funcion).findById(id); //verifica que se haga el llamado del método
        Mockito.verify(repo_Funcion).deleteById(id);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Funcion_Service Test - Desvincular Funciones de un Artista")
    void testDesvincularFuncionesDeArtista() {
        Funcion funcion2 = Funcion.builder().idFuncion(6).sala(salaPreparada).build();
        Funcion funcion3 = Funcion.builder().idFuncion(7).sala(salaPreparada).build();
        Funcion funcion4 = Funcion.builder().idFuncion(8).sala(null).build(); // no matchea
        List<Funcion> todasLasFunciones = List.of(funcionPreparada, funcion2, funcion3, funcion4);
        Mockito.when(repo_Funcion.findAll()).thenReturn(todasLasFunciones);
        funcion_Service.desvincularFuncionesDeSala(1);
        ArgumentCaptor<List<Funcion>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(repo_Funcion).saveAll(captor.capture());
        List<Funcion> funcionesGuardadas = captor.getValue();
        assertEquals(3, funcionesGuardadas.size());
        assertTrue(funcionesGuardadas.stream().allMatch(f -> f.getSala() == null));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Funcion_Service Test - Desvincular Funciones de una Sala")
    void testDesvincularFuncionesDeSala() {
        Funcion funcion2 = Funcion.builder().idFuncion(8).artista(artistaPreparado).build();
        Funcion funcion3 = Funcion.builder().idFuncion(9).artista(artistaPreparado).build();
        Funcion funcion4= Funcion.builder().idFuncion(10).artista(null).build(); // no matchea
        List<Funcion> todasLasFunciones = List.of(funcionPreparada,funcion2, funcion3, funcion4);
        Mockito.when(repo_Funcion.findAll()).thenReturn(todasLasFunciones);
        funcion_Service.desvincularFuncionesDeArtista(1);
        ArgumentCaptor<List<Funcion>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(repo_Funcion).saveAll(captor.capture());
        List<Funcion> funcionesGuardadas = captor.getValue();
        assertEquals(3, funcionesGuardadas.size());
        assertTrue(funcionesGuardadas.stream().allMatch(f -> f.getArtista() == null));
    }    
}
