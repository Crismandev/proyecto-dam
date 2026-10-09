import { Component, EventEmitter, Output, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-search-input',
  standalone: true,
  imports: [FormsModule, NgIf],
  template: `
    <div class="search-wrapper">
      <div class="input-container">
        <i class="ri-search-line search-icon"></i>
        <input type="text" 
               [placeholder]="placeholder" 
               [(ngModel)]="searchTerm" 
               (keyup.enter)="onSearch()"
               class="form-control search-input">
        <button *ngIf="searchTerm" class="clear-btn" (click)="clearSearch()">
          <i class="ri-close-line"></i>
        </button>
      </div>
      <button class="btn btn-primary" (click)="onSearch()">
        <i class="ri-search-line"></i>
        <span>Buscar</span>
      </button>
    </div>
  `,
  styles: [`
    .search-wrapper {
      display: flex;
      gap: 0.5rem;
      align-items: center;
    }
    .input-container {
      position: relative;
      display: flex;
      align-items: center;
    }
    .search-icon {
      position: absolute;
      left: 0.875rem;
      color: #94A3B8;
      font-size: 1.1rem;
      pointer-events: none;
    }
    .search-input {
      min-width: 280px;
      padding-left: 2.5rem !important;
      padding-right: 2.25rem !important;
    }
    .clear-btn {
      position: absolute;
      right: 0.625rem;
      background: transparent;
      border: none;
      color: #94A3B8;
      cursor: pointer;
      font-size: 1.1rem;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0.2rem;
      border-radius: 50%;
      &:hover { color: #475569; background: #E2E8F0; }
    }
  `]
})
export class SearchInputComponent {
  @Input() placeholder: string = 'Buscar...';
  @Output() search = new EventEmitter<string>();
  
  searchTerm: string = '';

  onSearch() {
    this.search.emit(this.searchTerm);
  }

  clearSearch() {
    this.searchTerm = '';
    this.search.emit('');
  }
}
