package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;

@Repository
public interface Repo_Sala extends JpaRepository<Sala, Integer> {
    
}
