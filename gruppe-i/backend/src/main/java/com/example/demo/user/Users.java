    package com.example.demo.user;

    import com.example.demo.DiscussionForum.Comment;
    import com.example.demo.DiscussionForum.DiscussionTopic;
    import com.example.demo.profile.DatasetSettings;
    import jakarta.persistence.*;
    import jakarta.transaction.Transactional;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Transactional
    @Getter
    @Setter
    public class Users {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "firstname")
        private String firstname;

        @Column(name = "lastname")
        private String lastname;

        @Column(name = "email")
        private String email;

        @Column(name = "password")
        private String password;

        @Column(name = "birthday")
        private String birthday;

        @Lob
        @Column(name = "profilePicture")
        private byte[] profilePicture; //for testing

        @Column(name = "favouriteDatasetId")
        private Long favouriteDatasetId;

        @Column(name = "friendListPrivacy")
        private Privacy friendListPrivacy;

        @Column(name = "profilePrivacy")
        private Privacy profilePrivacy;

        @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinTable(name = "profileDatasetsSettings")
        private List<DatasetSettings> profileDatasetsSettings = new ArrayList<>();

        //private Image profileImage;

        //for Authentification

        //for Authorization
        @Column(name = "twoFactorCode")
        private int twoFactorCode;

        private int[] chatList;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_friends",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "friend_id"))
        private Set<Users> friendList = new HashSet<>();


        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_friend_request",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "friend_id"))
        private Set<Users> recievedFriendRequest = new HashSet<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "sended_friend_requests",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "friend_id"))
        private Set<Users> sendedFriendRequests = new HashSet<>();

        //Maybe not necessary
        @OneToMany(mappedBy = "commenter",
        cascade = CascadeType.ALL)
        private Set<Comment> ownCommments = new HashSet<>();

        @OneToMany(mappedBy = "creator",
        cascade = CascadeType.ALL)
        private Set<DiscussionTopic> ownDiscussionTopics = new HashSet<>();

        @ManyToMany(mappedBy = "forumLikedBy")
        private Set<DiscussionTopic> likedForums = new HashSet<>();

        private String profileBackgroundColor;

        @Column(name="nyanCounter")
        private Long nyanCounter;

        public Users(String firstname, String lastname, String email, String password, String birthday){
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.password = password;
            this.birthday = birthday;
            //damit das profilbild nie leer ist.
            this.profilePicture = new byte[]{
                    (byte) 0x05
            };
            this.favouriteDatasetId = -1L;
            this.chatList = new int[]{};
            this.friendListPrivacy = Privacy.OPEN;
            this.profilePrivacy = Privacy.OPEN;

            this.profileBackgroundColor = "#23887f";

            this.nyanCounter = 0L;
        }

        public Users() {

        }



    }

