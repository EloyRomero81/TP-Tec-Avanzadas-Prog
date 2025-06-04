package com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaDTO {
    private Integer idEntrada;
    private Integer idCuenta;
    private Integer idFuncion;
    private String tipoEntrada;
    private Double precio;
}
