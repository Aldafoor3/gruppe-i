package com.example.demo.DiscussionForum;

import com.example.demo.user.CroppedUser;
import com.example.demo.user.Users;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class CroppedComment {

    private Long id;

    private String content;

    private CroppedUser commenter;

    private Date datum;

}
