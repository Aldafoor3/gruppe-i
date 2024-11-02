import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TicketServiceService {
  private baseURI: string = "http://localhost:8080/ticket";

  constructor(private http: HttpClient) {}

  createTicket(formdata: FormData): Observable<String> {
    console.log(formdata)
    return this.http.post<String>(`${this.baseURI}/createTicket`, formdata);
  }

}




