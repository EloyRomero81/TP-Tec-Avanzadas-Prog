package com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.ArtistaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Artista;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;

@Mapper(componentModel = "spring")
public interface ArtistaMapper {
    ArtistaDTO artistaToArtistaDTO(Artista artista);

    @Mapping(target = "funciones", ignore = true)
    Artista artistaDTOToArtista(ArtistaDTO artistaDTO);

    // Método auxiliar para transformar funciones a lista de IDs
    default List<Integer> mapFuncionesToIds(List<Funcion> funciones) {
        if (funciones == null) return null;
        return funciones.stream()
                       .map(Funcion::getIdFuncion)
                       .collect(Collectors.toList());
    }
}


