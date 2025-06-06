package com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class crearFuncionDTO {
    private Integer idCuenta;
    private Integer idFuncion;
    private Integer nroSala;
    private String nombreArtista;
    private LocalDate fecha;
    private LocalTime hora; 
    private Integer duracion;
    private String tipoFuncion;
    private Double precioBaseEntrada; 
}
