package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;

import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class TopicController {

    private final TopicService topicService;

    @Autowired
    TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/topics")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TopicGetDTO createTopic(@RequestBody TopicPostDTO topicPostDTO) {

        Topic topicInput = new Topic();

        BeanUtils.copyProperties(topicPostDTO, topicInput);
      
        Topic createdTopic = topicService.createTopic(topicInput);

        return DTOMapper.INSTANCE.convertEntityToTopicGetDTO(createdTopic);
    }

    @GetMapping("/topics")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TopicGetDTO> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        List<TopicGetDTO> topicGetDTOs = new ArrayList<>();

        for (Topic topic : topics) {
            topicGetDTOs.add(DTOMapper.INSTANCE.convertEntityToTopicGetDTO(topic));
        }
        return topicGetDTOs;
    }

    @GetMapping("/topics/topicId/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TopicGetDTO getTopicById(@PathVariable int topicId) {
        Topic topic = topicService.getTopicById(topicId);
        TopicGetDTO topicGetDTO = DTOMapper.INSTANCE.convertEntityToTopicGetDTO(topic);
        return topicGetDTO;
    }

    @GetMapping("/topics/topicName/{topicName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TopicGetDTO getTopicByTopicName(@PathVariable String topicName) {
        Topic topic = topicService.getTopicByTopicName(topicName);
        TopicGetDTO topicGetDTO = DTOMapper.INSTANCE.convertEntityToTopicGetDTO(topic);
        return topicGetDTO;
    }

//    @GetMapping("/topics/Owner/{OwnerId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public TopicGetDTO getTopicByOwnerId(@PathVariable int ownerId) {
//        List<Topic> topics = topicService.getTopicByOwnerId(ownerId);
//        if (topics.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
//        }
//        Topic topic = topics.get(0);
//        TopicGetDTO topicGetDTO = DTOMapper.INSTANCE.convertEntityToTopicGetDTO(topic);
//        return topicGetDTO;
//    }

    @GetMapping("/topics/search")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<TopicGetDTO>> searchTopics(@RequestParam String keyword) {
        List<Topic> topics = topicService.searchTopics(keyword);
        List<TopicGetDTO> topicGetDTOs = topics.stream()
                .map(DTOMapper.INSTANCE::convertEntityToTopicGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topicGetDTOs);
    }


    @GetMapping("/topics/popular")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<TopicGetDTO>> getMostPopularTopics() {
        List<Topic> topics = topicService.getMostPopularTopics();
        List<TopicGetDTO> topicGetDTOs = topics.stream()
                .map(DTOMapper.INSTANCE::convertEntityToTopicGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topicGetDTOs);
    }

    @GetMapping("/topics/by-first-letter/{letter}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<TopicGetDTO>> getTopicsByFirstLetter(@PathVariable String letter) {
        if (letter.equals("#")) {
            letter = "^[^a-zA-Z].*";
        }
        List<Topic> topics = topicService.getTopicsByFirstLetter(letter);
        List<TopicGetDTO> topicGetDTOs = topics.stream()
                .map(DTOMapper.INSTANCE::convertEntityToTopicGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topicGetDTOs);
    }

    @PutMapping("/topics/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TopicGetDTO updateTopic(@RequestBody Topic topicInput, @PathVariable("topicId") int topicId) {
        topicInput.setTopicId(topicId);
        // create user
        Topic createdTopic = topicService.updateTopic(topicInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToTopicGetDTO(createdTopic);
    }

    @DeleteMapping("/topics/topicName/{topicName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopicByName(@PathVariable String topicName) {
        topicService.deleteTopicByTopicName(topicName);
    }

    @DeleteMapping("/topics/topicId/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopicById(@PathVariable Integer topicId) {
        topicService.deleteTopicByTopicId(topicId);
    }

    @PostMapping("/topics/initialize")
    public ResponseEntity<Void> initializeTopics() {
        topicService.initializeTopics();
        return ResponseEntity.ok().build();
    }
}
