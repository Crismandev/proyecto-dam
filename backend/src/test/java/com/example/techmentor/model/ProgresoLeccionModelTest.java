package com.example.techmentor.model;

import com.example.techmentor.bean.dto.CompletarLeccionDto;
import com.example.techmentor.bean.dto.RespuestaLeccionDto;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.entity.Ejercicio;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.bean.entity.OpcionEjercicio;
import com.example.techmentor.bean.entity.ProgresoLeccion;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.bean.mapper.ProgresoLeccionMapper;
import com.example.techmentor.persistence.EjercicioRepository;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.persistence.OpcionEjercicioRepository;
import com.example.techmentor.persistence.ProgresoLeccionRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.security.UsuarioAutenticado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgresoLeccionModelTest {

    private static final Long ID_USUARIO = 1L;
    private static final Long ID_LECCION = 10L;
    private static final UsuarioAutenticado ALUMNO =
            new UsuarioAutenticado(ID_USUARIO, "alumno-prueba", "ALUMNO");

    @Mock private ProgresoLeccionRepository progresoLeccionRepository;
    @Mock private LeccionRepository leccionRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ProgresoCursoGestor progresoCursoGestor;
    @Mock private ProgresoLeccionMapper progresoLeccionMapper;
    @Mock private EjercicioRepository ejercicioRepository;
    @Mock private OpcionEjercicioRepository opcionEjercicioRepository;
    @InjectMocks private ProgresoLeccionModel model;

    private Usuario usuario;
    private Leccion leccion;
    private ProgresoLeccion progreso;

    @BeforeEach
    void prepararEntidades() {
        usuario = Usuario.builder().idUsuario(ID_USUARIO).xpTotal(30).build();
        Curso curso = Curso.builder().idCurso(20L).activo(true).build();
        Nivel nivel = Nivel.builder().idNivel(30L).curso(curso).orden(1).build();
        leccion = Leccion.builder().idLeccion(ID_LECCION).nivel(nivel)
                .titulo("Lección de prueba").xpRecompensa(10).build();
        progreso = ProgresoLeccion.builder().idProgresoLeccion(40L)
                .usuario(usuario).leccion(leccion).estado(EstadoProgreso.EN_CURSO).build();
    }

    @Test
    void calculaAciertosEnServidorYActualizaXpDelPerfil() {
        Ejercicio primero = ejercicio(100L, null);
        Ejercicio segundo = ejercicio(200L, null);
        Ejercicio tercero = ejercicio(300L, "SELECT");
        prepararContenido(List.of(primero, segundo, tercero), List.of(
                opcion(101L, primero, true), opcion(102L, primero, false),
                opcion(201L, segundo, true), opcion(202L, segundo, false)));
        prepararProgreso();

        model.completar(ID_LECCION, solicitud(
                respuesta(100L, 101L, null),
                respuesta(200L, 202L, null),
                respuesta(300L, null, "  SELECT  ")), ALUMNO);

        assertEquals(2, progreso.getPuntaje());
        assertEquals(EstadoProgreso.COMPLETADO, progreso.getEstado());
        assertEquals(1, progreso.getIntentos());
        assertEquals(10, progreso.getXpGanado());
        assertEquals(40, usuario.getXpTotal());
        assertNotNull(progreso.getFechaCompletado());
        verify(usuarioRepository).save(usuario);
        verify(progresoLeccionRepository).saveAndFlush(progreso);
        verify(progresoCursoGestor).registrarLeccionCompletada(ID_USUARIO, leccion, 10);
    }

    @Test
    void repetirConservaElMejorPuntajeYNoDuplicaElPremio() {
        Ejercicio primero = ejercicio(100L, null);
        Ejercicio segundo = ejercicio(200L, "SELECT");
        prepararContenido(List.of(primero, segundo), List.of(
                opcion(101L, primero, true), opcion(102L, primero, false)));
        prepararProgreso();

        model.completar(ID_LECCION, solicitud(
                respuesta(100L, 102L, null), respuesta(200L, null, "select")), ALUMNO);
        assertEquals(0, progreso.getPuntaje());
        assertEquals(40, usuario.getXpTotal());

        model.completar(ID_LECCION, solicitud(
                respuesta(100L, 101L, null), respuesta(200L, null, "SELECT")), ALUMNO);
        assertEquals(2, progreso.getPuntaje());

        model.completar(ID_LECCION, solicitud(
                respuesta(100L, 102L, null), respuesta(200L, null, "UPDATE")), ALUMNO);

        assertEquals(2, progreso.getPuntaje());
        assertEquals(3, progreso.getIntentos());
        assertEquals(10, progreso.getXpGanado());
        assertEquals(40, usuario.getXpTotal());
        verify(usuarioRepository, times(1)).save(usuario);
        verify(progresoCursoGestor, times(1)).registrarLeccionCompletada(ID_USUARIO, leccion, 10);
        verify(progresoLeccionRepository, times(3)).saveAndFlush(progreso);
    }

    @ParameterizedTest(name = "Rechaza {0} sin guardar progreso ni premios")
    @MethodSource("respuestasInvalidas")
    void rechazaRespuestasInvalidasSinOtorgarPremios(String caso, List<RespuestaLeccionDto> respuestas) {
        Ejercicio primero = ejercicio(100L, null);
        Ejercicio segundo = ejercicio(200L, null);
        // La validación puede terminar antes de consultar las opciones o incluso los ejercicios.
        lenient().when(leccionRepository.findById(ID_LECCION)).thenReturn(Optional.of(leccion));
        lenient().when(ejercicioRepository.findByLeccion_IdLeccionOrderByOrdenAsc(ID_LECCION))
                .thenReturn(List.of(primero, segundo));
        lenient().when(opcionEjercicioRepository.findAllByLeccion(ID_LECCION)).thenReturn(List.of(
                opcion(101L, primero, true), opcion(102L, primero, false),
                opcion(201L, segundo, true), opcion(202L, segundo, false)));
        lenient().when(usuarioRepository.bloquearPorId(ID_USUARIO)).thenReturn(Optional.of(usuario));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> model.completar(ID_LECCION,
                        CompletarLeccionDto.builder().respuestas(respuestas).build(), ALUMNO));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals(30, usuario.getXpTotal());
        verify(progresoLeccionRepository, never()).saveAndFlush(any(ProgresoLeccion.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verifyNoInteractions(progresoCursoGestor);
    }

    static Stream<Arguments> respuestasInvalidas() {
        return Stream.of(
                Arguments.of("lista nula", null),
                Arguments.of("respuestas omitidas", List.of(respuesta(100L, 101L, null))),
                Arguments.of("ejercicio duplicado", List.of(
                        respuesta(100L, 101L, null), respuesta(100L, 102L, null))),
                Arguments.of("ejercicio ajeno", List.of(
                        respuesta(100L, 101L, null), respuesta(999L, 201L, null))),
                Arguments.of("opción de otro ejercicio", List.of(
                        respuesta(100L, 201L, null), respuesta(200L, 201L, null))),
                Arguments.of("opción inexistente", List.of(
                        respuesta(100L, 999L, null), respuesta(200L, 201L, null))),
                Arguments.of("opción y texto juntos", List.of(
                        respuesta(100L, 101L, "respuesta"), respuesta(200L, 201L, null))),
                Arguments.of("entrada nula", Arrays.asList(null, respuesta(200L, 201L, null))),
                Arguments.of("identificador ausente", List.of(
                        respuesta(null, 101L, null), respuesta(200L, 201L, null))),
                Arguments.of("opción ausente", List.of(
                        respuesta(100L, null, null), respuesta(200L, 201L, null)))
        );
    }

    @Test
    void noCompletaContenidoQueCareceDeRespuestaCorrecta() {
        prepararContenido(List.of(ejercicio(100L, null)), List.of());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> model.completar(ID_LECCION,
                        solicitud(respuesta(100L, null, "respuesta")), ALUMNO));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(progresoLeccionRepository, never()).saveAndFlush(any(ProgresoLeccion.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verifyNoInteractions(progresoCursoGestor);
    }

    @Test
    void noCompletaUnEjercicioConVariasOpcionesCorrectas() {
        Ejercicio ejercicio = ejercicio(100L, null);
        prepararContenido(List.of(ejercicio), List.of(
                opcion(101L, ejercicio, true), opcion(102L, ejercicio, true)));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> model.completar(ID_LECCION,
                        solicitud(respuesta(100L, 101L, null)), ALUMNO));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(progresoLeccionRepository, never()).saveAndFlush(any(ProgresoLeccion.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verifyNoInteractions(progresoCursoGestor);
    }

    @Test
    void permiteTerminarUnaLeccionDeLecturaSinInventarPuntaje() {
        prepararContenido(List.of(), List.of());
        prepararProgreso();

        model.completar(ID_LECCION, solicitud(), ALUMNO);

        assertEquals(EstadoProgreso.COMPLETADO, progreso.getEstado());
        assertEquals(0, progreso.getPuntaje());
        assertEquals(40, usuario.getXpTotal());
        verify(progresoCursoGestor).registrarLeccionCompletada(ID_USUARIO, leccion, 10);
    }

    private void prepararContenido(List<Ejercicio> ejercicios, List<OpcionEjercicio> opciones) {
        when(leccionRepository.findById(ID_LECCION)).thenReturn(Optional.of(leccion));
        when(ejercicioRepository.findByLeccion_IdLeccionOrderByOrdenAsc(ID_LECCION)).thenReturn(ejercicios);
        when(opcionEjercicioRepository.findAllByLeccion(ID_LECCION)).thenReturn(opciones);
    }

    private void prepararProgreso() {
        when(usuarioRepository.bloquearPorId(ID_USUARIO)).thenReturn(Optional.of(usuario));
        when(progresoLeccionRepository.findByUsuario_IdUsuarioAndLeccion_IdLeccion(ID_USUARIO, ID_LECCION))
                .thenReturn(Optional.of(progreso));
        when(progresoLeccionRepository.saveAndFlush(any(ProgresoLeccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Ejercicio ejercicio(Long id, String respuestaCorrecta) {
        return Ejercicio.builder().idEjercicio(id).leccion(leccion)
                .respuestaCorrecta(respuestaCorrecta).xpRecompensa(100).build();
    }

    private OpcionEjercicio opcion(Long id, Ejercicio ejercicio, boolean correcta) {
        return OpcionEjercicio.builder().idOpcion(id).ejercicio(ejercicio).esCorrecta(correcta).build();
    }

    private static RespuestaLeccionDto respuesta(Long idEjercicio, Long idOpcion, String texto) {
        return RespuestaLeccionDto.builder().idEjercicio(idEjercicio)
                .idOpcionSeleccionada(idOpcion).respuestaTexto(texto).build();
    }

    private static CompletarLeccionDto solicitud(RespuestaLeccionDto... respuestas) {
        return CompletarLeccionDto.builder().respuestas(List.of(respuestas)).build();
    }
}
