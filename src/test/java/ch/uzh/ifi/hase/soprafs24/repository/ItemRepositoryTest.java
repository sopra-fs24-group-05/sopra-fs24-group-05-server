package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // disable default H2 database
@ComponentScan(basePackages = "ch.uzh.ifi.hase.soprafs24", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ItemRepository.class, TopicRepository.class}))
public class ItemRepositoryTest {

    @MockBean
    private ServerEndpointExporter serverEndpointExporter; // Mock ServerEndpointExporter to avoid loading WebSocket configuration

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Topic topic;
    private Item item;

    @BeforeEach
    public void setup() {
        topic = new Topic();
        topic.setTopicName("Test Topic");
        topic.setCreationDate(new Date());
        topic.setOwnerId(1);
        topic.setEditAllowed(true);
        topic.setDescription("This is a test topic");
        topicRepository.save(topic);

        item = new Item();
        item.setItemName("Test Item");
        item.setContent("Test Content");
        item.setCreationDate(new Date());
        item.setLikes(10);
        item.setScore(5.0);
        item.setTopicId(topic.getTopicId());
        item.setPopularity(100);
        itemRepository.save(item);
    }

    @Test
    public void findByItemName_success() {
        Item found = itemRepository.findByItemName("Test Item");
        assertNotNull(found);
        assertEquals(item.getItemName(), found.getItemName());
    }

    @Test
    public void findByItemId_success() {
        Item found = itemRepository.findByItemId(item.getItemId());
        assertNotNull(found);
        assertEquals(item.getItemId(), found.getItemId());
    }

    @Test
    public void findByCustomQuery_success() {
        Item found = itemRepository.findByCustomQuery(item.getItemId());
        assertNotNull(found);
        assertEquals(item.getItemId(), found.getItemId());
    }

    @Test
    public void findByTopicIdOrderByScoreDesc_success() {
        Item item2 = new Item();
        item2.setItemName("Another Item");
        item2.setContent("Another Content");
        item2.setCreationDate(new Date());
        item2.setLikes(20);
        item2.setScore(10.0);
        item2.setTopicId(topic.getTopicId()); // 转换为Long
        item2.setPopularity(200);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findByTopicIdOrderByScoreDesc(topic.getTopicId()); // 转换为Long
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(item2.getItemName(), items.get(0).getItemName());
    }

    @Test
    public void findByTopicId_success() {
        List<Item> items = itemRepository.findByTopicId(topic.getTopicId());
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getItemName(), items.get(0).getItemName());
    }

    @Test
    public void findByTopicName_success() {
        List<Item> items = itemRepository.findByTopicName("Test Topic");
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getItemName(), items.get(0).getItemName());
    }

    @Test
    public void findByKeyword_success() {
        List<Item> found = itemRepository.findByKeyword("Test");
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(item -> item.getItemName().contains("Test")));
    }

    @Test
    public void incrementPopularity_success() {
        Item item = itemRepository.findByItemName("Test Item");
        int initialPopularity = item.getPopularity();

        item.incrementPopularity();
        itemRepository.save(item);

        Item updatedItem = itemRepository.findByItemName("Test Item");
        assertEquals(initialPopularity + 1, updatedItem.getPopularity());
    }

    @Test
    public void findAllByOrderByPopularityDesc_success() {
        List<Item> items = itemRepository.findAllByOrderByPopularityDesc();
        assertNotNull(items);
        assertTrue(items.size() > 1);

        for (int i = 0; i < items.size() - 1; i++) {
            assertTrue(items.get(i).getPopularity() >= items.get(i + 1).getPopularity());
        }
    }
}
