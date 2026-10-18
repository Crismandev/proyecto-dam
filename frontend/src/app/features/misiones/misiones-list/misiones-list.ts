import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MisionService } from '../services/mision.service';
import { MisionDTO } from '../../../core/models/interfaces';

@Component({
  selector: 'app-misiones-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './misiones-list.html',
  styleUrls: ['./misiones-list.scss']
})
export class MisionesListComponent implements OnInit {
  private misionService = inject(MisionService);
  
  misiones = signal<MisionDTO[]>([]);
  page = signal<number>(0);
  totalPages = signal<number>(0);

  ngOnInit(): void {
    this.cargarMisiones();
  }

  cargarMisiones(page: number = 0): void {
    this.misionService.getAll().subscribe({
      next: (res) => {
        const start = page * 10;
        this.misiones.set(res.slice(start, start + 10));
        this.page.set(page);
        this.totalPages.set(Math.ceil(res.length / 10));
      },
      error: (err) => console.error('Error al cargar misiones', err)
    });
  }

  eliminar(id?: number): void {
    if (id && confirm('¿Deseas eliminar esta misión?')) {
      this.misionService.delete(id).subscribe(() => this.cargarMisiones(this.page()));
    }
  }
}
