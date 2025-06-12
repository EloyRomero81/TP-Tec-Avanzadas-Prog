package com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistaDTO {
    private Integer idArtista;
    private String nombre;
    private List<Integer> funciones;
 
}
