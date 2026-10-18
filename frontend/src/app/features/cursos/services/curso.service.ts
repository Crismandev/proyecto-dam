import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../../core/services/crud.service';
import { CursoDTO } from '../../../core/models/interfaces';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CursoService extends CrudService<CursoDTO, any> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiUrl}/catalogo/cursos`);
  }

  override getAll(): Observable<CursoDTO[]> {
    return this.http.get<CursoDTO[]>(`${this.url}/todos`);
  }
}
