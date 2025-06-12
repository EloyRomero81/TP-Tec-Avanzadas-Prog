package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;

@DataJpaTest
@ActiveProfiles("test")
public class Repo_ArtistaTest {
    @Autowired
    private Repo_Artista repo_Artista;

    @Test
    @DisplayName("Repo_Artista Test - Encontrar a artista por nombre")
    public void testFindByNombreFound() {
        Artista artista = Artista.builder().nombre("Juan Perez").build();
        repo_Artista.save(artista);

        Optional<Artista> encontrado = repo_Artista.findByNombre("Juan Perez");
        assertTrue(encontrado.isPresent());
        assertEquals("Juan Perez", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("Repo_Artista Test - NO encontrar a artista por nombre")
    public void testFindByNombreNotFound() {
        Optional<Artista> encontrado = repo_Artista.findByNombre("Artista Fantasma");
        assertFalse(encontrado.isPresent());
    }
}
