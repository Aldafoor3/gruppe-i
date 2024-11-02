package com.example.demo.ChatBot;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/chatbot")
@CrossOrigin(origins = "http://localhost:4200")
@Setter
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping("/countUsers")
    public ResponseEntity<Integer> countUsers() {
        Integer userCount = chatbotService.getUserCount();
        if (userCount != null) {
            return new ResponseEntity<>(userCount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/countAdmins")
    public ResponseEntity<Integer> countAdmins() {
        Integer adminCount = chatbotService.getAdminCount();
        if (adminCount != null) {
            return new ResponseEntity<>(adminCount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/countTickets")
    public ResponseEntity<Integer> countTickets() {
        Integer ticketCount = chatbotService.getTicketCount();
        if (ticketCount != null) {
            return new ResponseEntity<>(ticketCount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}



