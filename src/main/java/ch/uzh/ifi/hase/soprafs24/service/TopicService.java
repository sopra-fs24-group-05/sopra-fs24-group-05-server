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
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicGetDTO;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class TopicService {
    private User currentUser;

    public TopicService(User user, TopicRepository topicRepository) {
        this.currentUser = user;
        this.topicRepository = topicRepository;
        Topic mensa = new Topic();
        mensa.setTopicName("MENSA");
        mensa.setEditAllowed(true);
        mensa.setTopicId(1);
        mensa.setDescription("In this topic, different mensas in UZH are displayed. You can comment and rate here!");

        Topic course = new Topic();
        course.setTopicName("COURSE");
        course.setEditAllowed(true);
        course.setTopicId(2);

        course.setDescription("In this topic, different courses in UZH are displayed. You can comment and rate here!");

        topicRepository.save(mensa);
        topicRepository.save(course);
    }
    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(@Qualifier("topicRepository")TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {return topicRepository.findAll();}


    public Topic createTopic(Topic newTopic) {

        newTopic.setCreationDate(new Date());

        checkIfTopicExists(newTopic);

        newTopic = topicRepository.save(newTopic); // 保存话题
        topicRepository.flush();

        log.debug("Created Information for Topic: {}", newTopic);
        return newTopic;
    }

    private void checkIfTopicExists(Topic topicToBeCreated) {
        Topic topicByTopicName = topicRepository.findByTopicName(topicToBeCreated.getTopicName());
        String baseErrorMessage = "The %s provided %s not unique. Therefore, the topic could not be created!";
     if (topicByTopicName != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "add Topic failed because topicName already exists");
        }
    }


    public Topic getTopicById(int topicId) {
        Topic topic = topicRepository.findByTopicId(topicId);
        topic.setSearchCount(topic.getSearchCount() + 1);
        return topic;
    }

    public Topic getTopicByTopicName(String topicName) {
        Topic topic = topicRepository.findByTopicName(topicName);
        topic.setSearchCount(topic.getSearchCount() + 1);
        return topic;
    }
    public Topic getTopicByOwnerId(int ownerId) {
        Topic topic = topicRepository.findByOwnerId(ownerId);
        return topic;
    }

    public Topic updateTopic(Topic topicInput) {
        Topic topic = topicRepository.findById(Long.valueOf(topicInput.getTopicId())).get();
        if(topic == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Topic with TopicId was not found");
        }
        return topicRepository.save(topicInput);
    }


    public void deleteTopicByTopicName(String topicName) {
        Topic topic = topicRepository.findByTopicName(topicName);
        if (topic == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        else {
            topicRepository.delete(topic);
        /*}
        if (topic.getOwnerId().equals(userService.getCurrentUser().getId())) {
            topicRepository.delete(topic);
            log.debug("Deleted Topic: {}", topic);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this topic");
        }*/
            //Need a method from userService!
        }
    }
    public void deleteTopicByTopicId(Integer topicId) {
        if (!topicRepository.existsById(Long.valueOf(topicId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        topicRepository.deleteById(Long.valueOf(topicId));
    }

    public List<Topic> searchTopics(String keyword) {
        List<Topic> topics = topicRepository.searchByKeyword(keyword);
        List<Integer> ids = topics.stream().map(Topic::getTopicId).collect(Collectors.toList());
        topicRepository.incrementSearchCount(ids);
        return topics;
    }

/*    public List<Topic> filterTopics(String name, Boolean editAllowed) {
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
    }*/

    public List<Topic> getMostPopularTopics() {
        return topicRepository.findMostPopularTopics();
    }

    public List<Topic> getTopicsByFirstLetter(String prefix) {
        return topicRepository.findByFirstLetter(prefix);
    }

}
