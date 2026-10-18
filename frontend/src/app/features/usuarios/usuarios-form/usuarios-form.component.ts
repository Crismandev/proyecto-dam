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
    idRol: [null as number | null],
    nombres: ['', [Validators.required, Validators.maxLength(100)]],
    apellidos: ['', [Validators.maxLength(100)]],
    correo: ['', [Validators.email, Validators.maxLength(150)]],
    username: [''],
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
          idRol: usuario.idRol,
          nombres: usuario.nombres,
          apellidos: usuario.apellidos,
          correo: usuario.email
        });
      });
      this.form.controls.idRol.clearValidators();
      this.form.controls.correo.clearValidators();
      this.form.controls.username.clearValidators();
      this.form.controls.contrasena.clearValidators();
    } else {
      this.form.controls.idRol.setValidators([Validators.required]);
      this.form.controls.correo.setValidators([Validators.required, Validators.email, Validators.maxLength(150)]);
      this.form.controls.username.setValidators([Validators.required, Validators.minLength(3), Validators.maxLength(100)]);
      this.form.controls.contrasena.setValidators([Validators.required, Validators.minLength(8)]);
    }
    Object.values(this.form.controls).forEach(control => control.updateValueAndValidity());
  }

  loadRoles() {
    this.http.get<RolDTO[]>(`${environment.apiUrl}/seguridad/roles`).subscribe(res => {
      this.roles.set(res);
    });
  }

  onSubmit() {
    if (this.form.valid) {
      if (this.isEditing && this.usuarioId) {
        const { nombres, apellidos } = this.form.getRawValue();
        this.usuarioService.update(this.usuarioId, { nombres, apellidos }).subscribe(() => this.goBack());
      } else {
        const raw = this.form.getRawValue();
        const dto = { idRol: raw.idRol, nombres: raw.nombres, apellidos: raw.apellidos, email: raw.correo, username: raw.username, password: raw.contrasena };
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
