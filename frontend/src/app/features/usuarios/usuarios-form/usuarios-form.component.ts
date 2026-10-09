import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { RolDTO } from '../../../core/models/interfaces';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-usuarios-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor],
  templateUrl: './usuarios-form.component.html'
})
export class UsuariosFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);
  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isEditing = false;
  usuarioId?: number;
  roles = signal<RolDTO[]>([]);

  form = this.fb.nonNullable.group({
    idRol: [null as number | null, [Validators.required]],
    nombres: ['', [Validators.required, Validators.maxLength(100)]],
    apellidos: ['', [Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
    contrasena: ['']
  });

  ngOnInit() {
    this.loadRoles();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditing = true;
      this.usuarioId = Number(id);
      this.usuarioService.getById(this.usuarioId).subscribe((usuario: any) => {
        this.form.patchValue({
          idRol: usuario.rol.idRol,
          nombres: usuario.nombres,
          apellidos: usuario.apellidos,
          correo: usuario.correo
        });
      });
    } else {
      this.form.controls.contrasena.setValidators([Validators.required, Validators.minLength(8)]);
      this.form.controls.contrasena.updateValueAndValidity();
    }
  }

  loadRoles() {
    this.http.get<any>(`${environment.apiUrl}/admin/roles?size=50`).subscribe(res => {
      this.roles.set(res.content);
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const dto = this.form.getRawValue();
      if (this.isEditing && this.usuarioId) {
        // Remove password if empty
        if (!dto.contrasena) {
          delete (dto as any).contrasena;
        }
        this.usuarioService.update(this.usuarioId, dto).subscribe(() => this.goBack());
      } else {
        this.usuarioService.create(dto).subscribe(() => this.goBack());
      }
    } else {
      this.form.markAllAsTouched();
    }
  }

  goBack() {
    this.router.navigate(['/admin/usuarios']);
  }
}
