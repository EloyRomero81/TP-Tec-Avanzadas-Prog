package com.tec_avan_prog_2025.app.tp_tec_avan_prog.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuenta;

    @Enumerated(EnumType.STRING) //asegura que se guarde como "USUARIO" o "ADMINISTRADOR" en la base de datos.
    private TipoCuenta tipoCuenta;
    
    private String nombre;
    private String email;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true) //Si se borra una cuenta, se borra sus entradas
    private List<Entrada> entradas;

    public enum TipoCuenta {
        USUARIO,
        ADMINISTRADOR
    }
}