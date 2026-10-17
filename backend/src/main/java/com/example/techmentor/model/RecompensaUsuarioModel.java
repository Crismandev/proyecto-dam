package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RecompensaUsuarioDto;
import com.example.techmentor.bean.entity.Recompensa;
import com.example.techmentor.bean.entity.RecompensaUsuario;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.bean.mapper.RecompensaUsuarioMapper;
import com.example.techmentor.persistence.RecompensaRepository;
import com.example.techmentor.persistence.RecompensaUsuarioRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.usecase.RecompensaUsuarioUseCase;

/** Lógica de recompensas del usuario: obtener (con costo o racha) y reclamar (suma XP o monedas). */
@Service
@RequiredArgsConstructor
public class RecompensaUsuarioModel implements RecompensaUsuarioUseCase {

    private final RecompensaUsuarioRepository recompensaUsuarioRepository;
    private final RecompensaRepository recompensaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecompensaUsuarioMapper recompensaUsuarioMapper;

    @Override
    @Transactional
    public RecompensaUsuarioDto obtener(Long idRecompensa, Long idUsuario) {
        Recompensa recompensa = recompensaRepository.findById(idRecompensa)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recompensa no encontrada: " + idRecompensa));
        if (!Boolean.TRUE.equals(recompensa.getActiva())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La recompensa no está activa");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado: " + idUsuario));

        // Recompensas de racha: requieren N días seguidos y se obtienen una sola vez
        if (recompensa.getDiaSecuenciaRequerido() != null) {
            if (usuario.getRachaActual() < recompensa.getDiaSecuenciaRequerido()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Necesitas una racha de " + recompensa.getDiaSecuenciaRequerido() + " días");
            }
            if (recompensaUsuarioRepository.existsByUsuario_IdUsuarioAndRecompensa_IdRecompensa(idUsuario, idRecompensa)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya obtuviste esta recompensa");
            }
        }
        // Recompensas con costo: se descuentan monedas
        if (recompensa.getCostoMonedas() > 0) {
            if (usuario.getMonedas() < recompensa.getCostoMonedas()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monedas insuficientes");
            }
            usuario.setMonedas(usuario.getMonedas() - recompensa.getCostoMonedas());
            usuarioRepository.save(usuario);
        }

        RecompensaUsuario creada = recompensaUsuarioRepository.saveAndFlush(RecompensaUsuario.builder()
                .usuario(usuario).recompensa(recompensa).build());
        return recompensaUsuarioMapper.toDto(creada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecompensaUsuarioDto> misRecompensas(Long idUsuario) {
        return listarPorUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecompensaUsuarioDto> listarPorUsuario(Long idUsuario) {
        return recompensaUsuarioRepository.findByUsuario_IdUsuarioOrderByFechaObtencionDesc(idUsuario)
                .stream().map(recompensaUsuarioMapper::toDto).toList();
    }

    @Override
    @Transactional
    public RecompensaUsuarioDto reclamar(Long idRecompensaUsuario, Long idUsuario) {
        RecompensaUsuario ru = recompensaUsuarioRepository.findById(idRecompensaUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recompensa de usuario no encontrada: " + idRecompensaUsuario));
        if (!ru.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta recompensa no es tuya");
        }
        if (Boolean.TRUE.equals(ru.getReclamada())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La recompensa ya fue reclamada");
        }
        ru.setReclamada(true);

        Recompensa recompensa = ru.getRecompensa();
        Usuario usuario = ru.getUsuario();
        if ("XP".equals(recompensa.getTipo())) {
            usuario.setXpTotal(usuario.getXpTotal() + recompensa.getCantidad());
        } else if ("MONEDAS".equals(recompensa.getTipo())) {
            usuario.setMonedas(usuario.getMonedas() + recompensa.getCantidad());
        }
        usuarioRepository.save(usuario);
        return recompensaUsuarioMapper.toDto(recompensaUsuarioRepository.saveAndFlush(ru));
    }
}
