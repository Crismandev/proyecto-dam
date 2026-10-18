import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CategoriasService } from '../../core/services/categoria.service';

@Component({
  selector: 'app-categorias-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './categorias-list.component.html',
  styleUrls: ['./categorias-list.component.scss']
})
export class CategoriasListComponent implements OnInit {
  categorias = signal<any[]>([]);

  constructor(private categoriasService: CategoriasService) {}

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.categoriasService.getAll().subscribe({
      next: (data) => this.categorias.set(data),
      error: (err) => console.error(err)
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro?')) {
      this.categoriasService.delete(id).subscribe({
        next: () => this.cargarDatos(),
        error: (err) => console.error(err)
      });
    }
  }
}