import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../../core/services/crud.service';
import { UsuarioDTO } from '../../../core/models/interfaces';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService extends CrudService<UsuarioDTO, any> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiUrl}/admin/usuarios`);
  }

  getDetalleAlumno(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}/detalle`);
  }
}
