package com.example.demo.SupportTicket;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;



    @PostMapping("/createTicket")
    public ResponseEntity<String> sendTicket(@RequestParam("sessionID") String sessionID,
                                             @RequestParam("description") String description) {
        Ticket ticket = new Ticket();
        ticket.setDescription(description);

        boolean ticketSent = ticketService.sendTicket(ticket, sessionID);

        if (ticketSent) {
            return new ResponseEntity<>("Ticket erfolgreich gesendet", HttpStatus.OK);
        }

        return new ResponseEntity<>("Fehler beim Senden des Tickets", HttpStatus.CONFLICT);
    }



    @PostMapping("/updateTicketStatus")
    public ResponseEntity<String> updateTicketStatus(@RequestParam("ticketID") Long ticketID,
                                                     @RequestParam("sessionID") String sessionID) {
        boolean ticketUpdated = ticketService.updateTicketStatus(ticketID, sessionID);

        if (ticketUpdated) {
            return new ResponseEntity<>("Ticket-Status erfolgreich aktualisiert", HttpStatus.OK);
        }

        return new ResponseEntity<>("Fehler beim Aktualisieren des Ticket-Status", HttpStatus.CONFLICT);
    }

    @GetMapping("/listTickets")
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
  }
    @GetMapping("/listTicketStatus")
    public ResponseEntity<List<Ticket>> fetchAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();

        for (Ticket ticket : tickets) {
            TicketStatus status = ticketService.getTicketStatusByTicketID(ticket.getId());
            if (status != null) {
                ticket.setDescription(ticket.getDescription() + " - " + status.name());
            }
        }

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
