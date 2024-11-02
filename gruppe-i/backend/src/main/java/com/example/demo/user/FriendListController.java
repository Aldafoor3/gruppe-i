package com.example.demo.user;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/friendListDemo")
public class FriendListController {

    private final FriendListService friendListService;

    @Autowired
    public FriendListController(FriendListService friendListService) {
        this.friendListService = friendListService;
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long receiverId, @RequestParam String sessionID) {
        try {
            friendListService.sendFriendRequest(receiverId, sessionID);

            return ResponseEntity.ok("Friend request sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/acceptRequest")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long receiverId, @RequestParam String sessionID) {
        try {
            friendListService.acceptFriendRequest(receiverId, sessionID);
            return ResponseEntity.ok("Friend request accepted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/declineRequest")
    public ResponseEntity<String> declineFriendRequest(@RequestParam Long receiverId, @RequestParam String sessionID) {
        try {
            friendListService.declineFriendRequest(receiverId, sessionID);
            return ResponseEntity.ok("Friend request declined successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    @GetMapping("/{sessionID}/friendList")
    public ResponseEntity<List<Users>> getFriendList(@PathVariable String sessionID) {
        List<Users> friendList = friendListService.getFriendList(sessionID);
        return ResponseEntity.ok(friendList);
    }

    @GetMapping("/{sessionID}/friendRequests")
    public ResponseEntity<List<Users>> getFriendRequests(@PathVariable String sessionID) {
        List<Users> requestList = friendListService.getFriendRequestList(sessionID);
        return ResponseEntity.ok(requestList);
    }
    */

    @GetMapping("/{sessionID}/getLists")
    public ResponseEntity<Map<String, List<CroppedUser>>> getUsers(@PathVariable String sessionID) {
        try {
            Map<String, List<CroppedUser>> allFriendLists = friendListService.getAllLists(sessionID);
            System.out.println(allFriendLists + "\n##########################----- ENDE -----##########################");

            return new ResponseEntity<>(allFriendLists, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{sessionID}/getFriendListOfProfile/{profileID}")
    public ResponseEntity<Map<String,List<CroppedUser>>> getProfileFriendList(@PathVariable String sessionID, @PathVariable Long profileID){
        List<CroppedUser> friendList = this.friendListService.getProfileFriendList(sessionID,profileID);

        Map<String,List<CroppedUser>> friendListHashMap = new HashMap<>();

        if(friendList == null) return new ResponseEntity<>(friendListHashMap, HttpStatus.BAD_REQUEST);
        friendListHashMap.put("friendList", friendList);
        return new ResponseEntity<>(friendListHashMap,HttpStatus.OK);
    }
}

