package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Use H2 database
@Sql(scripts = "/schema.sql")
public class CommentRepositoryIntegrationTest_3 {

    @Autowired
    private CommentRepository commentRepository;

    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

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
        comment.setLikedUserList(new ArrayList<Long>(Arrays.asList(1L,2L,3L)));

        commentRepository.save(comment);
        assertNotNull(comment.getCommentId());
    }

    // Add more test methods for other repository methods

}

