package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;

@DataJpaTest
@ActiveProfiles("test")
public class Repo_EntradaTest {
    @Autowired
    private Repo_Entrada repo_Entrada;

    @Autowired
    private TestEntityManager entityManager;  

    //Como solo hay un test (y si se quiere compartir las instancias) esto está bien, pero sino habria que buildearlas en setUp.
    public Sala salaPreparada = Sala.builder()
        .tipoSala("Anfiteatro")
        .capacidad(120)
        .build();

    public Artista artistaPreparado = Artista.builder()
        .nombre("Luis fonsi")
        .build();

    public Funcion funcionPreparada = Funcion.builder() //Sala y artista se setean en setUp para que queden persistentes
        .fecha(LocalDate.now())
        .hora(LocalTime.now())
        .duracion(60)
        .tipoFuncion("Infantil")
        .precioBaseEntrada(500.0)
        .build();

    public Cuenta cuentaPreparada = Cuenta.builder()
        .tipoCuenta(TipoCuenta.USUARIO)
        .nombre("Usuario")
        .email("usuario@gmail.com")
        .build();

    @BeforeEach
    void setUp() {
        repo_Entrada.deleteAll();
        entityManager.persist(salaPreparada);
        entityManager.persist(artistaPreparado);
        entityManager.persist(cuentaPreparada);
        funcionPreparada.setSala(salaPreparada);
        funcionPreparada.setArtista(artistaPreparado);
        entityManager.persist(funcionPreparada);
        entityManager.flush(); //Para asegurar que los IDs se generen
    }

    @Test
    @DisplayName("Repo_Entrada Test - Contar las entradas de una función")
    void testCountByFuncion_IdFuncion() {
        Entrada e1 = Entrada.builder()
            .cuenta(cuentaPreparada)
            .funcion(funcionPreparada)
            .tipoEntrada("Unica")
            .precio(500.0)
            .build();
        Entrada e2 = Entrada.builder()
            .cuenta(cuentaPreparada)
            .funcion(funcionPreparada)
            .tipoEntrada("Unica")
            .precio(500.0)
            .build();
        repo_Entrada.saveAll(List.of(e1, e2));
        long count = repo_Entrada.countByFuncion_IdFuncion(funcionPreparada.getIdFuncion());
        assertEquals(2, count);
        List<Entrada> entradas = repo_Entrada.findAll();
        assertEquals(funcionPreparada.getIdFuncion(), entradas.get(0).getFuncion().getIdFuncion());
    }
}