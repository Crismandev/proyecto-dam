export interface LoginResponse {
  token: string;
  correo: string;
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
  nombreCategoria: string;
}

export interface CursoDTO {
  idCurso: number;
  idCategoria: number;
  nombreCurso: string;
  descripcion: string;
  icono: string;
  dificultad: string;
  xpRequerido: number;
  xpRecompensa: number;
}

export interface RolDTO {
  idRol: number;
  nombreRol: string;
}

export interface UsuarioDTO {
  idUsuario: number;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: RolDTO;
  fechaRegistro: string;
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
