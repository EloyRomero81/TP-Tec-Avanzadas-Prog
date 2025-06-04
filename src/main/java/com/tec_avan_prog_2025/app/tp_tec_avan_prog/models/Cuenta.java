package com.tec_avan_prog_2025.app.tp_tec_avan_prog.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuenta;

    private String tipoCuenta;
    private String nombre;
    private String email;

    @OneToMany(mappedBy = "cuenta")
    private List<Entrada> entradas;
}