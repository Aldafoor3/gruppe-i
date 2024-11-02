import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpStatusCode, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Ticket} from "../Models/Ticket";
import {LoginServiceService} from "./login-service.service"; //--> hiervon sessionID??


@Injectable({
  providedIn: 'root'
})
export class TicketlistServiceService {

  private baseURI:String = "http://localhost:8080/ticket";
  constructor(private http:HttpClient) { }

  //Ticket-Status ändern
  resolveTicket(formData: FormData): Observable<any> {
    console.log(formData)
    return this.http.post<any>(`${this.baseURI}/updateTicketStatus`, formData);

  }
  // HTTP-Anfrage an Server senden und Liste von Tickets abzurufen

  getAllTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.baseURI}/listTickets`);
    // HTTP-GET-Anfrage an die angegebenen URL
  }
}
