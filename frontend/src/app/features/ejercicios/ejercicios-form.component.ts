import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EjerciciosService } from '../../core/services/ejercicios.service';

@Component({
  selector: 'app-ejercicios-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ejercicios-form.component.html',
  styleUrls: ['./ejercicios-form.component.scss']
})
export class EjerciciosFormComponent {
  ejercicioForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private ejerciciosService: EjerciciosService,
    private router: Router
  ) {
    this.ejercicioForm = this.fb.group({
      enunciado: ['', [Validators.required, Validators.minLength(5)]],
      tipo: ['', Validators.required]
    });
  }

  guardar() {
    if (this.ejercicioForm.invalid) {
      this.ejercicioForm.markAllAsTouched();
      return;
    }

    this.ejerciciosService.create(this.ejercicioForm.value).subscribe({
      next: () => {
        this.router.navigate(['../'], { relativeTo: this.router.routerState.root.firstChild });
      },
      error: (err) => console.error(err)
    });
  }
}