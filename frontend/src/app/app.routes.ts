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
      { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
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
      { path: 'categorias', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'categorias' } },
      { path: 'categorias/nuevo', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'categorias' } },
      { path: 'categorias/:id/editar', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'categorias' } },
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
      { path: 'niveles', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'niveles' } },
      { path: 'niveles/nuevo', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'niveles' } },
      { path: 'niveles/:id/editar', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'niveles' } },
      { path: 'lecciones', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'lecciones' } },
      { path: 'lecciones/nuevo', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'lecciones' } },
      { path: 'lecciones/:id/editar', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'lecciones' } },
      { path: 'ejercicios', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'ejercicios' } },
      { path: 'ejercicios/nuevo', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'ejercicios' } },
      { path: 'ejercicios/:id/editar', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'ejercicios' } },
      { path: 'progreso', loadComponent: () => import('./features/progreso/progreso-list.component').then(m => m.ProgresoListComponent) },
      { path: 'roles', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'roles' } },
      { path: 'roles/nuevo', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'roles' } },
      { path: 'roles/:id/editar', loadComponent: () => import('./shared/components/backend-crud/backend-crud.component').then(m => m.BackendCrudComponent), data: { resource: 'roles' } }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
