package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;

@Repository
public interface Repo_Artista extends JpaRepository<Artista, Integer>{
    Optional<Artista> findByNombre(String nombre);
}
