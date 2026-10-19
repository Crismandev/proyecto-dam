import { AfterViewInit, Component, ElementRef, Input, OnDestroy, ViewChild } from '@angular/core';
import { BrandSceneHandle, createBrandScene } from '../../three/brand-scene';

@Component({
  selector: 'app-scene-orb',
  standalone: true,
  template: `<canvas #canvas class="scene-orb" aria-hidden="true"></canvas>`,
  styles: [`
    :host {
      display: block;
      width: 100%;
      height: 100%;
      pointer-events: none;
    }
    .scene-orb {
      display: block;
      width: 100%;
      height: 100%;
    }
  `]
})
export class SceneOrbComponent implements AfterViewInit, OnDestroy {
  @Input() variant: 'hero' | 'compact' = 'compact';
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  private handle?: BrandSceneHandle;

  ngAfterViewInit(): void {
    this.handle = createBrandScene(this.canvas.nativeElement, this.variant);
  }

  ngOnDestroy(): void {
    this.handle?.dispose();
  }
}
