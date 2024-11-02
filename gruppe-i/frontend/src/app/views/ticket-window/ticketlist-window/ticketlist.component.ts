import { Component, OnInit } from '@angular/core';
import { TicketlistServiceService } from "../../../services/ticketlist-service.service";
import { Ticket } from '../../../Models/Ticket';


@Component({
  selector: 'app-ticketlist',
  templateUrl: './ticketlist.component.html',
  styleUrls: ['./ticketlist.component.scss']
})
export class TicketlistComponent implements OnInit {
  supportTickets: Ticket[] = [];

  ticketID: string = "";

  constructor(private ticketlistService: TicketlistServiceService) {}

  ngOnInit() {
    this.fetchTickets();

  }

  fetchTickets() {
    this.ticketlistService.getAllTickets().subscribe(
      tickets => {
        this.supportTickets = tickets;
        console.log(this.supportTickets);
      },
      error => {
        console.log('Error fetching tickets:', error);
      }
    );
  }



  resolveTicketStatus(ticketID: number) {
    if (localStorage.getItem('SID') == null || localStorage.getItem('SID') == '') {
      alert('Nicht eingeloggt');
      return;
    }

    const formData = new FormData();
    formData.append('sessionID', localStorage.getItem('SID')!);
    formData.append('ticketID', ticketID.toString());
    console.log(formData);

    this.ticketlistService.resolveTicket(formData).subscribe(
      () => {
        console.log('Data sent successfully');
      },
      error => {
        console.error('Error sending data', error);
      }
    );
  }


}
