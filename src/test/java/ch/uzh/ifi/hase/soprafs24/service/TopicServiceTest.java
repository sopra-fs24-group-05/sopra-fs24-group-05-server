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


import java.util.*;


import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


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
        when(topicRepository.save(Mockito.any())).thenReturn(testTopic);
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
        when(topicRepository.findByTopicName("testTopic")).thenReturn(testTopic);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> topicService.createTopic(testTopic));
    }

    @Test
    public void getAllTopics_success() {
        // given
        Topic topic1 = new Topic();
        topic1.setTopicName("Test Topic 1");
        Topic topic2 = new Topic();
        topic2.setTopicName("Test Topic 2");
        List<Topic> allTopics = Arrays.asList(topic1, topic2);

        when(topicRepository.findAll()).thenReturn(allTopics);

        // when
        List<Topic> topics = topicService.getAllTopics();

        // then
        assertEquals(2, topics.size());
        assertEquals("Test Topic 1", topics.get(0).getTopicName());
        assertEquals("Test Topic 2", topics.get(1).getTopicName());
    }

    @Test
    public void createTopic_validInput_success() {
        // given
        Topic newTopic = new Topic();
        newTopic.setTopicName("Test Topic");
        newTopic.setDescription("This is a test topic");

        when(topicRepository.save(any())).thenReturn(newTopic);

        // when
        Topic createdTopic = topicService.createTopic(newTopic);

        // then
        assertEquals(newTopic.getTopicName(), createdTopic.getTopicName());
        assertEquals(newTopic.getDescription(), createdTopic.getDescription());
        verify(topicRepository, times(1)).save(any());
    }

    @Test
    public void getTopicById_validId_success() {
        // given
        int topicId = 1;
        Topic topic = new Topic();
        topic.setTopicId(topicId);

        when(topicRepository.findByTopicId(anyInt())).thenReturn(topic);

        // when
        Topic foundTopic = topicService.getTopicById(topicId);

        // then
        assertEquals(topicId, foundTopic.getTopicId());
        verify(topicRepository, times(1)).findByTopicId(anyInt());
    }

    @Test
    public void getTopicByTopicName_validName_success() {
        // given
        String topicName = "Test Topic";
        Topic topic = new Topic();
        topic.setTopicName(topicName);

        when(topicRepository.findByTopicName(anyString())).thenReturn(topic);

        // when
        Topic foundTopic = topicService.getTopicByTopicName(topicName);

        // then
        assertEquals(topicName, foundTopic.getTopicName());
        verify(topicRepository, times(1)).findByTopicName(anyString());
    }

    @Test
    public void getTopicByOwnerId_validId_success() {
        // given
        int ownerId = 1;
        Topic topic = new Topic();
        topic.setOwnerId(ownerId);

        when(topicRepository.findByOwnerId(anyInt())).thenReturn(topic);

        // when
        Topic foundTopic = topicService.getTopicByOwnerId(ownerId);

        // then
        assertEquals(ownerId, foundTopic.getOwnerId());
        verify(topicRepository, times(1)).findByOwnerId(anyInt());
    }

    @Test
    public void updateTopic_validInput_success() {
        // given
        Topic topicInput = new Topic();
        topicInput.setTopicId(1);
        topicInput.setTopicName("Updated Topic");
        topicInput.setDescription("Updated description");

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topicInput));
        when(topicRepository.save(any())).thenReturn(topicInput);

        // when
        Topic updatedTopic = topicService.updateTopic(topicInput);

        // then
        assertEquals(topicInput.getTopicName(), updatedTopic.getTopicName());
        assertEquals(topicInput.getDescription(), updatedTopic.getDescription());
        verify(topicRepository, times(1)).save(any());
    }

    @Test
    public void deleteTopicByTopicName_validName_success() {
        // given
        String topicName = "Test Topic";
        Topic topic = new Topic();
        topic.setTopicName(topicName);

        when(topicRepository.findByTopicName(anyString())).thenReturn(topic);
        doNothing().when(topicRepository).delete(any());

        // when
        topicService.deleteTopicByTopicName(topicName);

        // then
        verify(topicRepository, times(1)).findByTopicName(anyString());
        verify(topicRepository, times(1)).delete(any());
    }


    @Test
    public void deleteTopicByTopicId_validId_success() {
        // given
        Integer topicId = 1;

        when(topicRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(topicRepository).deleteById(anyLong());

        // when
        topicService.deleteTopicByTopicId(topicId);

        // then
        verify(topicRepository, times(1)).existsById(anyLong());
        verify(topicRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void searchTopics_validKeyword_success() {
        // given
        String keyword = "Test";
        Topic topic = new Topic();
        topic.setTopicId(1);
        topic.setTopicName("Test Topic");
        List<Topic> topicList = Collections.singletonList(topic);

        when(topicRepository.searchByKeyword(anyString())).thenReturn(topicList);

        // when
        List<Topic> foundTopics = topicService.searchTopics(keyword);

        // then
        assertEquals(1, foundTopics.size());
        assertEquals("Test Topic", foundTopics.get(0).getTopicName());
        verify(topicRepository, times(1)).searchByKeyword(anyString());
        verify(topicRepository, times(1)).incrementSearchCount(anyList());
    }

    @Test
    public void getMostPopularTopics_success() {
        // given
        Topic topic1 = new Topic();
        topic1.setTopicName("Popular Topic 1");
        Topic topic2 = new Topic();
        topic2.setTopicName("Popular Topic 2");

        when(topicRepository.findMostPopularTopics()).thenReturn(Arrays.asList(topic1, topic2));

        // when
        List<Topic> popularTopics = topicService.getMostPopularTopics();

        // then
        assertEquals(2, popularTopics.size());
        assertEquals("Popular Topic 1", popularTopics.get(0).getTopicName());
        assertEquals("Popular Topic 2", popularTopics.get(1).getTopicName());
        verify(topicRepository, times(1)).findMostPopularTopics();
    }

    @Test
    public void getTopicsByFirstLetter_validPrefix_success() {
        // given
        String prefix = "T";
        Topic topic1 = new Topic();
        topic1.setTopicName("Test Topic 1");
        Topic topic2 = new Topic();
        topic2.setTopicName("Test Topic 2");

        when(topicRepository.findByFirstLetter(anyString())).thenReturn(Arrays.asList(topic1, topic2));

        // when
        List<Topic> topics = topicService.getTopicsByFirstLetter(prefix);

        // then
        assertEquals(2, topics.size());
        assertEquals("Test Topic 1", topics.get(0).getTopicName());
        assertEquals("Test Topic 2", topics.get(1).getTopicName());
        verify(topicRepository, times(1)).findByFirstLetter(anyString());
    }

    @Test
    public void initializeTopics_createsTopicsIfNotExist() {
        // given
        when(topicRepository.findByTopicName("MENSA")).thenReturn(null);
        when(topicRepository.findByTopicName("COURSE")).thenReturn(null);

        // when
        topicService.initializeTopics();

        // then
        verify(topicRepository, times(1)).findByTopicName("MENSA");
        verify(topicRepository, times(1)).findByTopicName("COURSE");
        verify(topicRepository, times(2)).save(any(Topic.class));
    }

    @Test
    public void deleteTopicByTopicId_topicNotFound_throwsException() {
        // given
        Integer topicId = 1;
        when(topicRepository.existsById(anyLong())).thenReturn(false);

        // when & then
        assertThrows(ResponseStatusException.class, () -> topicService.deleteTopicByTopicId(topicId));
        verify(topicRepository, times(1)).existsById(anyLong());
    }

    @Test
    public void updateTopic_topicNotFound_throwsException() {
        // given
        Topic topicInput = new Topic();
        topicInput.setTopicId(1);
        when(topicRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class, () -> topicService.updateTopic(topicInput));
        verify(topicRepository, times(1)).findById(anyLong());
    }
}