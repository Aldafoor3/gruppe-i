import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Admin } from "../Models/Admin";
import { User } from "../Models/User";
import {formatDate} from "@angular/common";
@Injectable({
  providedIn: 'root'
})
export class RegistrationServiceService {
  private baseURI:String = "http://localhost:8080/registration";
  constructor(private http:HttpClient) { }

  // Admin requests
  public registerAdmin(admin:Admin){
    return this.http.post(this.baseURI+'/createAdmin', admin,  {observe:'response', responseType:'text'});
  }

  public getAdminList(){
    return this.http.get<Admin[]>(this.baseURI+'/listAdmin');
  }


  // User Requests
  public registerUser(user: User){
    const formData = new FormData();
    formData.append('firstname',user.firstname);
    formData.append('lastname',user.lastname);
    formData.append('email',user.email);
    formData.append('password',user.password);
    formData.append('birthday',user.birthday);
    if (user.profilePictureFile!=undefined) {
      formData.append('profilePicture', user.profilePictureFile);
      return this.http.post(this.baseURI+'/createUserWithP', formData,  {observe:'response', responseType:'text'})
    }
    return this.http.post(this.baseURI+'/createUserWithoutP', formData,  {observe:'response',responseType:'text'})
  }

  public getUserList() {
    return this.http.get<User[]>(this.baseURI+'/listUser');
  }
}
