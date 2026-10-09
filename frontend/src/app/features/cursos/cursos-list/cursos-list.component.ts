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
    { key: 'nombreCurso', header: 'Nombre' },
    { key: 'dificultad', header: 'Dificultad' },
    { key: 'xpRequerido', header: 'XP Requerido' },
    { key: 'xpRecompensa', header: 'XP Recompensa' }
  ];

  ngOnInit() {
    this.loadData();
  }

  loadData(page: number = 0) {
    this.cursoService.getAll(page, 10, this.currentSearch()).subscribe(res => {
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
    this.router.navigate(['/admin/cursos/nuevo']);
  }

  onEdit(row: CursoDTO) {
    this.router.navigate(['/admin/cursos', row.idCurso, 'editar']);
  }

  onDelete(row: CursoDTO) {
    if (confirm(`¿Seguro que deseas eliminar el curso: ${row.nombreCurso}?`)) {
      this.cursoService.delete(row.idCurso).subscribe(() => {
        this.loadData(this.currentPage());
      });
    }
  }
}
