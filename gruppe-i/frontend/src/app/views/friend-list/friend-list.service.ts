import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FriendListService {

  private baseURI: string = "http://localhost:8080/friendListDemo"

  constructor(private http: HttpClient) {}

  public getLists(sessionID: String): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.baseURI}/${sessionID}/getLists`);
  }

  public sendFriendRequest(sessionID: string,friendRequestUserId:number):Observable<HttpResponse<any>>{

    let formData:FormData = new FormData();
    formData.append("receiverId", friendRequestUserId.toString());
    console.log("FriendID: " + friendRequestUserId + "\nSessionID: " + sessionID)
    formData.append("sessionID", sessionID);

    return this.http.post<any>(`${this.baseURI}/sendRequest`,formData);
  }

  public acceptFriendRequest(sessionID: string,friendRequestUserId:number):Observable<HttpResponse<any>>{
    let formData:FormData = new FormData();
    formData.append("receiverId", friendRequestUserId.toString());
    formData.append("sessionID", sessionID);

    return this.http.post<any>(`${this.baseURI}/acceptRequest`,formData);
  }
  public declineFriendRequest(sessionID: string,friendRequestUserId:number):Observable<HttpResponse<any>>{
    let formData:FormData = new FormData();
    formData.append("receiverId", friendRequestUserId.toString());
    formData.append("sessionID", sessionID);

    return this.http.post<any>(`${this.baseURI}/declineRequest`,formData);
  }
}
