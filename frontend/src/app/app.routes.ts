import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { 
    path: 'login', 
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin',
    loadComponent: () => import('./layout/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    canActivate: [roleGuard],
    data: { role: 'ADMIN' },
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { 
        path: 'dashboard', 
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) 
      },
      { 
        path: 'usuarios', 
        loadComponent: () => import('./features/usuarios/usuarios-list/usuarios-list.component').then(m => m.UsuariosListComponent) 
      },
      { 
        path: 'usuarios/nuevo', 
        loadComponent: () => import('./features/usuarios/usuarios-form/usuarios-form.component').then(m => m.UsuariosFormComponent) 
      },
      { 
        path: 'usuarios/:id/editar', 
        loadComponent: () => import('./features/usuarios/usuarios-form/usuarios-form.component').then(m => m.UsuariosFormComponent) 
      },
      { 
        path: 'usuarios/:id/detalle', 
        loadComponent: () => import('./features/usuarios/usuarios-detail/usuarios-detail.component').then(m => m.UsuariosDetailComponent) 
      },
      { 
        path: 'categorias', 
        loadComponent: () => import('./features/categorias/categorias-list.component').then(m => m.CategoriasListComponent) 
      },
      { 
        path: 'categorias/nueva', 
        loadComponent: () => import('./features/categorias/categorias-form.component').then(m => m.CategoriasFormComponent) 
      },
      { 
        path: 'categorias/:id/editar', 
        loadComponent: () => import('./features/categorias/categorias-form.component').then(m => m.CategoriasFormComponent) 
      },
      { 
        path: 'cursos', 
        loadComponent: () => import('./features/cursos/cursos-list/cursos-list.component').then(m => m.CursosListComponent) 
      },
      { 
        path: 'cursos/nuevo', 
        loadComponent: () => import('./features/cursos/cursos-form/cursos-form.component').then(m => m.CursosFormComponent) 
      },
      { 
        path: 'cursos/:id/editar', 
        loadComponent: () => import('./features/cursos/cursos-form/cursos-form.component').then(m => m.CursosFormComponent) 
      },
      { 
        path: 'niveles', 
        loadComponent: () => import('./features/niveles/niveles-list.component').then(m => m.NivelesListComponent) 
      },
      { 
        path: 'niveles/nuevo', 
        loadComponent: () => import('./features/niveles/niveles-form.component').then(m => m.NivelesFormComponent) 
      },
      { 
        path: 'niveles/:id/editar', 
        loadComponent: () => import('./features/niveles/niveles-form.component').then(m => m.NivelesFormComponent) 
      },
      { 
        path: 'lecciones', 
        loadComponent: () => import('./features/lecciones/lecciones-list.component').then(m => m.LeccionesListComponent) 
      },
      { 
        path: 'lecciones/nueva', 
        loadComponent: () => import('./features/lecciones/lecciones-form.component').then(m => m.LeccionesFormComponent) 
      },
      { 
        path: 'lecciones/:id/editar', 
        loadComponent: () => import('./features/lecciones/lecciones-form.component').then(m => m.LeccionesFormComponent) 
      },
      { 
        path: 'ejercicios', 
        loadComponent: () => import('./features/ejercicios/ejercicios-list.component').then(m => m.EjerciciosListComponent) 
      },
      { 
        path: 'ejercicios/nuevo', 
        loadComponent: () => import('./features/ejercicios/ejercicios-form.component').then(m => m.EjerciciosFormComponent) 
      },
      { 
        path: 'ejercicios/:id/editar', 
        loadComponent: () => import('./features/ejercicios/ejercicios-form.component').then(m => m.EjerciciosFormComponent) 
      },
      { 
        path: 'progreso', 
        loadComponent: () => import('./features/progreso/progreso-list.component').then(m => m.ProgresoListComponent) 
      },
      { 
        path: 'roles', 
        loadComponent: () => import('./features/roles/roles-list.component').then(m => m.RolesListComponent) 
      },
      { 
        path: 'roles/nuevo', 
        loadComponent: () => import('./features/roles/roles-form.component').then(m => m.RolesFormComponent) 
      },
      { 
        path: 'roles/:id/editar', 
        loadComponent: () => import('./features/roles/roles-form.component').then(m => m.RolesFormComponent) 
      }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
