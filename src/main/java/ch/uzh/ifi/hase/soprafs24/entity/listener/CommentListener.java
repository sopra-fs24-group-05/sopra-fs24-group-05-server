package ch.uzh.ifi.hase.soprafs24.entity.listener;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
public class CommentListener {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void handleCommentChange(Comment comment) {
        Long itemId = comment.getItemId();
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            item.updateScore(commentRepository);
        }
    }
}
