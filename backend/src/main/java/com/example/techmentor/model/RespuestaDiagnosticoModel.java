package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RespuestaDiagnosticoDto;
import com.example.techmentor.bean.entity.RespuestaDiagnostico;
import com.example.techmentor.bean.entity.ResultadoDiagnostico;
import com.example.techmentor.bean.mapper.RespuestaDiagnosticoMapper;
import com.example.techmentor.persistence.RespuestaDiagnosticoRepository;
import com.example.techmentor.persistence.ResultadoDiagnosticoRepository;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.RespuestaDiagnosticoUseCase;

/** Consulta de respuestas de un resultado (solo lectura; dueño o staff). */
@Service
@RequiredArgsConstructor
public class RespuestaDiagnosticoModel implements RespuestaDiagnosticoUseCase {

    private final RespuestaDiagnosticoRepository respuestaRepository;
    private final ResultadoDiagnosticoRepository resultadoRepository;
    private final RespuestaDiagnosticoMapper respuestaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDiagnosticoDto> listarPorResultado(Long idResultado, UsuarioAutenticado usuario) {
        ResultadoDiagnostico resultado = resultadoRepository.findById(idResultado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resultado no encontrado: " + idResultado));
        usuario.exigirPropietarioOStaff(resultado.getUsuario().getIdUsuario());

        return respuestaRepository.findByResultado_IdResultado(idResultado)
                .stream()
                .map(respuestaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaDiagnosticoDto obtener(Long id, UsuarioAutenticado usuario) {
        RespuestaDiagnostico respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Respuesta no encontrada: " + id));
        usuario.exigirPropietarioOStaff(respuesta.getResultado().getUsuario().getIdUsuario());
        return respuestaMapper.toDto(respuesta);
    }
}
