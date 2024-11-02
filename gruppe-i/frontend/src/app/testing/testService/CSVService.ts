import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from "rxjs";
import {TestCSV} from "../Models/testCSV";

@Injectable({
  providedIn: 'root'
})
export class CSVService {
  private baseURI:String = "http://localhost:8080";
   constructor(private http:HttpClient) {

   }

  public getCSVlist(){
    return this.http.get<TestCSV[]>(this.baseURI+'/VornamenNeuGeborenen/alleVornamen')
  }



}
