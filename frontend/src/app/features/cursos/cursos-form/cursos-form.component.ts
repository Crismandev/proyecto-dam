import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CursoService } from '../services/curso.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { CategoriaDTO } from '../../../core/models/interfaces';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-cursos-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor],
  templateUrl: './cursos-form.component.html'
})
export class CursosFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private cursoService = inject(CursoService);
  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isEditing = false;
  cursoId?: number;
  categorias = signal<CategoriaDTO[]>([]);

  form = this.fb.nonNullable.group({
    idCategoria: [null as number | null, [Validators.required]],
    nombreCurso: ['', [Validators.required, Validators.maxLength(150)]],
    descripcion: [''],
    icono: ['', [Validators.maxLength(100)]],
    dificultad: ['PRINCIPIANTE', [Validators.required]],
    xpRequerido: [0, [Validators.required, Validators.min(0)]],
    xpRecompensa: [0, [Validators.required, Validators.min(0)]]
  });

  ngOnInit() {
    this.loadCategorias();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditing = true;
      this.cursoId = Number(id);
      this.cursoService.getById(this.cursoId).subscribe(curso => {
        this.form.patchValue({
          idCategoria: curso.idCategoria,
          nombreCurso: curso.nombreCurso,
          descripcion: curso.descripcion,
          icono: curso.icono,
          dificultad: curso.dificultad,
          xpRequerido: curso.xpRequerido,
          xpRecompensa: curso.xpRecompensa
        });
      });
    }
  }

  loadCategorias() {
    // Unpaginated simplified fetch for dropdown
    this.http.get<any>(`${environment.apiUrl}/admin/categorias?size=100`).subscribe(res => {
      this.categorias.set(res.content);
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const dto = this.form.getRawValue();
      if (this.isEditing && this.cursoId) {
        this.cursoService.update(this.cursoId, dto).subscribe(() => this.goBack());
      } else {
        this.cursoService.create(dto).subscribe(() => this.goBack());
      }
    } else {
      this.form.markAllAsTouched();
    }
  }

  goBack() {
    this.router.navigate(['/admin/cursos']);
  }
}
