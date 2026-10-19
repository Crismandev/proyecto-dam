import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, forkJoin, map, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CategoriaDTO, CursoDTO, MisionDTO, UsuarioDTO } from '../../core/models/interfaces';

export interface MonthPoint {
  label: string;
  value: number;
}

export interface NamedBar {
  name: string;
  value: number;
  percent: number;
}

export interface DonutSlice {
  name: string;
  value: number;
  percent: number;
  color: string;
  dasharray: string;
  dashoffset: number;
}

export interface SatisfactionRow {
  label: string;
  stars: number;
  percent: number;
}

export interface DashboardVm {
  totalAlumnos: number;
  totalCursos: number;
  totalLecciones: number;
  totalEjercicios: number;
  totalXp: number;
  misionesActivas: number;
  totalMisiones: number;
  achievement: number;
  monthSeries: MonthPoint[];
  monthMax: number;
  categoryBars: NamedBar[];
  donut: DonutSlice[];
  satisfaction: SatisfactionRow[];
}

const MONTHS = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
const DONUT_COLORS = ['#1E1E38', '#FF6B35', '#FFB347', '#C9D6FF', '#7C4DFF'];

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  load(): Observable<DashboardVm> {
    return forkJoin({
      usuarios: this.get<UsuarioDTO[]>('/usuarios'),
      cursos: this.get<CursoDTO[]>('/catalogo/cursos/todos'),
      categorias: this.get<CategoriaDTO[]>('/catalogo/categorias/todos'),
      lecciones: this.get<unknown[]>('/catalogo/lecciones'),
      ejercicios: this.get<unknown[]>('/catalogo/ejercicios'),
      misiones: this.get<MisionDTO[]>('/gamificacion/misiones/todos')
    }).pipe(map((raw) => this.aggregate(raw)));
  }

  private get<T>(path: string): Observable<T> {
    return this.http.get<T>(`${this.api}${path}`).pipe(catchError(() => of([] as unknown as T)));
  }

  private aggregate(raw: {
    usuarios: UsuarioDTO[];
    cursos: CursoDTO[];
    categorias: CategoriaDTO[];
    lecciones: unknown[];
    ejercicios: unknown[];
    misiones: MisionDTO[];
  }): DashboardVm {
    const alumnos = raw.usuarios.filter((u) => this.isAlumno(u.nombreRol));
    const counts = new Array(12).fill(0) as number[];
    const nowYear = new Date().getFullYear();
    for (const user of raw.usuarios) {
      if (!user.fechaRegistro) {
        continue;
      }
      const date = new Date(user.fechaRegistro);
      if (Number.isNaN(date.getTime())) {
        continue;
      }
      if (date.getFullYear() === nowYear) {
        counts[date.getMonth()] += 1;
      }
    }
    let running = 0;
    const monthSeries = MONTHS.map((label, i) => {
      running += counts[i];
      return { label, value: running };
    });
    const monthMax = Math.max(1, running);

    const byCat = new Map<number, number>();
    for (const curso of raw.cursos) {
      byCat.set(curso.idCategoria, (byCat.get(curso.idCategoria) || 0) + 1);
    }
    const catName = new Map(raw.categorias.map((c) => [c.idCategoria, c.nombre]));
    const barsRaw = [...byCat.entries()]
      .map(([id, value]) => ({ name: catName.get(id) || `Ruta ${id}`, value }))
      .sort((a, b) => b.value - a.value)
      .slice(0, 4);
    const barMax = Math.max(1, ...barsRaw.map((b) => b.value));
    const categoryBars = barsRaw.map((b) => ({ ...b, percent: Math.round((b.value / barMax) * 100) }));

    const donutSource = (barsRaw.length ? barsRaw : raw.categorias.slice(0, 5).map((c) => ({ name: c.nombre, value: 1 })));
    const donutTotal = donutSource.reduce((sum, item) => sum + item.value, 0) || 1;
    let offset = 25;
    const donut: DonutSlice[] = donutSource.map((item, i) => {
      const percent = (item.value / donutTotal) * 100;
      const slice: DonutSlice = {
        name: item.name,
        value: item.value,
        percent: Math.round(percent),
        color: DONUT_COLORS[i % DONUT_COLORS.length],
        dasharray: `${percent} ${100 - percent}`,
        dashoffset: offset
      };
      offset -= percent;
      return slice;
    });

    const buckets = [
      { label: 'Muy alta', min: 7, stars: 5 },
      { label: 'Alta', min: 3, stars: 4 },
      { label: 'Media', min: 1, stars: 3 },
      { label: 'Baja', min: 0, stars: 1 }
    ];
    const studentPool = alumnos.length ? alumnos : raw.usuarios;
    const satisfaction = buckets.map((bucket, index) => {
      const nextMin = index === 0 ? Infinity : buckets[index - 1].min;
      const count = studentPool.filter((u) => {
        const streak = u.rachaActual ?? 0;
        return streak >= bucket.min && streak < nextMin;
      }).length;
      const percent = studentPool.length ? Math.round((count / studentPool.length) * 100) : 0;
      return { label: bucket.label, stars: bucket.stars, percent };
    });

    const misionesActivas = raw.misiones.filter((m) => m.activa).length;
    const totalXp = raw.usuarios.reduce((sum, u) => sum + (u.xpTotal || 0), 0);
    const achievement = raw.misiones.length
      ? Math.round((misionesActivas / raw.misiones.length) * 100)
      : 0;

    return {
      totalAlumnos: alumnos.length || raw.usuarios.length,
      totalCursos: raw.cursos.length,
      totalLecciones: raw.lecciones.length,
      totalEjercicios: raw.ejercicios.length,
      totalXp,
      misionesActivas,
      totalMisiones: raw.misiones.length,
      achievement,
      monthSeries,
      monthMax,
      categoryBars,
      donut,
      satisfaction
    };
  }

  private isAlumno(rol?: string): boolean {
    const value = (rol || '').toUpperCase();
    return value.includes('ALUMNO') || value.includes('VIP') || value.includes('ESTUDIANTE');
  }
}
