import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResourceListComponent } from './components/resource-list/resource-list.component';
import { ResourceFormComponent } from './components/resource-form/resource-form.component';

const routes: Routes = [
  { path: '', redirectTo: 'resources', pathMatch: 'full' },
  { path: 'resources', component: ResourceListComponent },
  { path: 'form', component: ResourceFormComponent },
  { path: 'form/:id', component: ResourceFormComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {
}
