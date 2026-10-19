import * as THREE from 'three';

export type BrandSceneHandle = {
  dispose: () => void;
};

export function createBrandScene(
  canvas: HTMLCanvasElement,
  variant: 'hero' | 'compact' = 'compact'
): BrandSceneHandle {
  const renderer = new THREE.WebGLRenderer({
    canvas,
    antialias: true,
    alpha: true,
    powerPreference: 'high-performance'
  });
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
  renderer.setClearColor(0x000000, 0);

  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(42, 1, 0.1, 40);
  camera.position.set(0, 0.15, variant === 'hero' ? 5.2 : 3.4);

  scene.add(new THREE.AmbientLight(0xffffff, 0.55));
  const key = new THREE.DirectionalLight(0x7c4dff, 1.4);
  key.position.set(2.4, 2.2, 3.2);
  scene.add(key);
  const fill = new THREE.PointLight(0xff6b35, 1.1, 12);
  fill.position.set(-2.2, -0.6, 2.4);
  scene.add(fill);

  const group = new THREE.Group();
  scene.add(group);

  const core = new THREE.Mesh(
    new THREE.IcosahedronGeometry(variant === 'hero' ? 1.15 : 0.85, 1),
    new THREE.MeshStandardMaterial({
      color: 0x7c4dff,
      metalness: 0.35,
      roughness: 0.28,
      emissive: 0x2a1b5c,
      emissiveIntensity: 0.35,
      wireframe: true
    })
  );
  group.add(core);

  const inner = new THREE.Mesh(
    new THREE.IcosahedronGeometry(variant === 'hero' ? 0.55 : 0.38, 0),
    new THREE.MeshStandardMaterial({
      color: 0xff6b35,
      metalness: 0.2,
      roughness: 0.4,
      emissive: 0xb66430,
      emissiveIntensity: 0.45
    })
  );
  group.add(inner);

  const ring = new THREE.Mesh(
    new THREE.TorusGeometry(variant === 'hero' ? 1.65 : 1.15, 0.025, 12, 80),
    new THREE.MeshStandardMaterial({
      color: 0x2592c5,
      metalness: 0.6,
      roughness: 0.25,
      emissive: 0x2592c5,
      emissiveIntensity: 0.2
    })
  );
  ring.rotation.x = Math.PI / 2.4;
  group.add(ring);

  if (variant === 'hero') {
    const count = 48;
    const positions = new Float32Array(count * 3);
    for (let i = 0; i < count; i++) {
      const r = 2.1 + Math.random() * 1.6;
      const theta = Math.random() * Math.PI * 2;
      const phi = Math.acos(2 * Math.random() - 1);
      positions[i * 3] = r * Math.sin(phi) * Math.cos(theta);
      positions[i * 3 + 1] = r * Math.sin(phi) * Math.sin(theta);
      positions[i * 3 + 2] = r * Math.cos(phi);
    }
    const stars = new THREE.BufferGeometry();
    stars.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    group.add(
      new THREE.Points(
        stars,
        new THREE.PointsMaterial({ color: 0xffffff, size: 0.035, transparent: true, opacity: 0.7 })
      )
    );
  }

  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  let frame = 0;
  let disposed = false;

  const resize = () => {
    const parent = canvas.parentElement;
    const w = parent?.clientWidth || canvas.clientWidth || 320;
    const h = parent?.clientHeight || canvas.clientHeight || 180;
    renderer.setSize(w, h, false);
    camera.aspect = w / Math.max(h, 1);
    camera.updateProjectionMatrix();
  };

  resize();
  const ro = new ResizeObserver(resize);
  if (canvas.parentElement) {
    ro.observe(canvas.parentElement);
  }

  const tick = (time: number) => {
    if (disposed) {
      return;
    }
    frame = requestAnimationFrame(tick);
    if (!reduceMotion) {
      const t = time * 0.001;
      core.rotation.y = t * 0.35;
      core.rotation.x = t * 0.12;
      inner.rotation.y = -t * 0.55;
      ring.rotation.z = t * 0.22;
      group.rotation.y = Math.sin(t * 0.3) * 0.12;
    }
    renderer.render(scene, camera);
  };
  frame = requestAnimationFrame(tick);

  return {
    dispose: () => {
      disposed = true;
      cancelAnimationFrame(frame);
      ro.disconnect();
      scene.traverse((obj) => {
        if (obj instanceof THREE.Mesh || obj instanceof THREE.Points) {
          obj.geometry.dispose();
          const mat = obj.material;
          if (Array.isArray(mat)) {
            mat.forEach((m) => m.dispose());
          } else {
            mat.dispose();
          }
        }
      });
      renderer.dispose();
    }
  };
}
