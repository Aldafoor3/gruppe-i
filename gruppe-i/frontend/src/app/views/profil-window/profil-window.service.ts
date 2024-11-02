import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProfilWindowService {
  private baseURI: string = "http://localhost:8080/profile"

  constructor(private http: HttpClient) { }

  public setBackgroundColor(sessionId:string, backgroundColor:string){
    return this.http.post<String>(`${this.baseURI}/setColor/${sessionId}`, backgroundColor);

  }

  public getProfile(profileID: bigint, sessionID: string): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.baseURI}/${profileID}/${sessionID}`);
  }

  public getProfilePrivacySetting(profileID: bigint, sessionID: string){
    return this.http.get<any>(`${this.baseURI}/getPrivacySettings/${profileID}/${sessionID}`);
  }

  public changePrivacySetting(profileID: bigint, sessionID: string, privacySetting: boolean) {
    const formdata: FormData = new FormData();
    formdata.append("profileID", profileID.toString());
    formdata.append("sessionID", sessionID);
    formdata.append("privacySetting", privacySetting.toString());

    return this.http.post(`${this.baseURI}/changePrivacy`, formdata);
  }

  public getAccIdBySessionId(){
    return this.http.get<number>(this.baseURI + "/getAccBySessionId/"+ localStorage.getItem("SID"));
  }

  public getProfilPictureObservable(userID:number){
    return this.http.get('http://localhost:8080/registration/users/'+userID.toString()+'/profile-picture', {responseType:"arraybuffer"})
  }

  public getFriendListFromProfile(profilId:bigint, sessionId:string): Observable<any>{
    return this.http.get<any>("http://localhost:8080/friendListDemo/"+sessionId+"/getFriendListOfProfile/"+profilId);
  }

  changeFriendlistPrivacySetting(profileID: bigint, sessionID: string, friendListPrivacySetting: boolean) {
    const formdata: FormData = new FormData();
    formdata.append("profileID", profileID.toString());
    formdata.append("sessionID", sessionID);
    formdata.append("privacySetting", friendListPrivacySetting.toString());

    return this.http.post(`${this.baseURI}/changeFriendlistPrivacy`, formdata);
  }

  changeProfileData(formdata: FormData) {
    return this.http.post(`${this.baseURI}/changeUserData`, formdata);
  }

  giveNyan(id: bigint) {
    return this.http.post(`${this.baseURI}/giveNyan/`+id, {});
  }
}
