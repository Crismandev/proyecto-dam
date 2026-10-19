import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, forkJoin, map, of, switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CategoriaDTO, CursoDTO, MisionDTO, UsuarioDTO } from '../../core/models/interfaces';

type UsuarioRow = UsuarioDTO & { nombreEstado?: string };

interface ProgresoCursoRow {
  idUsuario: number;
  idCurso: number;
  estado: 'NO_INICIADO' | 'EN_CURSO' | 'COMPLETADO' | 'ABANDONADO';
  fechaUltimaActividad?: string;
}

export interface MonthBar {
  label: string;
  value: number;
  percent: number;
  current: boolean;
}

export interface Segment {
  key: string;
  label: string;
  value: number;
  percent: number;
}

export interface CourseRow {
  nombre: string;
  categoria: string;
  inscritos: number;
  completados: number;
  tasa: number;
}

export interface TopStudent {
  nombre: string;
  iniciales: string;
  vip: boolean;
  xp: number;
  racha: number;
}

export interface DashboardVm {
  estudiantes: number;
  nuevosMes: number;
  activos30: number;
  activosPct: number;
  inscripciones: number;
  completadas: number;
  tasaFinalizacion: number;
  xpTotal: number;
  xpPromedio: number;
  meses: MonthBar[];
  mesesMax: number;
  estadosInscripcion: Segment[];
  cuentas: Segment[];
  vip: number;
  cursos: CourseRow[];
  top: TopStudent[];
  catalogo: { categorias: number; cursos: number; lecciones: number; ejercicios: number; misionesActivas: number };
  actualizado: Date;
}

