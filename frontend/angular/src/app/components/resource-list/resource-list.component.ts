import { Component, OnInit } from '@angular/core';
import { Resource } from '../../model/resource.model';
import { ResourceService } from '../../services/resource.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-resource-list',
  templateUrl: './resource-list.components.html'
})
export class ResourceListComponent implements OnInit {

  resources?: Resource[];
  selected?: Resource;
  currentIndex: number = -1;
  message: string = '';

  constructor(private resourceService: ResourceService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.queryParams
      .subscribe(params => {
              this.getResources();
          }
      );
  }

  getResources(): void {
    this.resourceService.list()
      .subscribe(
        data => {
          this.resources = data;
        },
        error => {
          console.error(error);
        });
  }

  refreshList(): void {
    this.getResources();
    this.selected = undefined;
    this.currentIndex = -1;
  }

  setSelected(resource: Resource, index: number): void {
    if (this.selected && this.selected.id == resource.id) {
      this.selected = undefined;
      this.currentIndex = -1;
    } else {
      this.selected = resource;
      this.currentIndex = index;
    }
  }

  deleteResource(): void {
    if (!this.selected) {
      return;
    }

    this.resourceService.delete(this.selected.id)
      .subscribe(
        response => {
          this.refreshList();
        },
        error => {
          console.error(error);
        });
  }

  toDate(ms: number): Date {
      return new Date(ms);
  }
}
