import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgFor, NgIf, NgClass, DatePipe } from '@angular/common';

export interface TableColumn {
  key: string;
  header: string;
  type?: 'text' | 'date' | 'boolean' | 'object';
  objectKey?: string; // Para acceder a subpropiedades si type === 'object'
}

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [NgFor, NgIf, NgClass, DatePipe],
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent {
  @Input() data: any[] = [];
  @Input() columns: TableColumn[] = [];
  @Input() totalElements: number = 0;
  @Input() currentPage: number = 0;
  @Input() pageSize: number = 10;
  @Input() totalPages: number = 0;
  @Input() hideActions: boolean = false;

  @Output() pageChange = new EventEmitter<number>();
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();
  @Output() view = new EventEmitter<any>();

  onPageChange(newPage: number) {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.pageChange.emit(newPage);
    }
  }

  getValue(row: any, col: TableColumn): any {
    if (col.type === 'object' && col.objectKey) {
      return row[col.key] ? row[col.key][col.objectKey] : '';
    }
    return row[col.key];
  }
}
