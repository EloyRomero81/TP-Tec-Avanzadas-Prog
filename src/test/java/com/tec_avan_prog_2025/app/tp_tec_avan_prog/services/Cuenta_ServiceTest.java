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

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CuentaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions.CuentaNoEncontradaException;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers.CuentaMapper;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios.Repo_Cuenta;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class Cuenta_ServiceTest {
    @InjectMocks
    private Cuenta_Service cuenta_Service;
    @Mock
    private Repo_Cuenta repo_Cuenta;
    @Mock 
    private CuentaMapper cuentaMapper;

    public CuentaDTO cuentaDTOPreparada;
    public Cuenta cuentaPreparada;

    @BeforeEach
    void setUp(){
        cuentaPreparada = Cuenta.builder()
            .idCuenta(1)
            .tipoCuenta(TipoCuenta.USUARIO)
            .nombre("Juancito")
            .email("Juancito@gmail.com")
            .build();
        cuentaDTOPreparada = CuentaDTO.builder()
            .idCuenta(cuentaPreparada.getIdCuenta())
            .tipoCuenta(cuentaPreparada.getTipoCuenta())
            .nombre(cuentaPreparada.getNombre())
            .email(cuentaPreparada.getEmail())
            .build();
    }
   
    @Test
    @DisplayName("Cuenta_Service Test - Encontrar CuentaDTO por Id")
    void testBuscarCuentaDTOPorId() {
        Mockito.when(repo_Cuenta.findById(1)).thenReturn(Optional.of(cuentaPreparada));
        Mockito.when(cuentaMapper.cuentaToCuentaDTO(cuentaPreparada)).thenReturn(cuentaDTOPreparada);
        CuentaDTO cuenta = cuenta_Service.buscarDTOPorId(cuentaDTOPreparada.getIdCuenta());
        assertEquals(cuentaPreparada.getIdCuenta(), cuenta.getIdCuenta());
    }

    @Test
    @DisplayName("Cuenta_Service Test - NO encontrar CuentaDTO por Id")
    void testBuscarCuentaDTOPorIdException() {
        Integer idInexistente = 200;
        Mockito.when(repo_Cuenta.findById(idInexistente)).thenReturn(Optional.empty());
        CuentaNoEncontradaException ex = assertThrows(
            CuentaNoEncontradaException.class,
            () -> cuenta_Service.buscarDTOPorId(idInexistente)
        );
        assertEquals("Cuenta con id: "+idInexistente+" no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Cuenta_Service Test - Listar cuentas")
    void testListarCuentas() {
        Cuenta cuenta2 = Cuenta.builder()
            .idCuenta(2)
            .tipoCuenta(TipoCuenta.USUARIO)
            .build();
        CuentaDTO cuentaDTO2 = CuentaDTO.builder()
            .idCuenta(cuenta2.getIdCuenta())
            .tipoCuenta(cuenta2.getTipoCuenta())
            .build();
        List<Cuenta> cuentas = List.of(cuentaPreparada, cuenta2);
        Mockito.when(cuentaMapper.cuentaToCuentaDTO(cuenta2)).thenReturn(cuentaDTO2);
        Mockito.when(cuentaMapper.cuentaToCuentaDTO(cuentaPreparada)).thenReturn(cuentaDTOPreparada);
        Mockito.when(repo_Cuenta.findAll()).thenReturn(cuentas);
        List<CuentaDTO> resultado = cuenta_Service.listarCuentas();
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getIdCuenta());
        assertEquals(2, resultado.get(1).getIdCuenta());
    }

    @Test
    @DisplayName("Cuenta_Service Test - Guardar cuenta")
    void testGuardarCuenta() {
        Mockito.when(cuentaMapper.cuentaDTOToCuenta(cuentaDTOPreparada)).thenReturn(cuentaPreparada);
        Mockito.when(cuentaMapper.cuentaToCuentaDTO(cuentaPreparada)).thenReturn(cuentaDTOPreparada);
        Mockito.when(repo_Cuenta.save(cuentaPreparada)).thenReturn(cuentaPreparada);
        CuentaDTO guardado = cuenta_Service.guardar(cuentaDTOPreparada);
        assertNotNull(guardado);
        assertEquals("Juancito", guardado.getNombre());
    }    

    @Test
    @DisplayName("Cuenta_Service Test - Actualizar cuenta")
    void testActualizarCuenta() {
        Integer id = 1;
        Cuenta cuentaActualizada = Cuenta.builder()
            .idCuenta(1)
            .tipoCuenta(TipoCuenta.USUARIO)
            .nombre("Emanuelito actualizado")
            .email("Emanuelitoactualizado@gmail.com")
            .build();
        CuentaDTO cuentaActualizadaDTO = CuentaDTO.builder()
            .idCuenta(cuentaActualizada.getIdCuenta())
            .tipoCuenta(cuentaActualizada.getTipoCuenta())
            .nombre(cuentaActualizada.getNombre())
            .email(cuentaActualizada.getEmail())
            .build();
        Mockito.when(repo_Cuenta.findById(id)).thenReturn(Optional.of(cuentaPreparada));
        Mockito.when(repo_Cuenta.save(cuentaActualizada)).thenReturn(cuentaActualizada);
        Mockito.when(cuentaMapper.cuentaToCuentaDTO(cuentaActualizada)).thenReturn(cuentaActualizadaDTO);
        CuentaDTO resultado = cuenta_Service.actualizar(id, cuentaActualizadaDTO);
        assertNotNull(resultado);
        assertEquals("Emanuelito actualizado", resultado.getNombre());
    }

    @Test
    @DisplayName("Cuenta_Service Test - Eliminar cuenta por id")
    void testEliminarCuentaPorId() {
        Integer id = 1;
        Mockito.when(repo_Cuenta.findById(id)).thenReturn(Optional.of(cuentaPreparada));
        cuenta_Service.eliminarPorId(id);
        Mockito.verify(repo_Cuenta).findById(id); //verifica que se haga el llamado del método
        Mockito.verify(repo_Cuenta).deleteById(id);
    }
}
