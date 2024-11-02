package com.example.demo.DiscussionForum;

import com.example.demo.admin.Admin;
import com.example.demo.email.EmailService;
import com.example.demo.login.LoginService;
import com.example.demo.user.CroppedUser;
import com.example.demo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiscussionService {

    @Autowired
    private DiscussionTopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmailService emailService ;


    @Autowired
    private LoginService loginService;
    //TODO: Kontrolle, ob die Discussionsforen tatsächlich existieren (anhand der Id)
    public DiscussionService() {
    }

    public boolean deleteComment(Long commentId, String sessionId) {
        Admin admin = loginService.getAdminBySessionID(sessionId);
        if (admin == null || !commentRepository.existsById(commentId)) return false;
        Comment comment = commentRepository.findById(commentId).get();
        String commentContent = comment.getContent();
        Users commenter = comment.getCommenter();
        String commenterEmail = commenter.getEmail();
        commentRepository.deleteById(commentId);
        sendEmailNotificationToUser(commenterEmail, "Comment Deleted", "Your comment has been deleted.");
        sendEmailNotificationToTopicFollowers(comment.getTopic().getId(), "A comment has been deleted:\n\n" + commentContent);
        return true;
    }

    public boolean deleteTopic(Long topicId, String sessionId){
        if(loginService.getAdminBySessionID(sessionId) == null || !topicRepository.existsById(topicId)) return false;
        topicRepository.deleteById(topicId);
        return true;
    }


    public void createTopic(String topic, String category,String text, String sessionId) {
        if(loginService.getUserBySessionID(sessionId) == null) return;
        DiscussionTopic newTopic = new DiscussionTopic();
        newTopic.setCreator(loginService.getUserBySessionID(sessionId));
        newTopic.setTitle(topic);
        newTopic.setCategory( category);
        newTopic.setText(text);
        topicRepository.save(newTopic);
    }


    //TODO: senden des neuen Kommentares per Email an alle Leute, die diese Diskussion favorisiert haben
    public void createComment(String content, Long topicId, String sessionId) {
        if(loginService.getUserBySessionID(sessionId) == null || topicRepository.findById(topicId).isEmpty()) return;
        Comment newComment = new Comment();
        newComment.setCommenter(loginService.getUserBySessionID(sessionId));
        newComment.setContent(content);
        newComment.setTopic(topicRepository.findById(topicId).get());
        commentRepository.save(newComment);
        sendEmailNotificationToTopicFollowers(topicId, content);
    }



    public void favoriteTopic(Long topicId, String sessionId) {
        Users user = loginService.getUserBySessionID(sessionId);
        if (user != null && topicRepository.existsById(topicId)) {
            DiscussionTopic topic = topicRepository.findById(topicId).get();
            topic.getFavorites().add(user);
            topicRepository.save(topic);
            sendEmailNotificationToUser(user.getEmail(), "New favorite", "You have favorited a topic.");
        }
    }

    public int likeTopic(Long topicId, String sessionId) {
        Users user = loginService.getUserBySessionID(sessionId);
        if (user != null && topicRepository.existsById(topicId)) {
            DiscussionTopic topic = topicRepository.findById(topicId).get();
            topic.getLikes().add(user);
            topicRepository.save(topic);
            sendEmailNotificationToUser(user.getEmail(), "Topic Liked", "You have liked a topic.");
            return topic.getLikes().size();
        }
        return  0;
    }

    private void sendEmailNotificationToTopicFollowers(Long topicId, String commentContent) {
        DiscussionTopic topic = topicRepository.findById(topicId).orElse(null);
        if (topic != null) {
            List<String> followerEmails = topic.getFavorites().stream()
                    .map(Users::getEmail)
                    .collect(Collectors.toList());

            String subject = "New comment in topic #" + topicId;
            String content = "A new comment has been added:\n\n" + commentContent;

            for (String email : followerEmails) {
                emailService.sendEmailNotification(email, subject, content);
            }
        }
    }

    private void sendEmailNotificationToUser(String userEmail, String subject, String content) {
        emailService.sendEmailNotification(userEmail, subject, content);
    }

    @Transactional
    public DiscussionTopic getTopicById(Long topicId) {
        return topicRepository.findById(topicId).orElse(null);
    }

    @Transactional
    public List<croppedDiscussionTopic> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(discussionTopic -> {
                    return new croppedDiscussionTopic(
                            discussionTopic.getId(),
                            discussionTopic.getTitle(),
                            discussionTopic.getCategory(),
                            new CroppedUser(discussionTopic.getCreator().getId(),
                                    discussionTopic.getCreator().getFirstname(),
                                    discussionTopic.getCreator().getLastname(),
                                    discussionTopic.getCreator().getEmail()),
                            discussionTopic.getText(),
                            discussionTopic.getDatum()
                    );
                }).collect(Collectors.toList());
    }




}

