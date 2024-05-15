package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "ch.uzh.ifi.hase.soprafs24", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ItemRepository.class, TopicRepository.class}))
public class ItemRepositoryTest {

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
}
