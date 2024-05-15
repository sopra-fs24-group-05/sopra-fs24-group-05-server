package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    //userPostDTO.setName("name");
    userPostDTO.setUsername("username");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    //assertEquals(userPostDTO.getName(), user.getName());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    //user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getUserId(), userGetDTO.getUserId());
    //assertEquals(user.getName(), userGetDTO.getName());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
  }

    @Test
    public void testConvertTopicPostDTOtoEntity() {
        TopicPostDTO topicPostDTO = new TopicPostDTO();
        topicPostDTO.setTopicName("Test Topic");
        topicPostDTO.setOwnerId(1);
        topicPostDTO.setEditAllowed(true);
        topicPostDTO.setDescription("Test Description");
        topicPostDTO.setCreationDate(new Date());

        Topic topic = DTOMapper.INSTANCE.convertTopicPostDTOtoEntity(topicPostDTO);

        assertEquals(topicPostDTO.getTopicName(), topic.getTopicName());
        assertEquals(topicPostDTO.getOwnerId(), topic.getOwnerId());
        assertEquals(topicPostDTO.getEditAllowed(), topic.getEditAllowed());
        assertEquals(topicPostDTO.getDescription(), topic.getDescription());
        assertEquals(topicPostDTO.getCreationDate(), topic.getCreationDate());
    }

    @Test
    public void testConvertEntityToTopicGetDTO() {
        Topic topic = new Topic();
        topic.setTopicId(1);
        topic.setTopicName("Test Topic");
        topic.setCreationDate(new Date());
        topic.setOwnerId(1);
        topic.setEditAllowed(true);
        topic.setDescription("Test Description");

        TopicGetDTO topicGetDTO = DTOMapper.INSTANCE.convertEntityToTopicGetDTO(topic);

        assertEquals(topic.getTopicId(), topicGetDTO.getId());
        assertEquals(topic.getTopicName(), topicGetDTO.getTopicName());
        assertEquals(topic.getCreationDate(), topicGetDTO.getCreationDate());
        assertEquals(topic.getOwnerId(), topicGetDTO.getOwnerId());
        assertEquals(topic.getEditAllowed(), topicGetDTO.getEditAllowed());
        assertEquals(topic.getDescription(), topicGetDTO.getDescription());
    }

    @Test
    public void testConvertItemPostDTOtoEntity() {
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setItemName("Test Item");
        itemPostDTO.setContent("Test Content");
        itemPostDTO.setCreationDate(new Date());
        itemPostDTO.setScore(10.0);
        itemPostDTO.setLikes(5);
        itemPostDTO.setTopicId(1);

        Item item = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);

        assertEquals(itemPostDTO.getItemName(), item.getItemName());
        assertEquals(itemPostDTO.getContent(), item.getContent());
        assertEquals(itemPostDTO.getCreationDate(), item.getCreationDate());
        assertEquals(itemPostDTO.getScore(), item.getScore());
        assertEquals(itemPostDTO.getLikes(), item.getLikes());
        assertEquals(itemPostDTO.getTopicId(), item.getTopicId());
    }

    @Test
    public void testConvertEntityToItemGetDTO() {
        Item item = new Item();
        item.setItemId(1L);
        item.setItemName("Test Item");
        item.setContent("Test Content");
        item.setCreationDate(new Date());
        item.setScore(10.0);
        item.setLikes(5);
        item.setTopicId(1);

        ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);

        assertEquals(item.getItemId(), itemGetDTO.getItemId());
        assertEquals(item.getItemName(), itemGetDTO.getItemName());
        assertEquals(item.getContent(), itemGetDTO.getContent());
        assertEquals(item.getCreationDate(), itemGetDTO.getCreationDate());
        assertEquals(item.getScore(), itemGetDTO.getScore());
        assertEquals(item.getLikes(), itemGetDTO.getLikes());
        assertEquals(item.getTopicId(), itemGetDTO.getTopicId());
    }


}
