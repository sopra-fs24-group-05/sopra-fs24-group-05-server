package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class TopicService {
    private User currentUser;

    public TopicService(User user, TopicRepository topicRepository) {
        this.currentUser = user;
        this.topicRepository = topicRepository;
    }
    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {return topicRepository.findAll();}


    public Topic createTopic(String topicName, Boolean editAllowed, Long currentUserId) {
        Topic newTopic = new Topic();
        newTopic.setCreationDate(new Date()); // set creation date
        newTopic.setTopicName(topicName);
        newTopic.setOwnerId(currentUser.getId()); // set Owner ID
        newTopic.setEditAllowed(editAllowed);
        newTopic = topicRepository.save(newTopic); // save Topic，ID from Repository automatically
        topicRepository.flush();
        log.debug("Created Information for Topic: {}", newTopic);
        return newTopic;
    }

    public Topic getTopicById(Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
        return topic;
    }

    public Topic getTopicByName(String topicName) {
        return topicRepository.findByTopicName(topicName);
    }

    public Topic getTopicsByOwner(User user) {
        return topicRepository.findByOwnerId(user.getId());
    }

    public Topic updateTopic(Topic topic, String topicName, boolean editAllowed) {
        if (topic.getOwnerId() == currentUser.getId()) {
            topic.setTopicName(topicName);
            topic.setEditAllowed(editAllowed);
            topic = topicRepository.save(topic);
            log.debug("Updated Information for Topic: {}", topic);
            return topic;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this topic");
        }
    }

    public void deleteTopic(Topic topic) {
        if (topic.getOwnerId() == currentUser.getId()) {
            topicRepository.delete(topic);
            log.debug("Deleted Topic: {}", topic);
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this topic");
        }
    }

    public Topic searchTopic(String topicName) {
        return topicRepository.findByTopicName(topicName);
    }
    public List<Topic> searchTopics(String keyword) {
        return topicRepository.searchByKeyword(keyword);
    }

    public List<Topic> filterTopics(String name, Boolean editAllowed) {
        return topicRepository.findAll((Specification<Topic>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("topicName")), "%" + name.toLowerCase() + "%"));
            }
            if (editAllowed != null) {
                predicates.add(criteriaBuilder.equal(root.get("editAllowed"), editAllowed));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public List<Topic> getMostPopularTopics() {
        return topicRepository.findMostPopularTopics();
    }

    public List<Topic> getTopicsByFirstLetter(String prefix) {
        return topicRepository.findByFirstLetter(prefix);
    }

}
