import { Component, ElementRef, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe, NgFor, NgIf } from '@angular/common';
import { gsap } from 'gsap';
import { DashboardService, DashboardVm } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, DecimalPipe, NgFor, NgIf],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  private dashboard = inject(DashboardService);
  private host = inject(ElementRef);
  data = signal<DashboardVm | null>(null);
  loading = signal(true);
  error = signal('');
  private ctx?: gsap.Context;
  private animated = false;

  ngOnInit(): void {
    this.refresh();
  }

  ngOnDestroy(): void {
    this.ctx?.revert();
  }

  refresh(): void {
    this.loading.set(true);
    this.error.set('');
    this.dashboard.load().subscribe({
      next: (vm) => {
        this.data.set(vm);
        this.loading.set(false);
        if (!this.animated) {
          this.animated = true;
          requestAnimationFrame(() => this.playIntro());
        }
      },
      error: () => {
        this.error.set('No se pudo cargar el dashboard.');
        this.loading.set(false);
      }
    });
  }

  private playIntro(): void {
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }
    const root = this.host.nativeElement as HTMLElement;
    this.ctx = gsap.context(() => {
      gsap.from('.card', { opacity: 0, y: 12, duration: 0.35, ease: 'power1.out', stagger: 0.05 });
      gsap.from('.col-fill', {
        scaleY: 0,
        transformOrigin: 'bottom center',
        duration: 0.5,
        ease: 'power1.out',
        stagger: 0.03,
        delay: 0.15
      });
      gsap.from('.meter-fill, .stack-seg', {
        scaleX: 0,
        transformOrigin: 'left center',
        duration: 0.5,
        ease: 'power1.out',
        stagger: 0.04,
        delay: 0.2
      });
    }, root);
  }
}
