import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotServiceService {

  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {
  }

  countUsers(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/chatbot/countUsers`);
  }

  countAdmins(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/chatbot/countAdmins`);

  }
  countTickets(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/chatbot/countTickets`);

  }
  getFirstName(sessionId: string) {
    return this.http.get<string>(`${this.baseUrl}/chatbot/firstname?sessionId=${sessionId}`);
  }

}
