//package ch.uzh.ifi.hase.soprafs24.service;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Topic;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import org.springframework.data.jpa.domain.Specification;
//import javax.persistence.criteria.Predicate;
//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//
//@Service
//@Transactional
//public class TopicService {
//    private User currentUser;
////    public TopicService(User user, TopicRepository topicRepository) {
////
////    }
//    private final Logger log = LoggerFactory.getLogger(TopicService.class);
//
//    private final TopicRepository topicRepository;
//
//    @Autowired
//    public TopicService(@Qualifier("topicRepository") TopicRepository topicRepository, User currentUser) {
//        this.topicRepository = topicRepository;
//        System.out.println("111");
//        this.currentUser = currentUser;
//        Topic mensa = createTopic("MENSA", true, 0L);
//        mensa.setContent("In this topic, different mensas in UZH are displayed. You can comment and rate here!");
//        Topic course = createTopic("COURSE", true, 1L);
//        course.setContent("In this topic, different courses in UZH are displayed. You can comment and rate here!");
//        topicRepository.save(mensa);
//        System.out.println("save mensa!");
//        topicRepository.save(course);
//        System.out.println("save course!");
////        topicRepository.flush();
//    }
//
//    public List<Topic> getAllTopics() {return topicRepository.findAll();}
//
//
//    public Topic createTopic(String topicName, Boolean editAllowed, Long currentUserId) {
//        Topic newTopic = new Topic();
//        newTopic.setCreationDate(new Date()); // set creation date
//        newTopic.setTopicName(topicName);
//        newTopic.setOwnerId(currentUser.getUserId()); // set Owner ID
//        newTopic.setEditAllowed(editAllowed);
//        newTopic = topicRepository.save(newTopic); // save Topic，ID from Repository automatically
//        topicRepository.flush();
//        log.debug("Created Information for Topic: {}", newTopic);
//        return newTopic;
//    }
//
//    public Topic getTopicById(Long topicId) {
//        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
//        return topic;
//    }
//
//    public Topic getTopicByName(String topicName) {
//        return topicRepository.findByTopicName(topicName);
//    }
//
//    public Topic getTopicsByOwner(User user) {
//        return topicRepository.findByOwnerId(user.getUserId());
//    }
//
//    public Topic updateTopic(Topic topic, String topicName, boolean editAllowed) {
//        if (topic.getOwnerId() == currentUser.getUserId()) {
//            topic.setTopicName(topicName);
//            topic.setEditAllowed(editAllowed);
//            topic = topicRepository.save(topic);
//            log.debug("Updated Information for Topic: {}", topic);
//            return topic;
//        } else {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this topic");
//        }
//    }
//
//    public void deleteTopic(Topic topic) {
//        if (topic.getOwnerId() == currentUser.getUserId()) {
//            topicRepository.delete(topic);
//            log.debug("Deleted Topic: {}", topic);
//        }
//        else {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this topic");
//        }
//    }
//
//    public Topic searchTopic(String topicName) {
//        return topicRepository.findByTopicName(topicName);
//    }
//    public List<Topic> searchTopics(String keyword) {
//        return topicRepository.searchByKeyword(keyword);
//    }
//
//    public List<Topic> filterTopics(String name, Boolean editAllowed) {
//        return topicRepository.findAll((Specification<Topic>) (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (name != null) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("topicName")), "%" + name.toLowerCase() + "%"));
//            }
//            if (editAllowed != null) {
//                predicates.add(criteriaBuilder.equal(root.get("editAllowed"), editAllowed));
//            }
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        });
//    }
//
//    public List<Topic> getMostPopularTopics() {
//        return topicRepository.findMostPopularTopics();
//    }
//
//    public List<Topic> getTopicsByFirstLetter(String prefix) {
//        return topicRepository.findByFirstLetter(prefix);
//    }
//
//}
package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(@Qualifier("topicRepository") TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
        System.out.println("111");
        Topic mensa = createTopic("MENSA", true, 1L);
        mensa.setTopicIntroduction("In this topic, different mensas in UZH are displayed. You can comment and rate here!");
        Topic course = createTopic("COURSE", true, 2L);
        course.setTopicIntroduction("In this topic, different courses in UZH are displayed. You can comment and rate here!");
        topicRepository.save(mensa);
        System.out.println("save mensa!");
        topicRepository.save(course);
        System.out.println("save course!");
//        topicRepository.flush();
    }

    public List<Topic> getAllTopics() {return topicRepository.findAll();}


    public Topic createTopic(String topicName, Boolean editAllowed, Long currentUserId) {
        Topic newTopic = new Topic();
        newTopic.setCreationDate(new Date()); // set creation date
        newTopic.setTopicName(topicName);
//        newTopic.setOwnerId(currentUser.getUserId()); // set Owner ID
        newTopic.setEditAllowed(editAllowed);
        newTopic = topicRepository.save(newTopic); // save Topic，ID from Repository automatically
        topicRepository.flush();
        log.debug("Created Information for Topic: {}", newTopic);
        return newTopic;
    }

    public Topic getTopicById(Long topicId) {
        if(topicRepository.findByTopicId(topicId) != null) {
            return topicRepository.findByTopicId(topicId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
    }

    public Topic getTopicByName(String topicName) {
        return topicRepository.findByTopicName(topicName);
    }

    public Topic getTopicsByOwner(User user) {
        return topicRepository.findByOwnerId(user.getUserId());
    }

//    public Topic updateTopic(Topic topic, String topicName, boolean editAllowed) {
//        if (topic.getOwnerId() == currentUser.getUserId()) {
//            topic.setTopicName(topicName);
//            topic.setEditAllowed(editAllowed);
//            topic = topicRepository.save(topic);
//            log.debug("Updated Information for Topic: {}", topic);
//            return topic;
//        } else {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this topic");
//        }
//    }

//    public void deleteTopic(Topic topic) {
//        if (topic.getOwnerId() == currentUser.getUserId()) {
//            topicRepository.delete(topic);
//            log.debug("Deleted Topic: {}", topic);
//        }
//        else {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this topic");
//        }
//    }

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
