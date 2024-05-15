package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // disable default H2 database
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Topic topic;

    @BeforeEach
    public void setup() {
        topic = new Topic();
        topic.setTopicName("Test Topic");
        topic.setCreationDate(new Date());
        topic.setOwnerId(1);
        topic.setEditAllowed(true);
        topic.setDescription("This is a test topic");
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
        Topic topic2 = new Topic();
        topic2.setTopicName("Popular Topic");
        topic2.setCreationDate(new Date());
        topic2.setOwnerId(2);
        topic2.setEditAllowed(true);
        topic2.setDescription("This is a popular topic");
        topicRepository.save(topic2);

        Item item1 = new Item();
        item1.setItemName("Item 1");
        item1.setContent("Content 1");
        item1.setCreationDate(new Date());
        item1.setScore(5.0);
        item1.setLikes(10);
        item1.setTopicId((int) topic.getTopicId().longValue());
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setItemName("Item 2");
        item2.setContent("Content 2");
        item2.setCreationDate(new Date());
        item2.setScore(15.0);
        item2.setLikes(20);
        item2.setTopicId((int) topic2.getTopicId().longValue());
        itemRepository.save(item2);

        List<Topic> popularTopics = topicRepository.findMostPopularTopics();
        assertFalse(popularTopics.isEmpty());

        assertEquals(topic2.getTopicName(), popularTopics.get(0).getTopicName());
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
