import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export class CrudService<T, SaveDTO> {
  constructor(protected http: HttpClient, protected url: string) {}

  getAll(filterParam?: {key: string, value: any}): Observable<T[]> {
    let params = new HttpParams();
    if (filterParam?.value !== undefined && filterParam.value !== null) {
      params = params.set(filterParam.key, filterParam.value.toString());
    }
    return this.http.get<T[]>(this.url, { params });
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
