package com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.FuncionDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Entrada;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;

@Mapper(componentModel = "spring")
public interface FuncionMapper {

    @Mapping(source = "sala.nroSala", target = "nroSala")
    @Mapping(source = "artista.nombre", target = "nombreArtista")
    FuncionDTO funcionToFuncionDTO(Funcion funcion);

    @Mapping(target = "sala", ignore = true) // Se ignora el atributo de sala y se resuelve en service
    @Mapping(target = "artista", ignore = true) // Lo mismo con artista y entradas
    @Mapping(target = "entradas", ignore = true)
    Funcion funcionDTOToFuncion(FuncionDTO funcionDTO);

    // Método auxiliar para transformar entradas a lista de IDs
    default List<Integer> mapEntradasToIds(List<Entrada> entradas) {
        if (entradas == null) return null;
        return entradas.stream()
                       .map(Entrada::getIdEntrada)
                       .collect(Collectors.toList());
    }
}
