package com.example.demo.ChatBot;

import com.example.demo.SupportTicket.Ticket;
import com.example.demo.SupportTicket.TicketRepository;
import com.example.demo.admin.AdminRepository;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import com.example.demo.admin.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public Integer getUserCount() {
        List<Users> userList = userRepository.findAll();
        return userList.size();
    }

    public Integer getAdminCount() {
        List<Admin> adminList = adminRepository.findAll();
        return adminList.size();

    }

    public Integer getTicketCount() {
        List<Ticket>  ticketList = ticketRepository.findAll();
        return ticketList.size();
    }
}

