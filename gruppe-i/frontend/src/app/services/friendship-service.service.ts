import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../Models/User";


@Injectable({
  providedIn: 'root'
})
export class FriendshipServiceService {

  private baseURI: String = "http://localhost:8080/friendlist";
  constructor(private http:HttpClient) {
  }

  getAllFriends(): Observable<User[]>{
    return this.http.get<User[]>(`${this.baseURI}/listFriends`)
  }

  sendEmail(formdata: FormData): Observable<String>{
    console.log(formdata)
    return this.http.post<String>(`${this.baseURI}/sendEmail`, formdata);
  }
}
