import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../core/services/crud.service';
import { environment } from '../../../environments/environment';
import { CategoriaDTO } from '../../core/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class CategoriasService extends CrudService<CategoriaDTO, CategoriaDTO> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiUrl}/categorias`);
  }
}