package com.tec_avan_prog_2025.app.tp_tec_avan_prog.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "entradas")
@Data
@NoArgsConstructor
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEntrada;

    @ManyToOne
    @JoinColumn(name = "idCuenta", nullable = true)
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "idFuncion", nullable = true)
    private Funcion funcion;

    private String tipoEntrada;
    private Double precio;
}
