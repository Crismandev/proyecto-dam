import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { UsuarioDTO, TableColumn } from '../../../core/models/interfaces';
import { DataTableComponent } from '../../../shared/components/data-table/data-table.component';
import { SearchInputComponent } from '../../../shared/components/search-input/search-input.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [DataTableComponent, SearchInputComponent, NgIf],
  template: `
    <div class="page-header">
      <h1>Usuarios</h1>
      <button class="btn btn-primary" (click)="goToCreate()">+ Nuevo Usuario</button>
    </div>

    <div class="card">
      <div style="margin-bottom: 1.5rem;">
        <app-search-input placeholder="Buscar por nombre o correo..." (search)="onSearch($event)"></app-search-input>
      </div>

      <app-data-table 
        [data]="data()" 
        [columns]="columns" 
        [totalElements]="totalElements()"
        [totalPages]="totalPages()"
        [currentPage]="currentPage()"
        (pageChange)="onPageChange($event)"
        (edit)="onEdit($event)"
        (delete)="onDelete($event)"
        (view)="onView($event)">
      </app-data-table>
    </div>
  `
})
export class UsuariosListComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  data = signal<UsuarioDTO[]>([]);
  totalElements = signal<number>(0);
  totalPages = signal<number>(0);
  currentPage = signal<number>(0);
  currentSearch = signal<string>('');

  columns: TableColumn[] = [
    { key: 'idUsuario', header: 'ID' },
    { key: 'nombres', header: 'Nombres' },
    { key: 'apellidos', header: 'Apellidos' },
    { key: 'correo', header: 'Correo' },
    { key: 'rol', header: 'Rol', type: 'object', objectKey: 'nombreRol' },
    { key: 'fechaRegistro', header: 'Registro', type: 'date' }
  ];

  ngOnInit() {
    this.loadData();
  }

  loadData(page: number = 0) {
    this.usuarioService.getAll(page, 10, this.currentSearch()).subscribe(res => {
      this.data.set(res.content);
      this.totalElements.set(res.totalElements);
      this.totalPages.set(res.totalPages);
      this.currentPage.set(res.number);
    });
  }

  onSearch(term: string) {
    this.currentSearch.set(term);
    this.loadData(0);
  }

  onPageChange(page: number) {
    this.loadData(page);
  }

  goToCreate() {
    this.router.navigate(['/admin/usuarios/nuevo']);
  }

  onView(row: UsuarioDTO) {
    if (row.rol.nombreRol === 'ESTUDIANTE') {
      this.router.navigate(['/admin/usuarios', row.idUsuario, 'detalle']);
    } else {
      this.onEdit(row);
    }
  }

  onEdit(row: UsuarioDTO) {
    this.router.navigate(['/admin/usuarios', row.idUsuario, 'editar']);
  }

  onDelete(row: UsuarioDTO) {
    if (confirm(`¿Seguro que deseas eliminar al usuario: ${row.correo}?`)) {
      this.usuarioService.delete(row.idUsuario).subscribe(() => {
        this.loadData(this.currentPage());
      });
    }
  }
}
