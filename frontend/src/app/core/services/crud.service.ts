import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../models/interfaces';

export class CrudService<T, SaveDTO> {
  constructor(protected http: HttpClient, protected url: string) {}

  getAll(page: number = 0, size: number = 10, search?: string, filterParam?: {key: string, value: any}): Observable<PaginatedResponse<T>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
      
    if (search) {
      params = params.set('q', search);
    }
    
    if (filterParam && filterParam.value) {
      params = params.set(filterParam.key, filterParam.value.toString());
    }

    return this.http.get<PaginatedResponse<T>>(this.url, { params });
  }

  getById(id: number): Observable<T> {
    return this.http.get<T>(`${this.url}/${id}`);
  }

  create(dto: SaveDTO): Observable<T> {
    return this.http.post<T>(this.url, dto);
  }

  update(id: number, dto: SaveDTO): Observable<T> {
    return this.http.put<T>(`${this.url}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
