package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;

@DataJpaTest
@ActiveProfiles("test") // Si usás application-test.properties para H2
public class Repo_FuncionTest {
    @Autowired
    private Repo_Funcion repo_Funcion;
    @Autowired
    private TestEntityManager entityManager;  

    //Como se quiere compartir las instancias esto está bien, pero sino habria que buildearlas en setUp.
    public Sala salaPreparada = Sala.builder()
        .tipoSala("Anfiteatro")
        .capacidad(120)
        .build();

    public Artista artistaPreparado = Artista.builder()
        .nombre("Luis fonsi")
        .build();

    @BeforeEach
    void setUp() {
        repo_Funcion.deleteAll();
        entityManager.persist(salaPreparada);
        entityManager.persist(artistaPreparado);
        entityManager.flush();
    }

    @Test
    @DisplayName("Repo_Funcion Test - Encontrar funciones por Sala y Fecha")
    void testFindBySalaAndFechaFound() {
        Funcion funcion = Funcion.builder()
            .sala(salaPreparada)
            .artista(artistaPreparado)
            .fecha(LocalDate.of(2025, 12, 8))
            .hora(LocalTime.now())
            .duracion(60)
            .tipoFuncion("Infantil")
            .precioBaseEntrada(500.0)
            .build();
        repo_Funcion.save(funcion);
        List<Funcion> funciones = repo_Funcion.findBySalaAndFecha(funcion.getSala(), LocalDate.of(2025, 12, 8));
        assertEquals(1, funciones.size());
        assertEquals(funcion.getIdFuncion(), funciones.get(0).getIdFuncion());
    }

    @Test
    @DisplayName("Repo_Funcion Test - No encontrar funcion por Sala y Fecha")
    public void testFindBySalaAndFechaNotFound(){
        List<Funcion> funciones = repo_Funcion.findBySalaAndFecha(salaPreparada, LocalDate.of(2025, 12, 8));
        assertTrue(funciones.isEmpty());
    }
}