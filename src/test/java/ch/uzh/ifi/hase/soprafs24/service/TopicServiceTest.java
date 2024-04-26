package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    private Topic testTopic;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testTopic = new Topic();
        testTopic.setTopicId(1);
        testTopic.setTopicName("testTopic");
        testTopic.setOwnerId(1);
        testTopic.setCreationDate(new Date());
        testTopic.setDescription("testDescription");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(topicRepository.save(Mockito.any())).thenReturn(testTopic);
    }
    @Test
    public void createTopic_validInputs_success() {

        Topic createdTopic = topicService.createTopic(testTopic);

        // then
        Mockito.verify(topicRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testTopic.getTopicId(), createdTopic.getTopicId());
        assertEquals(testTopic.getTopicName(), createdTopic.getTopicName());
        assertEquals(testTopic.getOwnerId(), createdTopic.getOwnerId());
        assertEquals(testTopic.getCreationDate(), createdTopic.getCreationDate());
        assertEquals(testTopic.getDescription(), createdTopic.getDescription());
    }

    @Test
    public void createTopic_duplicateName_throwsException() {
        // Arrange
        Mockito.when(topicRepository.findByTopicName("testTopic")).thenReturn(testTopic);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> topicService.createTopic(testTopic));
    }
}