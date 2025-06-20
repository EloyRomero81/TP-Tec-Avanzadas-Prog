package com.tec_avan_prog_2025.app.tp_tec_avan_prog.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFuncion;

    @ManyToOne
    @JoinColumn(name = "nroSala", nullable = true)
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "idArtista", nullable = true)
    private Artista artista;

    private LocalDate fecha;
    private LocalTime hora; 
    private Integer duracion;
    private String tipoFuncion;
    private Double precioBaseEntrada;

    @OneToMany(mappedBy = "funcion", cascade = CascadeType.ALL, orphanRemoval = true) //Si se borra una funcion, se borra sus entradas
    @Builder.Default
    private List<Entrada> entradas = new ArrayList<>();
}
