package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ItemPostDTOTest {

    @Test
    public void testAddLike() {
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        assertEquals(0, itemPostDTO.getLikes());

        itemPostDTO.addLike();
        assertEquals(1, itemPostDTO.getLikes());

        itemPostDTO.addLike();
        assertEquals(2, itemPostDTO.getLikes());
    }

    @Test
    public void testIncrementPopularity() {
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        assertEquals(0, itemPostDTO.getPopularity());

        itemPostDTO.incrementPopularity();
        assertEquals(1, itemPostDTO.getPopularity());

        itemPostDTO.incrementPopularity();
        assertEquals(2, itemPostDTO.getPopularity());
    }

    @Test
    public void testGettersAndSetters() {
        ItemPostDTO itemPostDTO = new ItemPostDTO();

        itemPostDTO.setItemId(1L);
        assertEquals(1L, itemPostDTO.getItemId());

        itemPostDTO.setItemName("Test Item");
        assertEquals("Test Item", itemPostDTO.getItemName());

        itemPostDTO.setContent("Test Content");
        assertEquals("Test Content", itemPostDTO.getContent());

        Date now = new Date();
        itemPostDTO.setCreationDate(now);
        assertEquals(now, itemPostDTO.getCreationDate());

        itemPostDTO.setScore(5.0);
        assertEquals(5.0, itemPostDTO.getScore());

        itemPostDTO.setLikes(10);
        assertEquals(10, itemPostDTO.getLikes());

        Topic topic = new Topic();
        itemPostDTO.setTopic(topic);
        assertEquals(topic, itemPostDTO.getTopic());

        itemPostDTO.setTopicId(2);
        assertEquals(2, itemPostDTO.getTopicId());

        itemPostDTO.setPopularity(3);
        assertEquals(3, itemPostDTO.getPopularity());
    }
}
