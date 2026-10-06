import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../../core/services/crud.service';
import { CursoDTO } from '../../../core/models/interfaces';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CursoService extends CrudService<CursoDTO, any> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiUrl}/admin/cursos`);
  }
}
