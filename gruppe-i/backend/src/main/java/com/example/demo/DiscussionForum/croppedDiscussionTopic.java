package com.example.demo.DiscussionForum;

import com.example.demo.user.CroppedUser;
import com.example.demo.user.Users;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class croppedDiscussionTopic {

    public croppedDiscussionTopic(Long id, String title, String category, CroppedUser creator, String text, Date datum){
        this.id = id;
        this.category = category;
        this.title = title;
        this.creator = creator;
        this.text = text;
        this.datum = datum;
    }

    private Long id;

    private String title;

    private String category;

    private String text;

    //private boolean approved; // New field for moderation approval

    private CroppedUser creator;

    private Date datum;
}
