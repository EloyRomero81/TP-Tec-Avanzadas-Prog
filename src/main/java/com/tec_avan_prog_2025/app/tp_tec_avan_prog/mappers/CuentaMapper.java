package com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.CuentaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Cuenta;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;

@Mapper(componentModel = "spring")
public interface CuentaMapper {
    CuentaDTO cuentaToCuentaDTO(Cuenta cuenta);

    @Mapping(target = "entradas", ignore = true)
    Cuenta cuentaDTOToCuenta(CuentaDTO cuentaDTO);

    // Método auxiliar para transformar entradas a lista de IDs
    default List<Integer> mapEntradasToIds(List<Entrada> entradas) {
        if (entradas == null) return null;
        return entradas.stream()
                       .map(Entrada::getIdEntrada)
                       .collect(Collectors.toList());
    }
}
