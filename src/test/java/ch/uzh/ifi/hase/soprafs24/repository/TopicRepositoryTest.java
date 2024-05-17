package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // disable default H2 database
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicService topicService;

    private Topic topic;

    @BeforeEach
    public void setup() {
        topic = new Topic();
        topic.setTopicName("Test Topic");
        topic.setCreationDate(new Date());
        topic.setOwnerId(1);
        topic.setEditAllowed(true);
        topic.setDescription("This is a test topic");
        topic.setSearchCount(10);
        topicRepository.save(topic);

    }

    @Test
    public void findByTopicName_success() {
        Topic found = topicRepository.findByTopicName("Test Topic");
        assertNotNull(found);
        assertEquals(topic.getTopicName(), found.getTopicName());
    }

    @Test
    public void findByTopicId_success() {
        Topic found = topicRepository.findByTopicId(topic.getTopicId());
        assertNotNull(found);
        assertEquals(topic.getTopicId(), found.getTopicId());
    }

    @Test
    public void findByOwnerId_success() {
        Topic found = topicRepository.findByOwnerId(topic.getOwnerId());
        assertNotNull(found);
        assertEquals(topic.getOwnerId(), found.getOwnerId());
    }

    @Test
    public void searchByKeyword_success() {
        List<Topic> foundTopics = topicRepository.searchByKeyword("Test");
        assertFalse(foundTopics.isEmpty());
        assertEquals(topic.getTopicName(), foundTopics.get(0).getTopicName());
    }

    @Test
    public void findMostPopularTopics_success() {
        // Initialize test data
        Topic topic1 = new Topic();
        topic1.setTopicName("Topic 1");
        topic1.setCreationDate(new Date());
        topic1.setOwnerId(1);
        topic1.setEditAllowed(true);
        topic1.setSearchCount(10000);

        Topic topic2 = new Topic();
        topic2.setTopicName("Topic 2");
        topic2.setCreationDate(new Date());
        topic2.setOwnerId(2);
        topic2.setEditAllowed(true);
        topic2.setSearchCount(20000);

        Topic topic3 = new Topic();
        topic3.setTopicName("Topic 3");
        topic3.setCreationDate(new Date());
        topic3.setOwnerId(3);
        topic3.setEditAllowed(true);
        topic3.setSearchCount(5000);

        topicRepository.save(topic1);
        topicRepository.save(topic2);
        topicRepository.save(topic3);

        // Execute the method to be tested
        List<Topic> popularTopics = topicRepository.findMostPopularTopics();

        // Verify the results
        assertFalse(popularTopics.isEmpty());
        assertEquals(6, popularTopics.size());
        assertEquals("Topic 2", popularTopics.get(0).getTopicName());
        assertEquals("Topic 1", popularTopics.get(1).getTopicName());
        assertEquals("Topic 3", popularTopics.get(2).getTopicName());
    }


    @Test
    public void findByFirstLetter_success() {
        Topic topic2 = new Topic();
        topic2.setTopicName("Another Topic");
        topic2.setCreationDate(new Date());
        topic2.setOwnerId(2);
        topic2.setEditAllowed(true);
        topic2.setDescription("This is another topic");
        topicRepository.save(topic2);

        List<Topic> foundTopics = topicRepository.findByFirstLetter("T");
        assertFalse(foundTopics.isEmpty());
        assertEquals(1, foundTopics.size());
        assertEquals(topic.getTopicName(), foundTopics.get(0).getTopicName());
    }
}
