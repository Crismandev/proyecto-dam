package com.example.techmentor.model;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.AsignarProfesionDto;
import com.example.techmentor.bean.dto.UsuarioProfesionDto;
import com.example.techmentor.bean.entity.Profesion;
import com.example.techmentor.bean.entity.UsuarioProfesion;
import com.example.techmentor.bean.mapper.UsuarioProfesionMapper;
import com.example.techmentor.persistence.ProfesionRepository;
import com.example.techmentor.persistence.UsuarioProfesionRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.usecase.UsuarioProfesionUseCase;

/** Lógica de profesiones del usuario: garantiza que solo una sea la principal (puesto al que postula). */
@Service
@RequiredArgsConstructor
public class UsuarioProfesionModel implements UsuarioProfesionUseCase {

    private final UsuarioProfesionRepository usuarioProfesionRepository;
    private final ProfesionRepository profesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioProfesionMapper usuarioProfesionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioProfesionDto> listarDeUsuario(Long idUsuario) {
        return usuarioProfesionRepository.findByUsuario_IdUsuario(idUsuario)
                .stream().map(usuarioProfesionMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UsuarioProfesionDto asignar(Long idUsuario, AsignarProfesionDto dto) {
        Profesion profesion = profesionRepository.findById(dto.getIdProfesion())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profesión no encontrada: " + dto.getIdProfesion()));
        if (!Boolean.TRUE.equals(profesion.getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La profesión no está activa");
        }
        if (usuarioProfesionRepository.existsByUsuario_IdUsuarioAndProfesion_IdProfesion(idUsuario, dto.getIdProfesion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya tiene esa profesión");
        }

        boolean primera = usuarioProfesionRepository.findByUsuario_IdUsuario(idUsuario).isEmpty();
        boolean principal = primera || Boolean.TRUE.equals(dto.getPrincipal());
        if (principal) {
            quitarPrincipalActual(idUsuario);
        }

        UsuarioProfesion creada = usuarioProfesionRepository.saveAndFlush(UsuarioProfesion.builder()
                .usuario(usuarioRepository.getReferenceById(idUsuario))
                .profesion(profesion)
                .principal(principal)
                .build());
        return usuarioProfesionMapper.toDto(creada);
    }

    @Override
    @Transactional
    public UsuarioProfesionDto marcarPrincipal(Long idUsuario, Long idProfesion) {
        UsuarioProfesion objetivo = buscar(idUsuario, idProfesion);
        quitarPrincipalActual(idUsuario);
        objetivo.setPrincipal(true);
        return usuarioProfesionMapper.toDto(usuarioProfesionRepository.saveAndFlush(objetivo));
    }

    @Override
    @Transactional
    public void quitar(Long idUsuario, Long idProfesion) {
        UsuarioProfesion existente = buscar(idUsuario, idProfesion);
        boolean eraPrincipal = Boolean.TRUE.equals(existente.getPrincipal());
        usuarioProfesionRepository.delete(existente);
        usuarioProfesionRepository.flush();

        // Si quitó la principal, la profesión más antigua que le quede pasa a ser la principal
        if (eraPrincipal) {
            usuarioProfesionRepository.findByUsuario_IdUsuario(idUsuario).stream()
                    .min(Comparator.comparing(UsuarioProfesion::getFechaAsignacion))
                    .ifPresent(otra -> {
                        otra.setPrincipal(true);
                        usuarioProfesionRepository.saveAndFlush(otra);
                    });
        }
    }

    /** Desmarca la principal actual (y hace flush) antes de marcar otra: el índice único parcial lo exige. */
    private void quitarPrincipalActual(Long idUsuario) {
        List<UsuarioProfesion> actuales = usuarioProfesionRepository.findByUsuario_IdUsuario(idUsuario);
        actuales.stream().filter(x -> Boolean.TRUE.equals(x.getPrincipal())).forEach(x -> x.setPrincipal(false));
        usuarioProfesionRepository.saveAllAndFlush(actuales);
    }

    private UsuarioProfesion buscar(Long idUsuario, Long idProfesion) {
        return usuarioProfesionRepository.findByUsuario_IdUsuarioAndProfesion_IdProfesion(idUsuario, idProfesion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El usuario no tiene esa profesión: " + idProfesion));
    }
}
