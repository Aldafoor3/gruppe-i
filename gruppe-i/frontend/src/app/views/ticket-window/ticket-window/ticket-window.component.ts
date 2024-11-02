import { Component } from '@angular/core';
import { TicketServiceService } from "../../../services/ticket-service.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-ticket-window',
  templateUrl: './ticket-window.component.html',
  styleUrls: ['./ticket-window.component.scss']
})
export class TicketWindowComponent {

  description: string = "";

  constructor(
    private ticketService: TicketServiceService,
    private router: Router
  ) {}

  createTicket() {

    if(localStorage.getItem('SID')==null || localStorage.getItem('SID')==''){
      alert('nicht eingeloggt');
      return;
    }

    let formData = new FormData();
    formData.append('description',this.description.toString());
    formData.append('sessionID',localStorage.getItem('SID')!);
    console.log(formData);
    this.ticketService.createTicket(formData).subscribe(
      (response) => {
        console.log("Ticket created successfully");

      },
      (error) => {
        console.error("Error creating ticket:", error);

      }
    );
  }
}




