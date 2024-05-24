package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ItemGetDTOTest {

    @Test
    public void testAddLike() {
        ItemGetDTO itemGetDTO = new ItemGetDTO();
        assertEquals(0, itemGetDTO.getLikes());

        itemGetDTO.addLike();
        assertEquals(1, itemGetDTO.getLikes());

        itemGetDTO.addLike();
        assertEquals(2, itemGetDTO.getLikes());
    }

    @Test
    public void testIncrementPopularity() {
        ItemGetDTO itemGetDTO = new ItemGetDTO();
        assertEquals(0, itemGetDTO.getPopularity());

        itemGetDTO.incrementPopularity();
        assertEquals(1, itemGetDTO.getPopularity());

        itemGetDTO.incrementPopularity();
        assertEquals(2, itemGetDTO.getPopularity());
    }

    @Test
    public void testGettersAndSetters() {
        ItemGetDTO itemGetDTO = new ItemGetDTO();

        itemGetDTO.setItemId(1L);
        assertEquals(1L, itemGetDTO.getItemId());

        itemGetDTO.setItemName("Test Item");
        assertEquals("Test Item", itemGetDTO.getItemName());

        itemGetDTO.setContent("Test Content");
        assertEquals("Test Content", itemGetDTO.getContent());

        Date now = new Date();
        itemGetDTO.setCreationDate(now);
        assertEquals(now, itemGetDTO.getCreationDate());

        itemGetDTO.setAverageScore(5.0);
        assertEquals(5.0, itemGetDTO.getAverageScore());

        itemGetDTO.setLikes(10);
        assertEquals(10, itemGetDTO.getLikes());

        Topic topic = new Topic();
        itemGetDTO.setTopic(topic);
        assertEquals(topic, itemGetDTO.getTopic());

        itemGetDTO.setTopicId(2);
        assertEquals(2, itemGetDTO.getTopicId());

        itemGetDTO.setPopularity(3);
        assertEquals(3, itemGetDTO.getPopularity());
    }
}
