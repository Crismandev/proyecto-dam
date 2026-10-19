import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

type Field = { key: string; label: string; type?: string; required?: boolean; optionsUrl?: string; optionId?: string; optionLabel?: string };
type Resource = { title: string; endpoint: string; listEndpoint?: string; id: string; fields: Field[]; columns: {key:string;label:string}[] };

const RESOURCES: Record<string, Resource> = {
  categorias: { title: 'Categorías', endpoint: '/catalogo/categorias', listEndpoint: '/catalogo/categorias/todos', id: 'idCategoria', fields: [
    {key:'nombre',label:'Nombre',required:true},{key:'descripcion',label:'Descripción',type:'textarea'},{key:'icono',label:'Icono'}
  ], columns:[{key:'idCategoria',label:'ID'},{key:'nombre',label:'Nombre'},{key:'descripcion',label:'Descripción'},{key:'activo',label:'Activo'}] },
  niveles: { title: 'Niveles', endpoint: '/catalogo/niveles', id: 'idNivel', fields: [
    {key:'idCurso',label:'Curso',type:'select',required:true,optionsUrl:'/catalogo/cursos/todos',optionId:'idCurso',optionLabel:'nombre'},
    {key:'nombre',label:'Nombre',required:true},{key:'descripcion',label:'Descripción',type:'textarea'},{key:'orden',label:'Orden',type:'number',required:true}
  ], columns:[{key:'idNivel',label:'ID'},{key:'nombre',label:'Nombre'},{key:'idCurso',label:'Curso'},{key:'orden',label:'Orden'}] },
  lecciones: { title: 'Lecciones', endpoint: '/catalogo/lecciones', id: 'idLeccion', fields: [
    {key:'idNivel',label:'Nivel',type:'select',required:true,optionsUrl:'/catalogo/niveles',optionId:'idNivel',optionLabel:'nombre'},
    {key:'titulo',label:'Título',required:true},{key:'descripcion',label:'Descripción',type:'textarea'},{key:'contenido',label:'Contenido',type:'textarea',required:true},{key:'orden',label:'Orden',type:'number',required:true},{key:'xpRecompensa',label:'XP de recompensa',type:'number'}
  ], columns:[{key:'idLeccion',label:'ID'},{key:'titulo',label:'Título'},{key:'idNivel',label:'Nivel'},{key:'orden',label:'Orden'},{key:'xpRecompensa',label:'XP'}] },
  ejercicios: { title: 'Ejercicios', endpoint: '/catalogo/ejercicios', id: 'idEjercicio', fields: [
    {key:'idLeccion',label:'Lección',type:'select',required:true,optionsUrl:'/catalogo/lecciones',optionId:'idLeccion',optionLabel:'titulo'},
    {key:'tipoEjercicio',label:'Tipo de ejercicio',required:true},{key:'enunciado',label:'Enunciado',type:'textarea',required:true},{key:'orden',label:'Orden',type:'number',required:true},{key:'respuestaCorrecta',label:'Respuesta correcta',type:'textarea'},{key:'explicacion',label:'Explicación',type:'textarea'},{key:'xpRecompensa',label:'XP de recompensa',type:'number'}
  ], columns:[{key:'idEjercicio',label:'ID'},{key:'tipoEjercicio',label:'Tipo'},{key:'enunciado',label:'Enunciado'},{key:'idLeccion',label:'Lección'},{key:'orden',label:'Orden'}] },
  roles: { title: 'Roles', endpoint: '/seguridad/roles', id: 'idRol', fields: [ {key:'nombre',label:'Nombre',required:true} ], columns:[{key:'idRol',label:'ID'},{key:'nombre',label:'Nombre'}] }
};

