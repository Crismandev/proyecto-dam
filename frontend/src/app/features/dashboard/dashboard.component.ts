import { Component, inject, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { DashboardDTO } from '../../core/models/interfaces';
import { DatePipe, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, NgFor, NgIf],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private http = inject(HttpClient);
  data = signal<DashboardDTO | null>(null);

  ngOnInit() {
    this.http.get<DashboardDTO>(`${environment.apiUrl}/admin/dashboard`).subscribe(res => {
      this.data.set(res);
    });
  }

  getBarHeight(value: number, max: number): string {
    if (max === 0) return '0%';
    return `${(value / max) * 100}%`;
  }

  getMaxAlumnos(): number {
    const d = this.data();
    if (!d || !d.alumnosPorCurso || d.alumnosPorCurso.length === 0) return 1;
    return Math.max(...d.alumnosPorCurso.map((a: any) => a.alumnos));
  }
}
