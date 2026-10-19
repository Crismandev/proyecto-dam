import { AfterViewInit, Component, ElementRef, OnDestroy, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { gsap } from 'gsap';
import { AuthService } from '../../../core/services/auth.service';
import { SceneOrbComponent } from '../../../shared/components/scene-orb/scene-orb.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, SceneOrbComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements AfterViewInit, OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private host = inject(ElementRef);
  private ctx?: gsap.Context;

  loginForm = this.fb.nonNullable.group({
    correo: ['', [Validators.required]],
    contrasena: ['', [Validators.required]]
  });

  errorMsg = signal<string>('');
  isLoading = signal<boolean>(false);

  ngAfterViewInit(): void {
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }
    this.ctx = gsap.context(() => {
      const tl = gsap.timeline({ defaults: { duration: 0.6, ease: 'power2.out' } });
      tl.from('.brand-container > *', { y: 22, autoAlpha: 0, stagger: 0.08 })
        .from('.login-card', { x: 36, autoAlpha: 0 }, 0.1);
    }, this.host.nativeElement);
  }

  ngOnDestroy(): void {
    this.ctx?.revert();
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading.set(true);
      this.errorMsg.set('');
      const { correo: username, contrasena: password } = this.loginForm.getRawValue();

      this.authService.login(username, password).subscribe({
        next: (res) => {
          this.isLoading.set(false);
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
