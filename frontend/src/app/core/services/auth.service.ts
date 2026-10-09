import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { tap } from 'rxjs';
import { LoginResponse } from '../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // Signals for state management
  currentUser = signal<{correo: string, rol: string} | null>(this.getUserFromStorage());
  isAuthenticated = signal<boolean>(!!this.getToken());

  constructor(private http: HttpClient, private router: Router) { }

  login(correo: string, contrasena: string) {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { correo, contrasena }).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('techmentor_token', response.token);
          const userObj = { correo: response.correo, rol: response.rol };
          localStorage.setItem('techmentor_user', JSON.stringify(userObj));
          
          this.currentUser.set(userObj);
          this.isAuthenticated.set(true);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('techmentor_token');
    localStorage.removeItem('techmentor_user');
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('techmentor_token');
  }

  private getUserFromStorage() {
    const user = localStorage.getItem('techmentor_user');
    return user ? JSON.parse(user) : null;
  }

  hasRole(role: string): boolean {
    const user = this.currentUser();
    if (!user || !user.rol) return false;
    if (user.rol === role) return true;
    if (role === 'ADMIN' && (user.rol === 'ADMINISTRADOR' || user.rol === 'ADMIN')) return true;
    return false;
  }
}
