import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../../../core/services/crud.service';
import { MisionDTO } from '../../../core/models/interfaces';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MisionService extends CrudService<MisionDTO, MisionDTO> {
  constructor() {
    const http = inject(HttpClient);
    super(http, `${environment.apiUrl}/misiones`);
  }
}