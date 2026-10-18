import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LeccionesService } from '../../core/services/lecciones.service';

@Component({
  selector: 'app-lecciones-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './lecciones-list.component.html',
  styleUrls: ['./lecciones-list.component.scss']
})
export class LeccionesListComponent implements OnInit {
  lecciones = signal<any[]>([]);

  constructor(private leccionesService: LeccionesService) {}

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.leccionesService.getAll().subscribe({
      next: (data) => this.lecciones.set(data),
      error: (err) => console.error(err)
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro?')) {
      this.leccionesService.delete(id).subscribe({
        next: () => this.cargarDatos(),
        error: (err) => console.error(err)
      });
    }
  }
}