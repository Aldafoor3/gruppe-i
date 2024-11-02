package com.example.demo.DiscussionForum;

import com.example.demo.user.CroppedUser;
import com.example.demo.user.Users;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*TODO: Boolean wert im service implementieren, sodass man bspw. sagen kann, dass
  TODO: eine neue Discussion eröffnet wurde oder nicht.*/
@RestController
@Transactional
@RequestMapping("/topics")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Setter
public class DiscussionTopicController {
    @Autowired
    private DiscussionService discussionService;


    public DiscussionTopicController() {

    }

    @PostMapping("/{commentId}/deleteComment")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestParam String sessionId) {
        if(discussionService.deleteComment(commentId,sessionId)) return new ResponseEntity<String>("Comment deleted.", HttpStatus.OK);
        return new ResponseEntity<String>("no Authorization.", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/{topicId}/deleteTopic")
    public ResponseEntity<String> deleteTopic(@PathVariable Long topicId, @RequestParam String sessionId) {
        if(discussionService.deleteTopic(topicId,sessionId)) return new ResponseEntity<String>("Topic deleted.", HttpStatus.OK);
        return new ResponseEntity<String>("no Authorization.", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTopic(@RequestParam String topic, @RequestParam String category,@RequestParam String text, @RequestParam String sessionId ) {
        discussionService.createTopic(topic, category,text, sessionId);
        return ResponseEntity.ok("topic created or not created");
    }

    @PostMapping("/{topicId}/createComment")
    public ResponseEntity<String> createComment(@PathVariable Long topicId,@RequestParam String content, @RequestParam String sessionId) {
        discussionService.createComment(content, topicId, sessionId);
        return ResponseEntity.ok("comment created or not created");
    }



    @GetMapping("/getAllTopics")
    public ResponseEntity<List<croppedDiscussionTopic>> getAllTopics() {
        List<croppedDiscussionTopic> topics = discussionService.getAllTopics();
        return ResponseEntity.ok(topics);
    }





    @PostMapping("/{topicId}/favorite")
    public ResponseEntity<String> favoriteTopic(
            @PathVariable Long topicId,
            @RequestParam String sessionId
    ) {
        discussionService.favoriteTopic(topicId, sessionId);
        return ResponseEntity.ok("Topic favorited or not favorited");
    }

    @PostMapping("/{topicId}/like")
    public ResponseEntity<Integer> likeTopic(
            @PathVariable Long topicId,
            @RequestParam String sessionId
    ) {
        int updatedLikes = discussionService.likeTopic(topicId, sessionId);
        return ResponseEntity.ok(updatedLikes);
    }

    @Transactional
    @GetMapping("/{topicId}")
    public ResponseEntity<Map<String, Object>> getTopicById(@PathVariable Long topicId) {
        System.out.println("###########FETCHING DISCUSSION######");
        System.out.println("ID:" + topicId);
        System.out.println("####################################");

        DiscussionTopic topic = discussionService.getTopicById(topicId);

        if (topic == null) return ResponseEntity.notFound().build();

        Users creator = topic.getCreator();

        CroppedUser croppedCreator = new CroppedUser(creator.getId(), creator.getFirstname(), creator.getLastname(), creator.getEmail());

        croppedDiscussionTopic croppedTopic = new croppedDiscussionTopic(topic.getId(), topic.getTitle(),topic.getCategory(), croppedCreator, topic.getText(), topic.getDatum());

        Map<String, Object> discussionData = new HashMap<>();
        discussionData.put("croppedTopic", croppedTopic);

        List<CroppedComment> croppedCommentList = topic.getComments().stream().map(comment -> {
            CroppedComment croppedComment = new CroppedComment();
            croppedComment.setContent(comment.getContent());
            croppedComment.setId(comment.getId());
            croppedComment.setDatum(comment.getDatum());

            Users commenter = comment.getCommenter();
            CroppedUser croppedCommenter = new CroppedUser(commenter.getId(), commenter.getFirstname(), commenter.getLastname(), commenter.getEmail());
            croppedComment.setCommenter(croppedCommenter);

            return croppedComment;
        }).collect(Collectors.toList());

        discussionData.put("commentList", croppedCommentList);


        System.out.println("###########SENDING DISCUSSION######");
        System.out.println("ID:" + topic.getId());
        System.out.println("TITLE:" + topic.getTitle());
        System.out.println("##################");
        return new ResponseEntity<>(discussionData, HttpStatus.OK);
    }
    // Other endpoints as needed
}
