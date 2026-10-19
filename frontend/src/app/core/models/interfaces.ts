export interface LoginResponse {
  token: string;
  username: string;
  rol: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface DashboardDTO {
  totalAlumnos: number;
  totalCursos: number;
  totalLecciones: number;
  totalEjercicios: number;
  alumnosPorCurso: Array<{ curso: string; alumnos: number }>;
  actividadReciente: Array<{
    tipo: string;
    titulo: string;
    descripcion: string;
    tiempoRelativo: string;
  }>;
}

export interface TableColumn {
  key: string;
  header: string;
  type?: 'text' | 'date' | 'boolean' | 'object';
  objectKey?: string;
}

export interface CategoriaDTO {
  idCategoria: number;
  nombre: string;
}

export interface CursoDTO {
  idCurso: number;
  idCategoria: number;
  nombre: string;
  descripcion: string;
  icono: string;
  xpRequerido: number;
  xpRecompensa: number;
}

export interface RolDTO {
  idRol: number;
  nombre: string;
}

export interface UsuarioDTO {
  idUsuario: number;
  nombres: string;
  apellidos: string;
  idRol: number;
  nombreRol: string;
  email: string;
  fechaRegistro: string;
  xpTotal?: number;
  rachaActual?: number;
}

export interface MisionDTO {
  idMision?: number;
  nombre: string;
  descripcion?: string;
  tipo: string;
  meta: number;
  xpRecompensa: number;
  fechaInicio?: string;
  fechaFin?: string;
  activa: boolean;
}
