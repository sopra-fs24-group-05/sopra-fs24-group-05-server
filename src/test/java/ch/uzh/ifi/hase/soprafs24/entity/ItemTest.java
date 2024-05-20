package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemTest {

    @Mock
    private CommentRepository commentRepository;

    private Item item;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item();
    }

    @Test
    public void testGetAndSetItemId() {
        Long itemId = 1L;
        item.setItemId(itemId);
        assertEquals(itemId, item.getItemId());
    }

    @Test
    public void testGetAndSetItemName() {
        String itemName = "Test Item";
        item.setItemName(itemName);
        assertEquals(itemName, item.getItemName());
    }

    @Test
    public void testGetAndSetContent() {
        String content = "This is a test content";
        item.setContent(content);
        assertEquals(content, item.getContent());
    }

    @Test
    public void testGetAndSetCreationDate() {
        Date date = new Date();
        item.setCreationDate(date);
        assertEquals(date, item.getCreationDate());
    }

    @Test
    public void testGetAndSetLikes() {
        int likes = 10;
        item.setLikes(likes);
        assertEquals(likes, item.getLikes());
    }

    @Test
    public void testAddLike() {
        item.setLikes(0);
        item.addLike();
        assertEquals(1, item.getLikes());
    }

    @Test
    public void testGetAndSetScore() {
        double score = 4.5;
        item.setScore(score);
        assertEquals(score, item.getScore());
    }

    @Test
    public void testGetAndSetTopic() {
        Topic topic = new Topic();
        topic.setTopicId(1);
        item.setTopic(topic);
        assertEquals(topic, item.getTopic());
    }

    @Test
    public void testGetAndSetTopicId() {
        int topicId = 1;
        item.setTopicId(topicId);
        assertEquals(topicId, item.getTopicId());
    }

    @Test
    public void testUpdateScore() {
        Long itemId = 1L;
        double averageScore = 4.5;

        item.setItemId(itemId);
        when(commentRepository.calculateAverageScoreByCommentItemId(itemId)).thenReturn(averageScore);

        item.updateScore(commentRepository);

        assertEquals(averageScore, item.getScore());
    }

    @Test
    public void testUpdateScoreWhenNull() {
        Long itemId = 1L;

        item.setItemId(itemId);
        when(commentRepository.calculateAverageScoreByCommentItemId(itemId)).thenReturn(null);

        item.updateScore(commentRepository);

        assertEquals(0.0, item.getScore());
    }
}
