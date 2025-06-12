package com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO;

import java.util.List;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta.TipoCuenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaDTO {
    
    private Integer idCuenta;
    private TipoCuenta tipoCuenta;
    private String nombre;
    private String email;
    private List<Integer> entradas;
    
}
