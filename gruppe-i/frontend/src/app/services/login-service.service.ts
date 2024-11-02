import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpStatusCode, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {

  private baseURI:String = "http://localhost:8080/login";
  constructor(private http:HttpClient) { }
  //User
  public loginUser(email:string,password:string): Observable<HttpResponse<any>>{
     return this.http.post<any>(this.baseURI+'/User/'+email+'/'+password,null , {observe:'response'});
  }
  public twoFactorCheckUser(email:string,password:string,twoFactorCode:number):Observable<HttpResponse<any>>{
    return this.http.post<any>(this.baseURI+'/User/verify/'+email+'/'+password+'/'+twoFactorCode,null , {observe:'response'});
  }

  //Admin
  public loginAdmin(email:string,password:string): Observable<HttpResponse<any>>{
    return this.http.post<any>(this.baseURI+'/Admin/'+email+'/'+password,null , {observe:'response'});
  }

  public twoFactorCheckAdmin(email:string,password:string,twoFactorCode:number):Observable<HttpResponse<any>>{
    return this.http.post<any>(this.baseURI+'/Admin/verify/'+email+'/'+password+'/'+twoFactorCode,null , {observe:'response'});
  }

  public logout():Observable<HttpResponse<any>>{
    const formdata: FormData = new FormData();
    let sessionId:string = '';
    if (localStorage.getItem('SID')!=null){
      sessionId = localStorage.getItem('SID')!;
      localStorage.removeItem('SID');
    }
    formdata.append('sessionId',sessionId);
    return this.http.post<any>(this.baseURI+'/logout',formdata);
  }

  public isLoggedIn():boolean{
    return ((localStorage.getItem('SID') != null && localStorage.getItem('SID') != ''));
  }

  public isUser(){
    return this.http.get<boolean>(this.baseURI+"/isUser/"+localStorage.getItem('SID'));
  }
}
