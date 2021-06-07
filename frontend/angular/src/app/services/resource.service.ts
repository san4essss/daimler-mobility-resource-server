import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Resource } from '../model/resource.model';
import {ResourceUpdateModel} from "../model/resourceUpdate.model";

const baseUrl = 'http://localhost:8080/api/resources';

const headers = new HttpHeaders().set('Authorization', 'api-key my-valid-api-key-admin');

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  constructor(private http: HttpClient) {
  }

  list(): Observable<any> {
    return this.http.get(`${baseUrl}?inactive=true&active=true`, {headers: headers});
  }

  get(id: string): Observable<any> {
    return this.http.get(`${baseUrl}/${id}`, {headers: headers});
  }

  create(data: ResourceUpdateModel): Observable<any> {
    const formData = this.createFormData(data);
    return this.http.post(baseUrl, formData, {headers: headers});
  }

  update(id: string, data: ResourceUpdateModel): Observable<any> {
    const formData = this.createFormData(data);
    return this.http.patch(`${baseUrl}/${id}`, formData, {headers: headers});
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`, {headers: headers});
  }

  private createFormData(data: ResourceUpdateModel) {
    const formData: FormData = new FormData();
    formData.append("description", data.description);
    formData.append("active", data.active.toString());
    if (data.file) {
      formData.append("file", data.file);
    }
    return formData;
  }
}
