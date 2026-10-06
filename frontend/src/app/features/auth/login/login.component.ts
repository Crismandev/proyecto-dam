import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm = this.fb.nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', [Validators.required]]
  });

  errorMsg = signal<string>('');
  isLoading = signal<boolean>(false);

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading.set(true);
      this.errorMsg.set('');
      const { correo, contrasena } = this.loginForm.getRawValue();
      
      this.authService.login(correo, contrasena).subscribe({
        next: (res) => {
          this.isLoading.set(false);
          // Redirect to admin if role is ADMIN or ADMINISTRADOR
          if (res.rol === 'ADMIN' || res.rol === 'ADMINISTRADOR') {
            this.router.navigate(['/admin/dashboard']);
          } else {
            this.errorMsg.set('Solo administradores pueden acceder al portal.');
            this.authService.logout();
          }
        },
        error: (err) => {
          this.isLoading.set(false);
          if (err.status === 401 || err.status === 400) {
            this.errorMsg.set('Credenciales inválidas.');
          } else {
            this.errorMsg.set('Error en el servidor.');
          }
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}
