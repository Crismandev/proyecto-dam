package com.example.techmentor.model;

import com.example.techmentor.bean.dto.ProgresoMisionDto;
import com.example.techmentor.bean.entity.Mision;
import com.example.techmentor.bean.entity.MisionUsuario;
import com.example.techmentor.bean.entity.Recompensa;
import com.example.techmentor.bean.entity.RecompensaUsuario;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.bean.mapper.MisionUsuarioMapper;
import com.example.techmentor.bean.mapper.RecompensaUsuarioMapper;
import com.example.techmentor.persistence.MisionRepository;
import com.example.techmentor.persistence.MisionUsuarioRepository;
import com.example.techmentor.persistence.RecompensaRepository;
import com.example.techmentor.persistence.RecompensaUsuarioRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremiosUsuarioModelTest {

    @Mock private MisionUsuarioRepository misionUsuarioRepository;
    @Mock private MisionRepository misionRepository;
    @Mock private MisionUsuarioMapper misionUsuarioMapper;
    @Mock private RecompensaUsuarioRepository recompensaUsuarioRepository;
    @Mock private RecompensaRepository recompensaRepository;
    @Mock private RecompensaUsuarioMapper recompensaUsuarioMapper;
    @Mock private UsuarioRepository usuarioRepository;

    private MisionUsuarioModel misiones;
    private RecompensaUsuarioModel recompensas;
    private Usuario usuario;

    @BeforeEach
    void preparar() {
        misiones = new MisionUsuarioModel(misionUsuarioRepository, misionRepository,
                usuarioRepository, misionUsuarioMapper);
        recompensas = new RecompensaUsuarioModel(recompensaUsuarioRepository, recompensaRepository,
                usuarioRepository, recompensaUsuarioMapper);
        // Los 40 XP incluyen una lección confirmada antes de adquirir el bloqueo.
        usuario = Usuario.builder().idUsuario(7L).xpTotal(40).monedas(100).build();
    }

    @Test
    void sumarMisionConservaLosXpDeLeccionesYLeeElEstadoDespuesDelBloqueo() {
        MisionUsuario mision = prepararMision(false);

        misiones.actualizarProgreso(10L, new ProgresoMisionDto(1));

        assertEquals(60, usuario.getXpTotal());
        assertTrue(mision.getCompletada());
        var orden = inOrder(misionUsuarioRepository, usuarioRepository);
        orden.verify(misionUsuarioRepository).buscarIdUsuario(10L);
        orden.verify(usuarioRepository).bloquearPorId(7L);
        orden.verify(misionUsuarioRepository).findById(10L);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void misionYaCompletadaPorOtraSolicitudNoVuelveAOtorgarXp() {
        prepararMision(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> misiones.actualizarProgreso(10L, new ProgresoMisionDto(1)));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals(40, usuario.getXpTotal());
        verify(usuarioRepository, never()).save(any());
        verify(misionUsuarioRepository, never()).saveAndFlush(any());
    }

    @Test
    void misionIncompletaSoloGuardaProgresoSinDarPremio() {
        MisionUsuario mision = prepararMision(false);

        misiones.actualizarProgreso(10L, new ProgresoMisionDto(0));

        assertFalse(mision.getCompletada());
        assertEquals(40, usuario.getXpTotal());
        verify(usuarioRepository, never()).save(any());
        verify(misionUsuarioRepository).saveAndFlush(mision);
    }

    @Test
    void reclamarSumaAlSaldoActualSinDuplicarElPremioAlReintentar() {
        RecompensaUsuario recompensa = prepararRecompensa();

        recompensas.reclamar(20L, 7L);

        assertTrue(recompensa.getReclamada());
        assertEquals(60, usuario.getXpTotal());
        var orden = inOrder(usuarioRepository, recompensaUsuarioRepository);
        orden.verify(usuarioRepository).bloquearPorId(7L);
        orden.verify(recompensaUsuarioRepository).findById(20L);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> recompensas.reclamar(20L, 7L));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals(60, usuario.getXpTotal());
        verify(usuarioRepository, times(1)).save(usuario);
        verify(recompensaUsuarioRepository, times(1)).saveAndFlush(recompensa);
    }

    @Test
    void reclamarUnaRecompensaAjenaNoModificaNingunSaldo() {
        RecompensaUsuario recompensa = prepararRecompensa();
        recompensa.setUsuario(Usuario.builder().idUsuario(8L).xpTotal(500).build());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> recompensas.reclamar(20L, 7L));

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        assertFalse(recompensa.getReclamada());
        assertEquals(40, usuario.getXpTotal());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void comprarUnaRecompensaDescuentaMonedasDelSaldoBloqueadoYConservaXp() {
        Recompensa recompensa = Recompensa.builder().idRecompensa(30L).activa(true)
                .tipo("XP").cantidad(20).costoMonedas(25).build();
        when(recompensaRepository.findById(30L)).thenReturn(Optional.of(recompensa));
        when(usuarioRepository.bloquearPorId(7L)).thenReturn(Optional.of(usuario));

        recompensas.obtener(30L, 7L);

        assertEquals(75, usuario.getMonedas());
        assertEquals(40, usuario.getXpTotal());
        verify(usuarioRepository).save(usuario);
        verify(usuarioRepository, never()).findById(any());
    }

    private MisionUsuario prepararMision(boolean completada) {
        MisionUsuario mision = MisionUsuario.builder().idMisionUsuario(10L)
                .usuario(Usuario.builder().idUsuario(7L).xpTotal(30).build())
                .mision(Mision.builder().meta(1).xpRecompensa(20).build())
                .completada(completada).build();
        when(misionUsuarioRepository.buscarIdUsuario(10L)).thenReturn(Optional.of(7L));
        when(usuarioRepository.bloquearPorId(7L)).thenReturn(Optional.of(usuario));
        when(misionUsuarioRepository.findById(10L)).thenReturn(Optional.of(mision));
        return mision;
    }

    private RecompensaUsuario prepararRecompensa() {
        RecompensaUsuario recompensa = RecompensaUsuario.builder().idRecompensaUsuario(20L)
                .usuario(Usuario.builder().idUsuario(7L).xpTotal(30).build())
                .recompensa(Recompensa.builder().tipo("XP").cantidad(20).build())
                .reclamada(false).build();
        when(usuarioRepository.bloquearPorId(7L)).thenReturn(Optional.of(usuario));
        when(recompensaUsuarioRepository.findById(20L)).thenReturn(Optional.of(recompensa));
        return recompensa;
    }
}