const MONTHS = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
const DAY_MS = 86_400_000;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  load(): Observable<DashboardVm> {
    return forkJoin({
      usuarios: this.get<UsuarioRow[]>('/usuarios'),
      cursos: this.get<CursoDTO[]>('/catalogo/cursos/todos'),
      categorias: this.get<CategoriaDTO[]>('/catalogo/categorias/todos'),
      lecciones: this.get<unknown[]>('/catalogo/lecciones'),
      ejercicios: this.get<unknown[]>('/catalogo/ejercicios'),
      misiones: this.get<MisionDTO[]>('/gamificacion/misiones/todos')
    }).pipe(
      switchMap((base) => {
        const progreso$ = base.cursos.length
          ? forkJoin(base.cursos.map((c) => this.get<ProgresoCursoRow[]>(`/progreso/cursos/curso/${c.idCurso}`)))
          : of([] as ProgresoCursoRow[][]);
        return progreso$.pipe(map((porCurso) => this.aggregate({ ...base, progreso: porCurso.flat() })));
      })
    );
  }

  private get<T>(path: string): Observable<T> {
    return this.http.get<T>(`${this.api}${path}`).pipe(catchError(() => of([] as unknown as T)));
  }

  private aggregate(raw: {
    usuarios: UsuarioRow[];
    cursos: CursoDTO[];
    categorias: CategoriaDTO[];
    lecciones: unknown[];
    ejercicios: unknown[];
    misiones: MisionDTO[];
    progreso: ProgresoCursoRow[];
  }): DashboardVm {
    const now = new Date();
    const estudiantes = raw.usuarios.filter((u) => this.isEstudiante(u.nombreRol));
    const idsEstudiantes = new Set(estudiantes.map((u) => u.idUsuario));
    const progreso = raw.progreso.filter((p) => idsEstudiantes.has(p.idUsuario));

    const meses = this.registrosPorMes(estudiantes, now);
    const mesesMax = Math.max(1, ...meses.map((m) => m.value));
    meses.forEach((m) => (m.percent = Math.round((m.value / mesesMax) * 100)));

    const hace30 = now.getTime() - 30 * DAY_MS;
    const activos = new Set(
      progreso
        .filter((p) => p.fechaUltimaActividad && new Date(p.fechaUltimaActividad).getTime() >= hace30)
        .map((p) => p.idUsuario)
    );

    const completadas = progreso.filter((p) => p.estado === 'COMPLETADO').length;
    const xpTotal = estudiantes.reduce((sum, u) => sum + (u.xpTotal || 0), 0);

    const estadosInscripcion = this.segments(progreso.map((p) => p.estado), [
      ['COMPLETADO', 'Completados'],
      ['EN_CURSO', 'En curso'],
      ['ABANDONADO', 'Abandonados']
    ]);
    const cuentas = this.segments(estudiantes.map((u) => (u.nombreEstado || 'ACTIVO').toUpperCase()), [
      ['ACTIVO', 'Activas'],
      ['INACTIVO', 'Inactivas'],
      ['BLOQUEADO', 'Bloqueadas'],
      ['SUSPENDIDO', 'Suspendidas']
    ]).filter((s) => s.value > 0);

    const catName = new Map(raw.categorias.map((c) => [c.idCategoria, c.nombre]));
    const cursos: CourseRow[] = raw.cursos
      .map((c) => {
        const delCurso = progreso.filter((p) => p.idCurso === c.idCurso);
        const done = delCurso.filter((p) => p.estado === 'COMPLETADO').length;
        return {
          nombre: c.nombre,
          categoria: catName.get(c.idCategoria) || '—',
          inscritos: delCurso.length,
          completados: done,
          tasa: delCurso.length ? Math.round((done / delCurso.length) * 100) : 0
        };
      })
      .sort((a, b) => b.inscritos - a.inscritos);

    const top: TopStudent[] = [...estudiantes]
      .sort((a, b) => (b.xpTotal || 0) - (a.xpTotal || 0))
      .slice(0, 5)
      .map((u) => {
        const nombre = [u.nombres, (u.apellidos || '').split(' ')[0]].filter(Boolean).join(' ') || u.email;
        return {
          nombre,
          iniciales: this.iniciales(u),
          vip: (u.nombreRol || '').toUpperCase().includes('VIP'),
          xp: u.xpTotal || 0,
          racha: u.rachaActual || 0
        };
      });

    return {
      estudiantes: estudiantes.length,
      nuevosMes: meses[meses.length - 1]?.value || 0,
      activos30: activos.size,
      activosPct: estudiantes.length ? Math.round((activos.size / estudiantes.length) * 100) : 0,
      inscripciones: progreso.length,
      completadas,
      tasaFinalizacion: progreso.length ? Math.round((completadas / progreso.length) * 100) : 0,
      xpTotal,
      xpPromedio: estudiantes.length ? Math.round(xpTotal / estudiantes.length) : 0,
      meses,
      mesesMax,
      estadosInscripcion,
      cuentas,
      vip: estudiantes.filter((u) => (u.nombreRol || '').toUpperCase().includes('VIP')).length,
      cursos,
      top,
      catalogo: {
        categorias: raw.categorias.length,
        cursos: raw.cursos.length,
        lecciones: raw.lecciones.length,
        ejercicios: raw.ejercicios.length,
        misionesActivas: raw.misiones.filter((m) => m.activa).length
      },
      actualizado: now
    };
  }

  private registrosPorMes(usuarios: UsuarioRow[], now: Date): MonthBar[] {
    const buckets: MonthBar[] = [];
    for (let i = 11; i >= 0; i--) {
      const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
      buckets.push({ label: MONTHS[d.getMonth()], value: 0, percent: 0, current: i === 0 });
    }
    const start = new Date(now.getFullYear(), now.getMonth() - 11, 1);
    for (const u of usuarios) {
      const date = u.fechaRegistro ? new Date(u.fechaRegistro) : null;
      if (!date || Number.isNaN(date.getTime()) || date < start) {
        continue;
      }
      const index = (date.getFullYear() - start.getFullYear()) * 12 + date.getMonth() - start.getMonth();
      if (index >= 0 && index < 12) {
        buckets[index].value += 1;
      }
    }
    return buckets;
  }

  private segments(values: string[], defs: [string, string][]): Segment[] {
    const total = values.length || 1;
    return defs.map(([key, label]) => {
      const value = values.filter((v) => v === key).length;
      return { key, label, value, percent: Math.round((value / total) * 100) };
    });
  }

  private iniciales(u: UsuarioRow): string {
    const a = (u.nombres || '').trim()[0] || '';
    const b = (u.apellidos || '').trim()[0] || '';
    return (a + b).toUpperCase() || (u.email || '?')[0].toUpperCase();
  }

  private isEstudiante(rol?: string): boolean {
    const value = (rol || '').toUpperCase();
    return value.includes('ALUMNO') || value.includes('VIP') || value.includes('ESTUDIANTE');
  }
}
