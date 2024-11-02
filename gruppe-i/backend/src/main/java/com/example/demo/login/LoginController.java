package com.example.demo.login;

import com.example.demo.admin.Admin;
import com.example.demo.admin.AdminRepository;
import com.example.demo.user.Users;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//TODO: Umbenennung zu AuthentificationController und Service. Einfügen eines Logouts --> löschen der Session in der Sesssion-Datenbank

@RestController
@RequestMapping(value = "/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private LoginService loginService;

    //TODO: NICHT FERTIG
    @PostMapping("/User/{email}/{password}")
    public ResponseEntity<String> loginUser(@PathVariable("email") String email, @PathVariable("password") String password ) {
        if (this.loginService.findUserByEmailAndPassword(email, password)!=null) {
            loginService.newValidation(email,password,true);
            return new ResponseEntity<>( HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @PostMapping("/User/verify/{email}/{password}/{twoFactor}")
    public ResponseEntity<Map<String, String>> checkTwoFactorUser(@PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("twoFactor") int twoFactor ){
        if(this.loginService.validateUser(email, password, twoFactor)){
            String sessionId = this.loginService.createSession(email, password, true);
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        }
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", "");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/Admin/{email}/{password}")
    public ResponseEntity<String> loginAdmin(@PathVariable("email") String email,@PathVariable("password") String password ) {
        if (this.loginService.findAdminByEmailAndPassword(email, password)!=null) {
            loginService.newValidation(email,password,false);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String sessionId){
        if(this.loginService.logout(sessionId)){
            return new ResponseEntity<>("succesfully logged out!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong. Your Session does not exist." +
                " Either way, you are logged out", HttpStatus.NOT_FOUND);

    }

    @PostMapping("/Admin/verify/{email}/{password}/{twoFactor}")
    public ResponseEntity<Map<String, String>> checkTwoFactorAdmin(@PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("twoFactor") int twoFactor ){
        if(this.loginService.validateAdmin(email, password, twoFactor)){
            String sessionId = this.loginService.createSession(email, password, false);
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        }
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", "");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/checkAuth/{sessionId}")
    public ResponseEntity<String> checkAuth(@PathVariable("sessionId") String sessionId) {
        if (loginService.getSession(sessionId)!=null) {
            return new ResponseEntity<>("User is authenticated", HttpStatus.OK);
        }
            return new ResponseEntity<>("Authentication unsuccessful", HttpStatus.NOT_FOUND);

    }

    @GetMapping("/isUser/{sessionId}")
    public ResponseEntity<Boolean> isUser(@PathVariable("sessionId") String sessionId){
        if(loginService.getSession(sessionId)== null) return new ResponseEntity<Boolean>(true,HttpStatus.EXPECTATION_FAILED);
        return new ResponseEntity<Boolean>(loginService.getSession(sessionId).isUser(),HttpStatus.OK);
    }


}
