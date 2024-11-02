import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Chat} from "../Models/Chat";
import {formatDate} from "@angular/common";
import {Form} from "@angular/forms";
import {Observable} from "rxjs";
import {Message_Instance} from "../Models/Message_Instance";
@Injectable({
  providedIn: 'root'
})
export class ChatserviceService {
  private baseURI:String = "http://localhost:8080/";
  constructor(private http:HttpClient) { }

  public createChat(chatName:string, participantsId: number[]){
    if(localStorage.getItem('SID') == "" || localStorage.getItem('SID') == null){
      alert("you are not Logged in.");
      return;
    }
    return this.http.post<HttpResponse<String>>(this.baseURI+"chat/create/"+chatName+"/"+localStorage.getItem('SID'), participantsId );
  }

  public getChatList(){
    if(localStorage.getItem('SID')=='' || localStorage.getItem("SID")==null){
      alert("you are not logged in");
    }

    let formaData:FormData = new FormData();
    return this.http.get<Chat[]>(this.baseURI+"chat/getList/"+localStorage.getItem('SID'));
  }

  public sendMsg(chatRoomId:bigint, msg:string){
    let sessionId = localStorage.getItem('SID');
    if(sessionId == null||sessionId==""){alert("you'r not logged in.");return null;}
    let formData: FormData = new FormData();
    formData.append('message', msg);
    formData.append('sessionId', sessionId);
    return this.http.post<string>(this.baseURI + "chat/sendMSG/"+chatRoomId,formData);
  }

  public getChatRoomMessages(chatRoomId:bigint){
    let sessionId = localStorage.getItem('SID');
    if(sessionId == null||sessionId==""){alert("you'r not logged in.")}
    return this.http.get<Message_Instance[]>(this.baseURI+ "chat/fetch/"+chatRoomId+"/"+sessionId);

  }

  //EDIT AND DELETE MESSAGES
  public editMessage(messageId:number, newMessage:string){
    let sessionId = localStorage.getItem('SID');
    if(sessionId == null||sessionId==""){alert("you're not logged in.");return new Observable<string>();}
    let formData: FormData = new FormData();
    formData.append('newMessage', newMessage);
    formData.append('sessionId', sessionId);
    console.log("message:" + messageId);
    console.log("new message:" + newMessage);
    return this.http.post<string>(this.baseURI+"chat/editMSG/"+messageId,formData);
  }

  public deleteMSG(messageId:number){
    let sessionId = localStorage.getItem('SID');
    if(sessionId == null||sessionId==""){alert("you're not logged in.");return new Observable<string>();}
    let formData: FormData = new FormData();
    formData.append('sessionId', sessionId);
    return this.http.post<string>(this.baseURI+"chat/deleteMSG/"+messageId,formData);
  }
}
