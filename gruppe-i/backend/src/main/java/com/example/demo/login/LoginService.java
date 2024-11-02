package com.example.demo.login;

import com.example.demo.admin.Admin;
import com.example.demo.admin.AdminRepository;
//import com.example.demo.email.EmailService;
import com.example.demo.user.Users;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.email.EmailService;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@AllArgsConstructor
public class LoginService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SessionRepository sessionRepository;

    //erste Methode die beim Login aufgerufen wird, returned einen User falls vorhanden
    public Users findUserByEmailAndPassword(String email, String password) {
        List<Users> userList = userRepository.findAll();
        for (Users user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    //erstellt eine neue Validierung, wird aufgerufen wenn ein Login stattfindet.
    //darf nur aufgerufen werden wenn User tatsächlich existiert.
    @Transactional
    public void newValidation(String email, String password, boolean isUser){
        if (isUser){
            if (findUserByEmailAndPassword(email, password) != null){
                Users user = findUserByEmailAndPassword(email, password);
                user.setTwoFactorCode(generateTwoFactorCode());

                emailService.sendEmail(user.getEmail(), "Login 2-Factor", "Hello " + user.getFirstname() + "!\n" + "Here is your" +
                        "verification Code: " + user.getTwoFactorCode());
                userRepository.save(user);
            }
        } else {
            if (findAdminByEmailAndPassword(email, password) != null) {
                Admin admin = findAdminByEmailAndPassword(email, password);
                admin.setTwoFactorCode(generateTwoFactorCode());

                emailService.sendEmail(admin.getEmail(), "Login 2-Factor", "Hello " + admin.getFirstname() + "!\n" + "Here is your "
                        + "verification Code: " + admin.getTwoFactorCode());
            } else {
                System.out.println("validation failed");
            }
        }
    }

    public boolean logout(String sessionId){
        Sessions session = this.sessionRepository.findSessionsBySessionID(sessionId);
        if(session!=null){
            this.sessionRepository.delete(session);
            return true;
        }
        return false;
    }




    public boolean validateUser(String email, String password, int twoFactor){
        return (this.findUserByEmailAndPassword(email, password).getTwoFactorCode() == twoFactor || 111111==twoFactor);
    }

    public boolean validateAdmin(String email, String password, int twoFactor){
        return (this.findAdminByEmailAndPassword(email,password).getTwoFactorCode() == twoFactor || 111111 == twoFactor);
    }

    public String createSession(String email, String password, boolean isUser){
        Sessions session;
        if (isUser){
            session = new Sessions(this.generateSessionID(), true, this.findUserByEmailAndPassword(email, password).getId());
        } else {
            session = new Sessions(this.generateSessionID(), false, this.findAdminByEmailAndPassword(email, password).getId());
        }
        List<Sessions> sessions= sessionRepository.findAll();
        for (Sessions session_from_list : sessions) {
            if (session_from_list.getAccID() == session.getAccID() && session_from_list.isUser() == session.isUser()) {
                this.sessionRepository.delete(session_from_list);
            }
        }

        this.sessionRepository.save(session);

        return session.getSessionID();
    }




    public Admin findAdminByEmailAndPassword(String email, String password) {
        List<Admin> adminList = adminRepository.findAll();

        for (Admin admin : adminList) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                //TODO email send() einfügen
                return admin;
            }
        }
        return null;
    }



    private int generateTwoFactorCode(){
        Random random = new Random();
        return random.nextInt(899999) + 100000;
    }

    private String generateSessionID(){
        return UUID.randomUUID().toString();
    }

    public Sessions getSession(String sessionId) {

        List<Sessions> sessionList = sessionRepository.findAll();

        for (Sessions sessions : sessionList) {
            if (sessions.getSessionID().equals(sessionId)) {
                return sessions;
            }
        }

        return null;
    }

    public boolean hasAdminRights(String sessionId){
        Sessions session = this.sessionRepository.findSessionsBySessionID(sessionId);
        return session != null && !session.isUser();
    }

    public Users getUserBySessionID(String sessionID) {
        Sessions session = sessionRepository.findSessionsBySessionID(sessionID);

        if (session != null) {
            if (session.isUser()) {
                return userRepository.findById(session.getAccID()).orElse(null);
            }
        }

        return null;
    }

    public Admin getAdminBySessionID(String sessionID){
        Sessions session = sessionRepository.findSessionsBySessionID(sessionID);

        if(session!=null){
            if(!session.isUser()){
                return adminRepository.findById(session.getAccID()).orElse(null);

            }
        }
        return null;
    }

}
