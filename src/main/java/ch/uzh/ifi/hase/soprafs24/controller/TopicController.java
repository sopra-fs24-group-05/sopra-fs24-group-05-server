package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;

import ch.uzh.ifi.hase.soprafs24.service.TopicService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }


    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody Topic newTopic) {
        Long currentUserId = getCurrentUserId();
        Topic topic = topicService.createTopic(newTopic.getTopicName(), newTopic.getEditAllowed(), currentUserId);
        return ResponseEntity.ok(topic);
    }

    private Long getCurrentUserId() {
        return 1L;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Topic>> searchTopics(@RequestParam String keyword) {
        List<Topic> topics = topicService.searchTopics(keyword);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Topic>> filterTopics(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean editAllowed) {
        List<Topic> topics = topicService.filterTopics(name, editAllowed);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Topic>> getMostPopularTopics() {
        List<Topic> topics = topicService.getMostPopularTopics();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/by-first-letter/{letter}")
    public ResponseEntity<List<Topic>> getTopicsByFirstLetter(@PathVariable String letter) {
        if (letter.equals("#")) {
            letter = "^[^a-zA-Z].*";  // Regex for non-alphabetical starts
        }
        List<Topic> topics = topicService.getTopicsByFirstLetter(letter);
        return ResponseEntity.ok(topics);
    }

}
