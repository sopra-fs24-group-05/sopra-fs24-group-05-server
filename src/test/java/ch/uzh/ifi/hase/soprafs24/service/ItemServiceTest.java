package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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

        when(commentRepository.calculateAverageScoreByCommentItemId(anyLong())).thenReturn(averageScore);

        // when
        Double foundAverageScore = itemService.getItemAverageScore(itemId);

        // then
        assertEquals(averageScore, foundAverageScore);
        verify(commentRepository, times(1)).calculateAverageScoreByCommentItemId(anyLong());
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

    @Test
    public void getItemByItemId_validId_success() {
        // given
        Long itemId = 1L;
        Item item = new Item();
        item.setItemId(itemId);
        item.setItemName("Test Item");

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // when
        Item foundItem = itemService.getItemByItemId(itemId);

        // then
        assertEquals(itemId, foundItem.getItemId());
        assertEquals("Test Item", foundItem.getItemName());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void getItemByItemId_invalidId_throwsException() {
        // given
        Long itemId = 1L;

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            itemService.getItemByItemId(itemId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Item not found", exception.getReason());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    public void deleteItemByItemId_itemNotFound_throwsException() {
        // given
        Long itemId = 1L;
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        // when & then
        assertThrows(ResponseStatusException.class, () -> {
            itemService.deleteItemByItemId(itemId);
        });

        verify(itemRepository, times(1)).existsById(anyLong());
        verify(itemRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void updateItem_itemNotFound_throwsException() {
        // given
        Item itemInput = new Item();
        itemInput.setItemId(1L);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            itemService.updateItem(itemInput);
        });

        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(0)).save(any());
    }


    @Test
    public void likeItem_itemNotFound_throwsException() {
        // given
        Long itemId = 1L;
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            itemService.likeItem(itemId);
        });

        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(0)).save(any());
    }

    @Test
    public void searchItemsByKeyword_validKeyword_success() {
        // given
        String keyword = "Test";
        Item item1 = new Item();
        item1.setItemName("Test Item 1");
        Item item2 = new Item();
        item2.setItemName("Test Item 2");

        List<Item> items = Arrays.asList(item1, item2);

        when(itemRepository.findByKeyword(anyString())).thenReturn(items);

        // when
        List<Item> foundItems = itemService.searchItemsByKeyword(keyword);

        // then
        assertEquals(2, foundItems.size());
        assertEquals("Test Item 1", foundItems.get(0).getItemName());
        assertEquals("Test Item 2", foundItems.get(1).getItemName());
    }

    @Test
    public void getItemsSortedByCommentCountAndTopicId_success() {
        // given
        Integer topicId = 1;

        Comment comment1 = new Comment();
        comment1.setCommentItemId(1L);

        Comment comment2 = new Comment();
        comment2.setCommentItemId(1L);

        Comment comment3 = new Comment();
        comment3.setCommentItemId(2L);

        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setTopicId(topicId);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setTopicId(topicId);

        Item item3 = new Item();  // 新增没有评论的 item
        item3.setItemId(3L);
        item3.setTopicId(topicId);

        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2, comment3));
        when(itemRepository.findByTopicId(topicId)).thenReturn(Arrays.asList(item1, item2, item3));
        when(itemRepository.findAllById(anyList())).thenReturn(Arrays.asList(item1, item2, item3));

        // when
        List<Item> items = itemService.getItemsSortedByCommentCountAndTopicId(topicId);

        // then
        assertEquals(3, items.size());
        assertEquals(1L, items.get(0).getItemId());
        assertEquals(2L, items.get(1).getItemId());
        assertEquals(3L, items.get(2).getItemId());
    }


    @Test
    public void getItemByItemId_incrementsPopularity() {
        Long itemId = 1L;
        Item item = new Item();
        item.setItemId(itemId);
        item.setPopularity(5);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        itemService.getItemByItemId(itemId);

        verify(itemRepository, times(1)).save(item);
        assertEquals(6, item.getPopularity());
    }

    @Test
    public void getItemsSortedByPopularity_success() {
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(itemRepository.findAllByOrderByPopularityDesc()).thenReturn(items);

        List<Item> sortedItems = itemService.getItemsSortedByPopularity();

        assertEquals(items.size(), sortedItems.size());
        verify(itemRepository, times(1)).findAllByOrderByPopularityDesc();
    }

    @Test
    public void getItemsByTopicName_validTopic_success() {
        String topicName = "Test Topic";
        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("Item A");

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("Item B");

        when(itemRepository.findByTopicName(topicName)).thenReturn(Arrays.asList(item1, item2));

        List<Item> items = itemService.getItemsByTopicName(topicName);

        assertEquals(2, items.size());
        verify(itemRepository, times(1)).findByTopicName(topicName);
        // Removed verification of save calls
    }


    @Test
    public void getItemsSortedByCommentCountAndTopicId_noComments() {
        Integer topicId = 1;

        when(commentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Item> items = itemService.getItemsSortedByCommentCountAndTopicId(topicId);

        assertTrue(items.isEmpty());
        verify(commentRepository, times(1)).findAll();
    }
}
