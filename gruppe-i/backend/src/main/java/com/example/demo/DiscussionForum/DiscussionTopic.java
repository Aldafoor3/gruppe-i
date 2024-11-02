package com.example.demo.DiscussionForum;

import com.example.demo.user.Users;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: ManyToOne (etc.) Annotations korrekt machen

@Getter
@Setter
@Entity
@Transactional
public class DiscussionTopic {

    //TODO: (Add on) Kategorien implementieren --> man sollte zwischen mehreren kategorien auswählen und keinen fließtext schreiben können

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    private String text;
    //private boolean approved; // New field for moderation approval
    @ManyToOne(fetch = FetchType.LAZY)
    private Users creator;

    private Date datum;

    @OneToMany(mappedBy = "topic",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "forumLikedBy",
    joinColumns = @JoinColumn(name = "forum_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> forumLikedBy = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "topic_favorites",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> favorites = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "topic_likes",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> likes = new HashSet<>();
    // Other topic properties, getters, and setters


    public DiscussionTopic(){
        this.datum = new Date();
    }
}

