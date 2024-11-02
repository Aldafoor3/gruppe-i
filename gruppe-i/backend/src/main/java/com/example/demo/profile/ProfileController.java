package com.example.demo.profile;

import com.example.demo.login.LoginService;
import com.example.demo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService loginService;

    @Transactional
    @GetMapping("/getAccBySessionId/{sessionId}")
    public ResponseEntity<Long> getAccountIdBySessionId(@PathVariable("sessionId") String sessionId){
        Users user = loginService.getUserBySessionID(sessionId);

        if(user == null) return new ResponseEntity<Long>((long) (-1), HttpStatus.EXPECTATION_FAILED);

        return new ResponseEntity<Long>(user.getId(),HttpStatus.OK);

    }

    @Transactional
    @GetMapping("/getPrivacySettings/{profileID}/{sessionID}")
    public ResponseEntity<Object> getPrivacySettings(@PathVariable("profileID") Long profileID,
                                              @PathVariable("sessionID") String sessionID) {

            Map<String,Object> profilePrivacySettings = profileService.getProfilePrivacySettings(profileID, sessionID);
            if(profilePrivacySettings == null)       return new ResponseEntity<>(new HashMap<>(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(profilePrivacySettings, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{profileID}/{sessionID}")
    public ResponseEntity<Object> getProfile(@PathVariable("profileID") Long profileID,
                                              @PathVariable("sessionID") String sessionID) {
        if(profileService.myProfileOrPublic(profileID, sessionID)) {
            Map<String,Object> profile = profileService.getProfile(profileID, sessionID);
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Privates Profil");
        }

    }

    @PostMapping("/giveNyan/{id}")
    public ResponseEntity<String>  giveNyan(@PathVariable Long id) {

        if (profileService.giveNyan(id)) {
            return ResponseEntity.ok().body("Nyan given");
        } else {
            return ResponseEntity.badRequest().body("Could not give Nyan");
        }
    }

    @PostMapping("/setColor/{sessionID}")
    public ResponseEntity<String> setProfileBackgroundColor(@RequestBody String color, @PathVariable String sessionID){
        if(this.loginService.getUserBySessionID(sessionID) == null) return new ResponseEntity<String>("session expired", HttpStatus.BAD_REQUEST);
        this.profileService.setColor(sessionID,color);
        return new ResponseEntity<String>("color changed", HttpStatus.OK);
    }

    @PostMapping("/{profileID}/changeDatasets")
    private ResponseEntity<String> setDatasetSettings() {
        //TODO: CHANGE DATA
        return ResponseEntity.ok().body("Changed");
    }


    @PostMapping("/changePrivacy")
    public ResponseEntity<String> changePrivacySetting(@RequestParam Long profileID,
                                                       @RequestParam String sessionID,
                                                       @RequestParam boolean privacySetting) {
        System.out.println("Get privacy change request");
        if (profileService.changePrivacySetting(profileID, sessionID, privacySetting)) {
            return ResponseEntity.ok().body("Changed");
        } else {
            return ResponseEntity.badRequest().body("Couldnt Change");
        }
    }

    @PostMapping("/changeFriendlistPrivacy")
    public ResponseEntity<String> changeFriendlistPrivacySetting(@RequestParam Long profileID,
                                                       @RequestParam String sessionID,
                                                       @RequestParam boolean privacySetting) {
        System.out.println("Get friendlist privacy change request");
        if (profileService.changeFriendlistPrivacySetting(profileID, sessionID, privacySetting)) {
            return ResponseEntity.ok().body("Changed");
        } else {
            return ResponseEntity.badRequest().body("Couldnt Change");
        }
    }

    @PostMapping("/changeUserData")
    public ResponseEntity<String> changeUserData(@RequestParam String sessionid,
                                                 @RequestParam Long profileID,
                                                 @RequestParam String firstname,
                                                 @RequestParam String lastname,
                                                 @RequestParam String email,
                                                 @RequestParam String birthday,
                                                 @RequestParam String newPassword,
                                                 @RequestParam String newPasswordRepeat) {
        System.out.println("Got A profile change request from user:" + firstname + " " + lastname );
        if(profileService.changeProfileSettings(sessionid,profileID,firstname,lastname,email,birthday,newPassword,newPasswordRepeat)) {
            return ResponseEntity.ok().body("Changed");
        } else {
            return ResponseEntity.badRequest().body("Not Changed");
        }

    }
}
