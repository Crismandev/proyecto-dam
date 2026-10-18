import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CursoService } from '../services/curso.service';
import { CursoDTO, TableColumn } from '../../../core/models/interfaces';
import { DataTableComponent } from '../../../shared/components/data-table/data-table.component';
import { SearchInputComponent } from '../../../shared/components/search-input/search-input.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-cursos-list',
  standalone: true,
  imports: [DataTableComponent, SearchInputComponent, NgIf],
  template: `
    <div class="page-header">
      <h1>Cursos</h1>
      <button class="btn btn-primary" (click)="goToCreate()">+ Nuevo Curso</button>
    </div>

    <div class="card">
      <div style="margin-bottom: 1.5rem;">
        <app-search-input placeholder="Buscar curso..." (search)="onSearch($event)"></app-search-input>
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
        (view)="onEdit($event)">
      </app-data-table>
    </div>
  `
})
export class CursosListComponent implements OnInit {
  private cursoService = inject(CursoService);
  private router = inject(Router);

  data = signal<CursoDTO[]>([]);
  totalElements = signal<number>(0);
  totalPages = signal<number>(0);
  currentPage = signal<number>(0);
  currentSearch = signal<string>('');

  columns: TableColumn[] = [
    { key: 'idCurso', header: 'ID' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'xpRequerido', header: 'XP Requerido' },
    { key: 'xpRecompensa', header: 'XP Recompensa' }
  ];

  ngOnInit() {
    this.loadData();
  }

  loadData(page: number = 0) {
    this.cursoService.getAll().subscribe(res => {
      const filtered = res.filter(item => (item.nombre || '').toLowerCase().includes(this.currentSearch().toLowerCase()));
      this.totalElements.set(filtered.length);
      this.totalPages.set(Math.ceil(filtered.length / 10));
      this.currentPage.set(page);
      this.data.set(filtered.slice(page * 10, page * 10 + 10));
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
    this.router.navigate(['/admin/cursos/nuevo']);
  }

  onEdit(row: CursoDTO) {
    this.router.navigate(['/admin/cursos', row.idCurso, 'editar']);
  }

  onDelete(row: CursoDTO) {
      if (confirm(`¿Seguro que deseas eliminar el curso: ${row.nombre}?`)) {
      this.cursoService.delete(row.idCurso).subscribe(() => {
        this.loadData(this.currentPage());
      });
    }
  }
}
