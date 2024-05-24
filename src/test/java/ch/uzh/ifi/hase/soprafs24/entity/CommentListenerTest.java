package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CommentListenerTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CommentListener commentListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleCommentChange_ItemExists() {
        // given
        Comment comment = new Comment();
        comment.setCommentItemId(1L);

        Item item = mock(Item.class);  // Mock the Item object

        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        // when
        commentListener.handleCommentChange(comment);

        // then
        verify(itemRepository, times(1)).findById(anyLong());
        verify(item, times(1)).updateScore(commentRepository);  // Now item is a mock, this should work
    }

    @Test
    public void testHandleCommentChange_ItemDoesNotExist() {
        // given
        Comment comment = new Comment();
        comment.setCommentItemId(1L);

        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        // when
        commentListener.handleCommentChange(comment);

        // then
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).save(any());
    }
}
