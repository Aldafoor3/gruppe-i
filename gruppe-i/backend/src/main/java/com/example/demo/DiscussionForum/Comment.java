package com.example.demo.DiscussionForum;

import com.example.demo.user.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//TODO: ManyToOne (etc.) Annotations korrekt machen

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    private Date datum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    private DiscussionTopic topic;
    // other comment properties, getters, and setters

    public Comment(){
        this.datum = new Date();
    }

}
