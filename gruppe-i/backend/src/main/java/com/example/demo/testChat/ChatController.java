package com.example.demo.testChat;

import com.example.demo.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@Controller
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private Message_Repository messageRepository;


    @GetMapping("/getList/{sessionId}")
    public ResponseEntity<List<ChatRoom_Instance>> getChatRoomListFromUser(@PathVariable String sessionId){
        return new ResponseEntity<List<ChatRoom_Instance>>(this.chatService.getChatRoomsByUser(sessionId),HttpStatus.OK);
    }

    @PostMapping("/sendMSG/{chatRoomId}")
    public ResponseEntity<String> sendMsg(@PathVariable Long chatRoomId, @RequestParam String sessionId, @RequestParam String message){
        if(this.chatService.addNewMsg(sessionId,chatRoomId,message)) return new ResponseEntity<>("message sent.",HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.CONFLICT);
    }

    @GetMapping("/fetch/{chatRoomId}/{sessionId}")
    public ResponseEntity<List<Message_Instance>> fetchChatRoom(@PathVariable Long chatRoomId, @PathVariable String sessionId){
        List<Message_Instance> messageList = chatService.fetchChatRoomBySessionId(sessionId,chatRoomId);
        if(messageList!=null) return new ResponseEntity<>(messageList, HttpStatus.OK);
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create/{chatName}/{sessionId}")
    public ResponseEntity<String> createNewChat(@PathVariable String chatName,
                                                @RequestBody int[] participants,
                                                @PathVariable String sessionId) {
        if(this.loginService.getUserBySessionID(sessionId) == null) return new ResponseEntity<>("" +
                "either your Session expired or you are not logged in.", HttpStatus.I_AM_A_TEAPOT);
        if (chatService.createChatRoom(chatName, participants, sessionId)) {
            return new ResponseEntity<>("Chatroom created!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Chatroom could not be created", HttpStatus.I_AM_A_TEAPOT);
    }

    @PostMapping("/editMSG/{messageId}")
    public ResponseEntity<String> editMessage(@PathVariable Long messageId, @RequestParam String sessionId, @RequestParam String newMessage){
        Message_Instance Message = messageRepository.findById(messageId).orElse(null);
        if (Message!=null&&Message.isRead()) {
            return new ResponseEntity<>("Cannot edit the message. Recipient has already read it.", HttpStatus.BAD_REQUEST);
        }
        if(this.chatService.editMessage(messageId,sessionId ,newMessage)) return new ResponseEntity<>("message updated.",HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.CONFLICT);
    }

    @PostMapping("/deleteMSG/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId, @RequestParam String sessionId){
        Message_Instance Message = messageRepository.findById(messageId).orElse(null);
        if (Message!=null && Message.isRead()) {
            return new ResponseEntity<>("Cannot edit the message. Recipient has already read it.", HttpStatus.BAD_REQUEST);
        }
        if(this.chatService.deleteMessage(messageId,sessionId)) return new ResponseEntity<>("message deleted.",HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.CONFLICT);
    }






}
