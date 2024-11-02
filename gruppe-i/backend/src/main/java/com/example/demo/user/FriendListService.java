    package com.example.demo.user;

    import com.example.demo.email.EmailService;
    import com.example.demo.login.LoginService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.*;
    import java.util.stream.Collectors;

    //TODO: Change Friendlist Privacy

    @Service
    @Transactional
    public class FriendListService {

        private final UserRepository userRepository;
        private final EmailService emailService;

        private final LoginService loginService;

        @Autowired
        public FriendListService(UserRepository userRepository, EmailService emailService, LoginService loginService) {
            this.userRepository = userRepository;
            this.emailService = emailService;
            this.loginService = loginService;
        }

        public boolean isNull(Users user){
            return user == null;
        }

        @Transactional
        public void sendFriendRequest(Long receiverId, String sessionID) {//TODO: Boolean --> Erfolgreiche Anfrage oder nicht
            Users sender = loginService.getUserBySessionID(sessionID);
            Users receiver = userRepository.findById(receiverId).orElse(null);

            if (sender == null || receiver == null) {
                // Ungültige Benutzer, handle den Fehler entsprechend
                return;
            }

            System.out.println("#-#-#-#-#\nSenderID: " + sender.getId() + "\nReceiverID: " + receiver.getId() + "\n#-#-#-#-#");

            if (!receiver.getRecievedFriendRequest().contains(sender)) {
                receiver.getRecievedFriendRequest().add(sender);
                userRepository.save(receiver);
                System.out.println("!$!$!$!$!$!$! Added Sender to Reciever List !$!$!$!$!$!$!");
            }


            if (!sender.getSendedFriendRequests().contains(receiver)) {
                sender.getSendedFriendRequests().add(receiver);
                userRepository.save(sender);
                System.out.println("!$!$!$!$!$!$! Added Reciever to Sender List !$!$!$!$!$!$!");
            }

            emailService.sendEmail(receiver.getEmail(),
                    "neue FA", "Du hast eine neue FA");



            System.out.println("#####################################################################################");
            System.out.println("Sender:"+sender.getEmail());
            System.out.println("All sent Requests:");
            loginService.getUserBySessionID(sessionID).getSendedFriendRequests().forEach(users -> System.out.println(users.getEmail()+";"));
            System.out.println("Receiver:"+receiver.getEmail());
            System.out.println("All received Requests:");
            Objects.requireNonNull(userRepository.findById(receiverId).orElse(null)).getRecievedFriendRequest().forEach(users -> System.out.println(users.getEmail()+";"));
            System.out.println("#####################################################################################");
        }

        public void acceptFriendRequest(Long receiverId, String sessionID) {//TODO: Boolean --> Erfolgreiche Anfrage oder nicht
            Users Sender = loginService.getUserBySessionID(sessionID);
            Users Reciever = userRepository.findById(receiverId).orElse(null);

            if(isNull(Sender) || isNull(Reciever)) return;

            assert Reciever != null;

            if (Sender.getRecievedFriendRequest().contains(Reciever) && Reciever.getSendedFriendRequests().contains(Sender)) {

                Sender.getFriendList().add(Reciever);
                Sender.getRecievedFriendRequest().remove(Reciever);
                userRepository.save(Sender);
                Reciever.getFriendList().add(Sender);
                Reciever.getSendedFriendRequests().remove(Sender);
                userRepository.save(Reciever);
            }

        }

        public void declineFriendRequest(Long receiverId, String sessionID) { //TODO: Boolean --> Erfolgreiche Anfrage oder nicht
            Users Sender = loginService.getUserBySessionID(sessionID);
            Users Reciever = userRepository.findById(receiverId).orElse(null);

            if(isNull(Sender) || isNull(Reciever)) return;

            assert Reciever != null;

            if (Sender.getRecievedFriendRequest().contains(Reciever)) {
                if (Reciever.getSendedFriendRequests().contains(Sender)) {

                    Sender.getRecievedFriendRequest().remove(Reciever);
                    userRepository.save(Sender);
                    Reciever.getSendedFriendRequests().remove(Sender);
                    userRepository.save(Reciever);
                }
            }
        }

        public List<CroppedUser> getFriendList(String sessionID) {
            Users sender = loginService.getUserBySessionID(sessionID);
            List<CroppedUser> friendList = sender.getFriendList().stream()
                    .map(user -> {
                        CroppedUser croppedUser = new CroppedUser();
                        croppedUser.setId(user.getId());
                        croppedUser.setFirstname(user.getFirstname());
                        croppedUser.setLastname(user.getLastname());
                        croppedUser.setEmail(user.getEmail());
                        return croppedUser;
                    })
                    .collect(Collectors.toList());

            return friendList;
        }

        public List<CroppedUser> getFriendRequestList(String sessionID) {
            Users sender = loginService.getUserBySessionID(sessionID);
            List<CroppedUser> friendRequestList = sender.getRecievedFriendRequest().stream()
                    .map(user -> {
                        CroppedUser croppedUser = new CroppedUser();
                        croppedUser.setId(user.getId());
                        croppedUser.setFirstname(user.getFirstname());
                        croppedUser.setLastname(user.getLastname());
                        croppedUser.setEmail(user.getEmail());
                        return croppedUser;
                    })
                    .collect(Collectors.toList());

            return friendRequestList;
        }

        @Transactional
        public Map<String, List<CroppedUser>> getAllLists(String sessionID) {

            Users userWithSessionId = loginService.getUserBySessionID(sessionID);

            System.out.println("##########\n SessionID: " + sessionID + "\nPerson: " + userWithSessionId.getFirstname() + userWithSessionId.getLastname() + "\n##########");

            // Die 3 Listen
            List<CroppedUser> friendRequestList = getFriendRequestList(sessionID); //Liste von Freundschaftsanfragen

            List<CroppedUser> friendList = getFriendList(sessionID); //Liste von Freunden
            //if(user.getId() != userWithSessionId.getId()) ;
            List<CroppedUser> userList = userRepository.findAll().stream() //Liste von leuten, an welche eine Freundschaftsanfrage gesendet werden kann
                    .filter(user -> !user.getId().equals(userWithSessionId.getId()))
                    .filter(user -> !userWithSessionId.getFriendList().contains(user))
                    .map(user -> {
                            CroppedUser croppedUser = new CroppedUser();
                            croppedUser.setId(user.getId());
                            croppedUser.setFirstname(user.getFirstname());
                            croppedUser.setLastname(user.getLastname());
                            croppedUser.setEmail(user.getEmail());
                            return croppedUser;

                    })
                    .collect(Collectors.toList());

            for (CroppedUser user : friendRequestList) {
                Long userId = user.getId();
                System.out.println("1List: " + userId);
            }
            System.out.println(":_:_:_:_: 1 Liste fertig :_:_:_:_:");
            for (CroppedUser user : friendList) {
                Long userId = user.getId();
                System.out.println("2List: " + userId);
            }
            System.out.println(":_:_:_:_: 2 Liste fertig :_:_:_:_:");
            for (CroppedUser user : userList) {
                Long userId = user.getId();
                System.out.println("3List: " + userId);
            }
            System.out.println(":_:_:_:_: 3 Liste fertig :_:_:_:_:");

            // Füge Listen einem Objekt hinzu
            Map<String, List<CroppedUser>> userLists = new HashMap<>();
            userLists.put("friendRequestList", new ArrayList<CroppedUser>(friendRequestList));
            userLists.put("friendList", new ArrayList<CroppedUser>(friendList));
            userLists.put("userList", new ArrayList<CroppedUser>(userList)); // Kopiere userList in eine neue ArrayList
            System.out.println(":_:_:_:_: Map Erstellt :_:_:_:_:");

            return userLists;
        }

        @Transactional
        public List<CroppedUser> getProfileFriendList(String sessionId, Long profileId){
            if(this.loginService.getUserBySessionID(sessionId) == null) return null;
            if(this.userRepository.findById(profileId).isEmpty()
                    || this.userRepository.findById(profileId).get().getFriendListPrivacy() == Privacy.PRIVATE  ) return null;

            return this.userRepository.findById(profileId).get().getFriendList().stream().map(user -> {
                CroppedUser croppedUser = new CroppedUser();
                croppedUser.setId(user.getId());
                croppedUser.setFirstname(user.getFirstname());
                croppedUser.setLastname(user.getLastname());
                croppedUser.setEmail(user.getEmail());
                return croppedUser;
            }).collect(Collectors.toList());
        }
    }