@Component({
  selector: 'app-backend-crud', standalone: true, imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="page-header"><h1>{{ resource.title }}</h1><button *ngIf="!editing" class="btn btn-primary" (click)="newRecord()">+ Nuevo</button><button *ngIf="editing" class="btn btn-secondary" (click)="cancel()">Volver</button></div>
    <div class="card" *ngIf="editing"><form [formGroup]="form" (ngSubmit)="save()">
      <div class="form-group" *ngFor="let field of resource.fields"><label>{{field.label}} <span *ngIf="field.required" class="text-danger">*</span></label>
        <select *ngIf="field.type === 'select'" class="form-control" [formControlName]="field.key"><option [ngValue]="null">Seleccione</option><option *ngFor="let option of options[field.key] || []" [ngValue]="option[field.optionId!]">{{option[field.optionLabel!]}}</option></select>
        <textarea *ngIf="field.type === 'textarea'" class="form-control" [formControlName]="field.key" rows="4"></textarea>
        <input *ngIf="field.type !== 'select' && field.type !== 'textarea'" class="form-control" [type]="field.type || 'text'" [formControlName]="field.key">
      </div><button class="btn btn-primary" type="submit" [disabled]="form.invalid">Guardar</button>
    </form></div>
    <div class="card" *ngIf="!editing"><div class="form-group"><input class="form-control" placeholder="Buscar..." [(ngModel)]="search" (ngModelChange)="filter()"></div>
      <div class="table-responsive"><table class="table"><thead><tr><th *ngFor="let col of resource.columns">{{col.label}}</th><th>Acciones</th></tr></thead><tbody>
        <tr *ngIf="visibleRows.length===0"><td [attr.colspan]="resource.columns.length+1" class="text-center empty-state">No hay registros para mostrar.</td></tr>
        <tr *ngFor="let row of visibleRows"><td *ngFor="let col of resource.columns">{{row[col.key]}}</td><td class="actions-col"><div class="row-actions"><button type="button" class="btn-icon btn-edit" (click)="edit(row)" title="Editar" aria-label="Editar"><i class="ri-pencil-line"></i></button><button type="button" class="btn-icon btn-delete" (click)="remove(row)" title="Eliminar" aria-label="Eliminar"><i class="ri-delete-bin-line"></i></button></div></td></tr>
      </tbody></table></div>
    </div>`
})
export class BackendCrudComponent implements OnInit {
  private http=inject(HttpClient); private route=inject(ActivatedRoute); private router=inject(Router); private fb=inject(UntypedFormBuilder);
  resource!: Resource; resourceKey=''; rows:any[]=[]; visibleRows:any[]=[]; search=''; editing=false; recordId:number|null=null; options:Record<string,any[]>={}; form:UntypedFormGroup=this.fb.group({});
  ngOnInit(){
    this.resourceKey=this.route.snapshot.data['resource']; this.resource=RESOURCES[this.resourceKey];
    for(const field of this.resource.fields){ const validators=[] as any[]; if(field.required)validators.push(Validators.required); if(field.type==='number'&&field.key==='orden')validators.push(Validators.min(1)); this.form.addControl(field.key,this.fb.control(null,validators)); if(field.optionsUrl)this.http.get<any[]>(environment.apiUrl+field.optionsUrl).subscribe(items=>this.options[field.key]=items); }
    const id=this.route.snapshot.paramMap.get('id'); if(id){this.editing=true;this.recordId=Number(id);this.http.get<any>(`${environment.apiUrl}${this.resource.endpoint}/${id}`).subscribe(row=>this.form.patchValue(row));} else if(this.route.snapshot.url.some(segment=>segment.path==='nuevo')){this.editing=true;} else this.load();
  }
  load(){this.http.get<any[]>(environment.apiUrl+(this.resource.listEndpoint||this.resource.endpoint)).subscribe(items=>{this.rows=items;this.filter();});}
  filter(){const q=this.search.toLocaleLowerCase();this.visibleRows=this.rows.filter(row=>JSON.stringify(row).toLocaleLowerCase().includes(q));}
  newRecord(){this.recordId=null;this.form.reset();this.editing=true;this.router.navigate(['/admin',this.resourceKey,'nuevo']);}
  edit(row:any){this.recordId=row[this.resource.id];this.editing=true;this.router.navigate(['/admin',this.resourceKey,this.recordId,'editar']);this.http.get<any>(`${environment.apiUrl}${this.resource.endpoint}/${this.recordId}`).subscribe(item=>this.form.patchValue(item));}
  save(){if(this.form.invalid)return;const call=this.recordId?this.http.put(`${environment.apiUrl}${this.resource.endpoint}/${this.recordId}`,this.form.getRawValue()):this.http.post(`${environment.apiUrl}${this.resource.endpoint}`,this.form.getRawValue());call.subscribe(()=>{this.editing=false;this.recordId=null;this.router.navigate(['/admin',this.resourceKey]);this.load();});}
  remove(row:any){if(confirm(`¿Seguro que deseas eliminar este registro?`))this.http.delete(`${environment.apiUrl}${this.resource.endpoint}/${row[this.resource.id]}`).subscribe(()=>this.load());}
  cancel(){this.editing=false;this.recordId=null;this.form.reset();this.router.navigate(['/admin',this.resourceKey]);}
}
