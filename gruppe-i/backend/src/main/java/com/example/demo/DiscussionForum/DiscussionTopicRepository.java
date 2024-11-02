package com.example.demo.DiscussionForum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionTopicRepository extends JpaRepository<DiscussionTopic, Long> {
    // custom queries if needed
}
