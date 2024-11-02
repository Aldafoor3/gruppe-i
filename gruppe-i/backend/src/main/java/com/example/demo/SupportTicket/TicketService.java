package com.example.demo.SupportTicket;

import com.example.demo.admin.Admin;
import com.example.demo.admin.AdminRepository;
import com.example.demo.email.EmailService;
import com.example.demo.login.LoginService;
import com.example.demo.login.SessionRepository;
import com.example.demo.login.Sessions;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private EmailService emailService;

    public List<Ticket> getAllTickets(){
        System.out.println(ticketRepository.findAll().size());
        return ticketRepository.findAll();
    }

    public Ticket findTicketByTicketID(Long ticketID) {
        return ticketRepository.findById(ticketID).orElse(null);
    }


    public boolean sendTicket(Ticket ticket, String sessionID) {
        Users user = loginService.getUserBySessionID(sessionID);

        if (user != null) {
            ticket.setEmail(user.getEmail());
            ticket.setStatus(TicketStatus.UNBEARBEITET);
            ticketRepository.save(ticket);

            return true;
        }

        return false;
    }

    public boolean updateTicketStatus(Long ticketID, String sessionID) {
        Admin admin = loginService.getAdminBySessionID(sessionID);

        if (admin != null) {
            Ticket ticket = findTicketByTicketID(ticketID);

            if (ticket != null) {
                ticket.setStatus(TicketStatus.BEARBEITET);
                ticketRepository.save(ticket);
                String userEmail = ticket.getEmail();

                this.emailService.sendEmail(userEmail, "Ticket Update", "Dein Supportticket wurde bearbeitet.");


                return true;
            }
        }

        return false;
    }

    public TicketStatus getTicketStatusByTicketID(Long ticketID) {
        Ticket ticket = ticketRepository.findById(ticketID).orElse(null);
        if (ticket != null) {
            return ticket.getStatus();
        }
        return null;
    }





}