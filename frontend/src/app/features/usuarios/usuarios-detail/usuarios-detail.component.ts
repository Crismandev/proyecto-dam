import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { NgIf, NgFor, DatePipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-usuarios-detail',
  standalone: true,
  imports: [NgIf, NgFor, DatePipe, NgClass],
  templateUrl: './usuarios-detail.component.html',
  styleUrls: ['./usuarios-detail.component.scss']
})
export class UsuariosDetailComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  alumno = signal<any>(null);
  activeTab = signal<string>('progreso');

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.usuarioService.getDetalleAlumno(Number(id)).subscribe(res => {
        this.alumno.set(res);
      });
    }
  }

  goBack() {
    this.router.navigate(['/admin/usuarios']);
  }
}
