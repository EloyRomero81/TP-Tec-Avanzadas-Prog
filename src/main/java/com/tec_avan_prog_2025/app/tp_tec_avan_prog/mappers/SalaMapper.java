package com.tec_avan_prog_2025.app.tp_tec_avan_prog.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.DTO.SalaDTO;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Funcion;
import com.tec_avan_prog_2025.app.tp_tec_avan_prog.models.Sala;

@Mapper(componentModel = "spring", uses = FuncionMapper.class)
public interface SalaMapper {
    @Mapping(target = "funciones", expression = "java(mapFuncionesToIds(sala.getFunciones()))")
    SalaDTO salaToSalaDTO(Sala sala);

    @Mapping(target = "funciones", ignore = true)
    Sala salaDTOToSala(SalaDTO salaDTO); 

    // Método auxiliar para transformar funciones a lista de IDs
    default List<Integer> mapFuncionesToIds(List<Funcion> funciones) {
        if (funciones == null) return null;
        return funciones.stream()
                       .map(Funcion::getIdFuncion)
                       .collect(Collectors.toList());
    }
}
