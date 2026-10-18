import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../core/services/crud.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LeccionesService extends CrudService<any, any> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiUrl}/lecciones`);
  }
}