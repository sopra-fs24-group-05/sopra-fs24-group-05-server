package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CommentRepositoryIntegrationTest_3 {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testSaveComment() {
        Comment comment = new Comment();
        // Set comment properties
        comment.setCommentOwnerId(1L);
        comment.setCommentOwnerName("John");
        comment.setCommentItemId(1L);
        comment.setScore(5L);
        comment.setContent("This is a test comment.");
        comment.setThumbsUpNum(0L);

        commentRepository.save(comment);
        assertNotNull(comment.getCommentId());
    }

    // Add more test methods for other repository methods

}

