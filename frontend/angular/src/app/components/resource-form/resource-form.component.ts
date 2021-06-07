import { Component, OnInit } from '@angular/core';
import { ResourceService } from '../../services/resource.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ResourceUpdateModel } from '../../model/resourceUpdate.model';

@Component({
  selector: 'app-post-form',
  templateUrl: './resource-form.component.html'
})
export class ResourceFormComponent implements OnInit {

  resource: ResourceUpdateModel = {
    description: '',
    active: true
  };
  message = '';

  constructor(
    private resourceService: ResourceService,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.message = '';
    const id = this.route.snapshot.params.id;
    if (id) {
      this.editResource(this.route.snapshot.params.id);
    }
  }

  editResource(id: string): void {
    this.resourceService.get(id)
      .subscribe(
        data => {
          this.resource = data;
        },
        error => {
          console.error(error);
        });
  }

  saveResource(): void {
    this.message = '';

    if (this.resource.id) {
      this.saveEditedResource();
    } else {
      this.createNewResource();
    }
  }

  onFileSelect(event: any) {
    if (event.target.files.length > 0) {
      this.resource.file = event.target.files[0];
    }
  }

  private createNewResource() {
    this.resourceService.create(this.resource)
      .subscribe(
        response => {
          this.router.navigate([ '/resources' ]);
        },
        error => {
          console.error(error);
          this.message = 'An error occurred while saving resource';
        });
  }

  private saveEditedResource() {
    this.resourceService.update(this.resource.id, this.resource)
      .subscribe(
        response => {
          this.router.navigate([ '/resources' ]);
        },
        error => {
          console.error(error);
          this.message = 'An error occurred while saving resource';
        });
  }
}
