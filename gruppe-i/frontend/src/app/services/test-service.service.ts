import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from "rxjs";
import {TestObject} from "../Models/testObject"
@Injectable({
  providedIn: 'root'
})
export class TestServiceService {

  private baseURI:String = "http://localhost:8080/test";
  constructor(private http:HttpClient) { }

  public getTestObjectList(){
    return this.http.get<TestObject[]>(this.baseURI+'/getTestObjectList' );
  }

  public createTestObject(testobject: TestObject){
    return this.http.post(this.baseURI+'/newTestObject', testobject, {responseType: 'text'});
  }
  public getBackendText(){
    return this.http.get(this.baseURI+'/text',{responseType: 'text'});
  }
}
