import {HttpClient, HttpResponse} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { DataSet } from '../Models/DataSet';
import {local} from "d3";

@Injectable({
  providedIn: 'root',
})
export class DataSetService {
  constructor(private http: HttpClient) {}

  getDataSetList(): Observable<DataSet[]> {
    // work with local data and update logic in the future to get the data via the endpoint
    return this.http.get('http://localhost:8080/api/v1/datasetsSirine').pipe(
      map((response: any) => {
        return response.map((item: any, index: any) => {
          console.log('####', index);
          const dataSet: DataSet = {
            id: item.id,
            name: item.name,
            isFavorite: !!localStorage.getItem(`dataset${index}`),
            fileName: item.fileName,
            isXML: item.fileName.endsWith('.xml'),
            isCSV: item.fileName.endsWith('.csv'),
          };
          return dataSet;
        });
      })
    );
  }

  getSingleDataSet(id: string): Observable<any> {
    let url = 'http://localhost:8080/api/v1/datasetsSirine/' + id;
    return this.http.get(url);
  }

}
