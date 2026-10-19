import {
  AfterViewChecked,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';
import { DecimalPipe, NgFor, NgIf } from '@angular/common';
import { gsap } from 'gsap';
import { SceneOrbComponent } from '../../shared/components/scene-orb/scene-orb.component';
import { DashboardService, DashboardVm } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DecimalPipe, NgFor, NgIf, SceneOrbComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, AfterViewChecked, OnDestroy {
  private dashboard = inject(DashboardService);
  private host = inject(ElementRef);
  data = signal<DashboardVm | null>(null);
  loading = signal(true);
  error = signal('');
  readonly yearNow = new Date().getFullYear();
  private ctx?: gsap.Context;
  private animated = false;

  ngOnInit(): void {
    this.dashboard.load().subscribe({
      next: (vm) => {
        this.data.set(vm);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el dashboard.');
        this.loading.set(false);
      }
    });
  }

  ngAfterViewChecked(): void {
    if (this.animated || !this.data() || this.loading()) {
      return;
    }
    this.animated = true;
    this.playIntro();
  }

  ngOnDestroy(): void {
    this.ctx?.revert();
  }

  linePoints(series: DashboardVm['monthSeries'], max: number): string {
    return this.plotPoints(series, max).join(' ');
  }

  areaPoints(series: DashboardVm['monthSeries'], max: number): string {
    const pts = this.plotPoints(series, max);
    if (!pts.length) {
      return '';
    }
    return `0,56 ${pts.join(' ')} 100,56`;
  }

  pointY(value: number, max: number): number {
    return 56 - (value / Math.max(max, 1)) * 50 - 3;
  }

  pointX(index: number, total: number): number {
    return total <= 1 ? 50 : (index * 100) / (total - 1);
  }

  private plotPoints(series: DashboardVm['monthSeries'], max: number): string[] {
    return series.map((point, i) => `${this.pointX(i, series.length)},${this.pointY(point.value, max)}`);
  }

  private playIntro(): void {
    const reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (reduce) {
      return;
    }
    const root = this.host.nativeElement;
    this.ctx = gsap.context(() => {
      const tl = gsap.timeline({ defaults: { duration: 0.55, ease: 'power2.out' } });
      tl.from('.dash-hero', { y: -28, autoAlpha: 0 })
        .from('.panel', { y: 32, autoAlpha: 0, stagger: 0.08 }, '-=0.3')
        .from('.kpi', { y: 18, autoAlpha: 0, stagger: 0.07 }, '-=0.35');

      gsap.from('.bar-fill', {
        scaleX: 0,
        transformOrigin: 'left center',
        duration: 0.85,
        stagger: 0.1,
        ease: 'power2.out',
        delay: 0.45
      });

      const line = root.querySelector('.line-path') as SVGGeometryElement | null;
      if (line && 'getTotalLength' in line) {
        const len = line.getTotalLength();
        gsap.fromTo(
          line,
          { strokeDasharray: len, strokeDashoffset: len },
          { strokeDashoffset: 0, duration: 1.2, ease: 'power2.inOut', delay: 0.35 }
        );
      }
    }, root);
  }
}
