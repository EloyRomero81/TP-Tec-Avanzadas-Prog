package com.tec_avan_prog_2025.app.tp_tec_avan_prog.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;

@Repository
public interface Repo_Funcion extends JpaRepository<Funcion, Integer>{
    List<Funcion> findBySalaAndFecha(Sala sala, LocalDate fecha);
}
