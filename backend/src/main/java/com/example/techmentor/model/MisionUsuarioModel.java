package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.MisionUsuarioDto;
import com.example.techmentor.bean.dto.ProgresoMisionDto;
import com.example.techmentor.bean.entity.Mision;
import com.example.techmentor.bean.entity.MisionUsuario;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.bean.mapper.MisionUsuarioMapper;
import com.example.techmentor.persistence.MisionRepository;
import com.example.techmentor.persistence.MisionUsuarioRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.usecase.MisionUsuarioUseCase;

/** Lógica de misiones del usuario: unirse, ver avance y completar (otorga el XP de la misión). */
@Service
@RequiredArgsConstructor
public class MisionUsuarioModel implements MisionUsuarioUseCase {

    private final MisionUsuarioRepository misionUsuarioRepository;
    private final MisionRepository misionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MisionUsuarioMapper misionUsuarioMapper;

    @Override
    @Transactional
    public MisionUsuarioDto unirse(Long idMision, Long idUsuario) {
        Mision mision = misionRepository.findById(idMision).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Misión no encontrada: " + idMision));
        OffsetDateTime ahora = OffsetDateTime.now();
        if (!Boolean.TRUE.equals(mision.getActiva())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La misión no está activa");
        }
        if (mision.getFechaInicio() != null && ahora.isBefore(mision.getFechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La misión aún no comienza");
        }
        if (mision.getFechaFin() != null && ahora.isAfter(mision.getFechaFin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La misión ya terminó");
        }
        if (misionUsuarioRepository.existsByUsuario_IdUsuarioAndMision_IdMision(idUsuario, idMision)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya te uniste a esta misión");
        }
        MisionUsuario creada = misionUsuarioRepository.saveAndFlush(MisionUsuario.builder()
                .usuario(usuarioRepository.getReferenceById(idUsuario))
                .mision(mision)
                .build());
        return misionUsuarioMapper.toDto(creada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MisionUsuarioDto> misMisiones(Long idUsuario) {
        return listarPorUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MisionUsuarioDto> listarPorUsuario(Long idUsuario) {
        return misionUsuarioRepository.findByUsuario_IdUsuario(idUsuario)
                .stream().map(misionUsuarioMapper::toDto).toList();
    }

    @Override
    @Transactional
    public MisionUsuarioDto actualizarProgreso(Long idMisionUsuario, ProgresoMisionDto dto) {
        MisionUsuario mu = misionUsuarioRepository.findById(idMisionUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Misión de usuario no encontrada: " + idMisionUsuario));
        if (Boolean.TRUE.equals(mu.getCompletada())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La misión ya fue completada");
        }
        mu.setProgresoActual(dto.getProgresoActual());
        if (mu.getProgresoActual() >= mu.getMision().getMeta()) {
            mu.setCompletada(true);
            mu.setFechaCompletada(OffsetDateTime.now());
            Usuario usuario = mu.getUsuario();
            usuario.setXpTotal(usuario.getXpTotal() + mu.getMision().getXpRecompensa());
            usuarioRepository.save(usuario);
        }
        return misionUsuarioMapper.toDto(misionUsuarioRepository.saveAndFlush(mu));
    }
}
