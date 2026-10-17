package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.RecompensaUsuarioDto;
import com.example.techmentor.bean.entity.RecompensaUsuario;

/** Mapper MapStruct: RecompensaUsuario -> RecompensaUsuarioDto. */
@Mapper(componentModel = "spring")
public interface RecompensaUsuarioMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "recompensa.idRecompensa", target = "idRecompensa")
    @Mapping(source = "recompensa.nombre", target = "nombreRecompensa")
    @Mapping(source = "recompensa.tipo", target = "tipo")
    @Mapping(source = "recompensa.cantidad", target = "cantidad")
    RecompensaUsuarioDto toDto(RecompensaUsuario entity);
}
