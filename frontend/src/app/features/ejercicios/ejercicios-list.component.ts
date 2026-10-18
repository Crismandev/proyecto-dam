import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EjerciciosService } from '../../core/services/ejercicios.service';

@Component({
  selector: 'app-ejercicios-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ejercicios-list.component.html',
  styleUrls: ['./ejercicios-list.component.scss']
})
export class EjerciciosListComponent implements OnInit {
  ejercicios = signal<any[]>([]);

  constructor(private ejerciciosService: EjerciciosService) {}

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.ejerciciosService.getAll().subscribe({
      next: (data) => this.ejercicios.set(data),
      error: (err) => console.error(err)
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro?')) {
      this.ejerciciosService.delete(id).subscribe({
        next: () => this.cargarDatos(),
        error: (err) => console.error(err)
      });
    }
  }
}