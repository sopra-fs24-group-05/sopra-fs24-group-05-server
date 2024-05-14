package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addItem_validInput_success() {
        // given
        Item newItem = new Item();
        newItem.setItemName("Test Item");
        newItem.setContent("This is a test item");
        newItem.setCreationDate(new Date());
        newItem.setTopicId(1);

        when(itemRepository.save(any())).thenReturn(newItem);

        // when
        Item createdItem = itemService.addItem(newItem);

        // then
        assertEquals(newItem.getItemName(), createdItem.getItemName());
        assertEquals(newItem.getContent(), createdItem.getContent());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void addItem_duplicateName_throwsException() {
        // given
        Item newItem = new Item();
        newItem.setItemName("Test Item");

        when(itemRepository.findByItemName(anyString())).thenReturn(newItem);

        // when/then
        assertThrows(ResponseStatusException.class, () -> {
            itemService.addItem(newItem);
        });

        verify(itemRepository, never()).save(any());
    }

    @Test
    public void updateItem_validInput_success() {
        // given
        Item itemInput = new Item();
        itemInput.setItemId(1L);
        itemInput.setItemName("Updated Item");
        itemInput.setContent("Updated content");
        itemInput.setCreationDate(new Date());

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemInput));
        when(itemRepository.save(any())).thenReturn(itemInput);

        // when
        Item updatedItem = itemService.updateItem(itemInput);

        // then
        assertEquals(itemInput.getItemName(), updatedItem.getItemName());
        assertEquals(itemInput.getContent(), updatedItem.getContent());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void likeItem_validId_success() {
        // given
        Item item = new Item();
        item.setItemId(1L);
        item.setLikes(0);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // when
        itemService.likeItem(1L);

        // then
        verify(itemRepository, times(1)).save(any());
        assertEquals(1, item.getLikes());
    }

    @Test
    public void getItemsByTopicSortedByScore_validId_success() {
        // given
        Integer topicId = 1;
        Item item1 = new Item();
        item1.setItemName("Item 1");
        item1.setScore(5);
        Item item2 = new Item();
        item2.setItemName("Item 2");
        item2.setScore(10);
        List<Item> items = Arrays.asList(item2, item1);

        when(itemRepository.findByTopicIdOrderByScoreDesc(anyInt())).thenReturn(items);

        // when
        List<Item> foundItems = itemService.getItemsByTopicSortedByScore(topicId);

        // then
        assertEquals(2, foundItems.size());
        assertEquals("Item 2", foundItems.get(0).getItemName());
        assertEquals("Item 1", foundItems.get(1).getItemName());
    }

    @Test
    public void getItemsByTopicId_validId_success() {
        // given
        Integer topicId = 1;
        Item item = new Item();
        item.setItemName("Item 1");

        when(itemRepository.findByTopicId(anyInt())).thenReturn(Collections.singletonList(item));

        // when
        List<Item> foundItems = itemService.getItemsByTopicId(topicId);

        // then
        assertEquals(1, foundItems.size());
        assertEquals("Item 1", foundItems.get(0).getItemName());
    }

    @Test
    public void getItemsByTopicName_validName_success() {
        // given
        String topicName = "Test Topic";
        Item item = new Item();
        item.setItemName("Item 1");

        when(itemRepository.findByTopicName(anyString())).thenReturn(Collections.singletonList(item));

        // when
        List<Item> foundItems = itemService.getItemsByTopicName(topicName);

        // then
        assertEquals(1, foundItems.size());
        assertEquals("Item 1", foundItems.get(0).getItemName());
    }

    @Test
    public void addItemToTopic_validInput_success() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setTopicId(topicId);

        Item item = new Item();
        item.setItemName("Test Item");
        item.setContent("This is a test item");

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic));
        when(itemRepository.save(any())).thenReturn(item);

        // when
        Item addedItem = itemService.addItemToTopic(topicId, item);

        // then
        assertEquals(topic, addedItem.getTopic());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void getItemAverageScore_validId_success() {
        // given
        Long itemId = 1L;
        Double averageScore = 4.5;

        when(commentRepository.calculateAverageScoreByItemId(anyLong())).thenReturn(averageScore);

        // when
        Double foundAverageScore = itemService.getItemAverageScore(itemId);

        // then
        assertEquals(averageScore, foundAverageScore);
        verify(commentRepository, times(1)).calculateAverageScoreByItemId(anyLong());
    }

    @Test
    public void deleteItemByItemName_validName_success() {
        // given
        String itemName = "Test Item";
        Item item = new Item();
        item.setItemName(itemName);

        when(itemRepository.findByItemName(anyString())).thenReturn(item);
        doNothing().when(itemRepository).delete(any());

        // when
        itemService.deleteItemByItemName(itemName);

        // then
        verify(itemRepository, times(1)).findByItemName(anyString());
        verify(itemRepository, times(1)).delete(any());
    }

    @Test
    public void deleteItemByItemId_validId_success() {
        // given
        Long itemId = 1L;

        when(itemRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(itemRepository).deleteById(anyLong());

        // when
        itemService.deleteItemByItemId(itemId);

        // then
        verify(itemRepository, times(1)).existsById(anyLong());
        verify(itemRepository, times(1)).deleteById(anyLong());
    }

}
