import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-progreso-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './progreso-list.component.html',
  styleUrl: './progreso-list.component.scss'
})
export class ProgresoListComponent implements OnInit {
  private http = inject(HttpClient);
  usuarios = signal<any[]>([]);
  cursos = signal<any[]>([]);
  lecciones = signal<any[]>([]);
  usuarioId: number | null = null;

  ngOnInit() {
    this.http.get<any[]>(`${environment.apiUrl}/usuarios`).subscribe(users => this.usuarios.set(users));
  }

  cargarProgreso() {
    if (!this.usuarioId) return;
    this.http.get<any[]>(`${environment.apiUrl}/progreso/cursos/usuario/${this.usuarioId}`).subscribe(items => this.cursos.set(items));
    this.http.get<any[]>(`${environment.apiUrl}/progreso/lecciones/usuario/${this.usuarioId}`).subscribe(items => this.lecciones.set(items));
  }
}
