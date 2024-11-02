package com.example.demo.testChat;

import com.example.demo.login.LoginService;
import com.example.demo.login.SessionRepository;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ChatService {
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private ChatRoom_Repository chatRoom_Repository;
   @Autowired
   private SessionRepository sessionRepository;
   @Autowired
   private Message_Repository message_Repository;

   @Autowired
   private LoginService loginService;


   public List<Message_Instance> fetchChatRoomBySessionId(String sessionId, Long chatRoomId){
      Users user = loginService.getUserBySessionID(sessionId);
      if (user == null) return null;
      Optional<ChatRoom_Instance> chatroom = chatRoom_Repository.findById(chatRoomId);
      if (chatroom.isEmpty()) return null;
      List<Message_Instance> messageListFromChatRoom = message_Repository.findByChatRoomIdOrderByMessageId(chatRoomId);
      for(int i = 0;i < messageListFromChatRoom.size();i++){
         if(user.getId() != messageListFromChatRoom.get(i).getSenderId()){
            Message_Instance message = messageListFromChatRoom.get(i);
            message.setRead(true);
            messageListFromChatRoom.get(i).setRead(true);
            message_Repository.save(message);
         }
      }
      return messageListFromChatRoom;
   }

   public boolean addNewMsg(String sessionId, Long chatRoomId, String msg){
      Users user = loginService.getUserBySessionID(sessionId);
      if (user == null) return false;
      Optional<ChatRoom_Instance> chatroom = chatRoom_Repository.findById(chatRoomId);
      if (chatroom.isEmpty()) return false;
      if(chatroom.get().isUserIdpresent((user.getId().intValue()))){
         Message_Instance messageInstance = new Message_Instance();
         messageInstance.setMsg(msg);
         messageInstance.setSenderId(user.getId());
         messageInstance.setChatRoomId(chatRoomId);
         messageInstance.setEmail(user.getEmail());
         messageInstance.setFirstName(user.getFirstname());
         messageInstance.setTime(new Date());
         message_Repository.save(messageInstance);
         System.out.println("New Message saved.");
         return true;
      }
      return false;

   }

   public List<ChatRoom_Instance> getChatRoomsByUser(String sessionId){
      if(loginService.getUserBySessionID(sessionId)==null) return null;
      int[] userChatRoomList_IDS = loginService.getUserBySessionID(sessionId).getChatList();
      List<ChatRoom_Instance> userChatRoomList = chatRoom_Repository.findAll();
      for(int i = 0; i < userChatRoomList.size();i++){
         boolean deleteFromList = true;
         for (int x = 0; x<userChatRoomList_IDS.length;x++){
            if(!deleteFromList || userChatRoomList_IDS[x] == userChatRoomList.get(i).getChatRoomId()) deleteFromList = false;
            else continue;
         }
         if(deleteFromList){
            userChatRoomList.remove(i);
            i--;
         }
      }

      for (int x = 0;x<userChatRoomList.size();x++){
         System.out.println(Arrays.toString(userChatRoomList.get(x).getParticipantList()));
      }
      return userChatRoomList;
   }

   public boolean createChatRoom(String chatName, int[] participants, String sessionId){
      //Überprüfen, ob alle eingetragenen account Ids tatsächlich existieren
      if(!userRepository.existsById(sessionRepository.findSessionsBySessionID(sessionId).getAccID())) return false;
      for (int i = 0;i<participants.length; i++){
         if(!userRepository.existsById((long) participants[i])){
            return false;
         }
      }

      ChatRoom_Instance chatRoom = new ChatRoom_Instance();

      int creatorId = sessionRepository.findSessionsBySessionID(sessionId).getAccID().intValue();
      boolean addCreator = true; //Mag sein, dass der Creator sein Account im Frontend bereits in die Liste der
      //Teilnehmer eingetragen hat
      for (int i=0;i<participants.length;i++) {
         if (participants[i] == creatorId) addCreator = false;
      }

      int[] participantsList = addCreator?new int[participants.length+1]:new int[participants.length];
      for (int i=0;i<participants.length;i++){
         participantsList[i] = participants[i];
      }

      if(addCreator) participantsList[participants.length] = creatorId;

      chatRoom.setChatName(chatName);
      chatRoom.setParticipantList(participantsList);
      chatRoom_Repository.save(chatRoom);

      for (int i = 0; i<participantsList.length;i++){
         Optional<Users> user = userRepository.findById((long)participantsList[i]);
         int[] chatList = user.get().getChatList();
         int[] updatedChatList = new int[chatList.length+1];

         for (int x=0;x<chatList.length;x++){
            updatedChatList[x] = chatList[x];
         }
         updatedChatList[chatList.length] = chatRoom.getChatRoomId().intValue();
         user.get().setChatList(updatedChatList);
         userRepository.save(user.get());
      }

      return true;
   }

    public boolean editMessage(Long messageId, String sessionId, String newMessage) {
        Users user = loginService.getUserBySessionID(sessionId);
        if (user == null) return false;
        Optional<Message_Instance> messageOptional = message_Repository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message_Instance message = messageOptional.get();
            if (message.getSenderId() != user.getId() || message.isRead() == true) {
                return false;
            }

         // Check recipient and other validations if needed
         // ...

         // Update the message
         message.setMsg(newMessage);
         message_Repository.save(message);

         return true;
      }
      return false ;
   }

    public boolean deleteMessage(Long messageId, String sessionId) {
        Users user = loginService.getUserBySessionID(sessionId);
        if (user == null) return false;
        Optional<Message_Instance> messageOptional = message_Repository.findById(messageId);
        Message_Instance message = messageOptional.get();
        if (message.getSenderId() != user.getId() || message.isRead() == true) {
            return false;
        }
        // Perform additional business logic or validation
        // ...

      // Delete the message
      message_Repository.deleteById(messageId);

      return true;
   }




}
