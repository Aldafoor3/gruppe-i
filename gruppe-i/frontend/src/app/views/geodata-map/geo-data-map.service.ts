import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GeoDataMapService {
  private baseURI: string = "http://localhost:8080/geoData"

  constructor(private http: HttpClient) {}

  public uploadGeoData(file: File, fileName: String): Promise<any> {
    const formData: FormData = new FormData();
    formData.append("file", file, file.name);

    const headers = new HttpHeaders();
    headers.append("Content-Type", "multipart/form-data");
    headers.append("Accept", "application/geo+json");

    console.log(this.http.post<any>(this.baseURI + "/uploadFile", formData))

    return this.http.post<any>(`${this.baseURI}/uploadFile`, formData, {headers: headers}).toPromise();
  }

  public getGeoDatasets() {
    return this.http.get<Object[]>(`${this.baseURI}/getGeoDatasets`);
  }
}
