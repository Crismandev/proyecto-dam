import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MisionService } from '../services/mision.service';

@Component({
  selector: 'app-misiones-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './misiones-form.html',
  styleUrls: ['./misiones-form.scss']
})
export class MisionesFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private misionService = inject(MisionService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  isEditing = false;
  misionId?: number;

  form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    descripcion: [''],
    tipo: ['DIARIA', [Validators.required]],
    meta: [1, [Validators.required, Validators.min(1)]],
    xpRecompensa: [50, [Validators.required, Validators.min(0)]],
    fechaInicio: [''],
    fechaFin: [''],
    activa: [true]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditing = true;
      this.misionId = Number(idParam);
      this.misionService.getById(this.misionId).subscribe((data) => {
        this.form.patchValue(data);
      });
    }
  }

  guardar(): void {
    if (this.form.invalid) return;

    const data = this.form.getRawValue();

    if (this.isEditing && this.misionId) {
      this.misionService.update(this.misionId, data).subscribe(() => {
        this.router.navigate(['/admin/misiones']);
      });
    } else {
      this.misionService.create(data).subscribe(() => {
        this.router.navigate(['/admin/misiones']);
      });
    }
  }
}