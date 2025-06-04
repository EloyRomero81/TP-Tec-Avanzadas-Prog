package com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.EntradaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;

@Mapper(componentModel = "spring")
public interface EntradaMapper {

    @Mapping(source = "cuenta.idCuenta", target = "idCuenta")
    @Mapping(source = "funcion.idFuncion", target = "idFuncion")
    EntradaDTO entradaToEntradaDTO(Entrada entrada);

    @Mapping(target = "cuenta", ignore = true)   // Cuenta y funcion se ignora, se resuelven en service
    @Mapping(target = "funcion", ignore = true)
    Entrada entradaDTOToEntrada(EntradaDTO entradaDTO);
}
