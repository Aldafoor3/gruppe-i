package com.example.demo.SupportTicket;

import com.example.demo.user.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @Column(name = "email")
     private String email;

    @Column( name = "description")
    private String description;

    @Column( name = "status")
    private TicketStatus status;

    public Ticket(String email,String description) {
        this.email=email;
        this.description = description;
        this.status=TicketStatus.UNBEARBEITET;



    }

    public Ticket() {
    }

}
  enum TicketStatus { UNBEARBEITET, BEARBEITET};