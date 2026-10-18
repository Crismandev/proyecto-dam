import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LeccionesService } from '../../core/services/lecciones.service';

@Component({
  selector: 'app-lecciones-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './lecciones-form.component.html',
  styleUrls: ['./lecciones-form.component.scss']
})
export class LeccionesFormComponent {
  leccionForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private leccionesService: LeccionesService,
    private router: Router
  ) {
    this.leccionForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(3)]],
      contenido: ['', Validators.required]
    });
  }

  guardar() {
    if (this.leccionForm.invalid) {
      this.leccionForm.markAllAsTouched();
      return;
    }

    this.leccionesService.create(this.leccionForm.value).subscribe({
      next: () => {
        this.router.navigate(['../'], { relativeTo: this.router.routerState.root.firstChild });
      },
      error: (err) => console.error(err)
    });
  }
}