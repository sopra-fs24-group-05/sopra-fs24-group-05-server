package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicPostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 * 
 * we can re-wrap the fields we need to send back to the client with DTO
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")

  @Mapping(target = "token", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password") //not necessary to send back the password, considering deleting this one
  @Mapping(source = "token", target = "token")
  @Mapping(source = "status", target = "status")
  UserGetDTO convertEntityToUserGetDTO(User user);


  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(target = "chatPool", ignore = true)
  @Mapping(target = "items", ignore = true)
  Topic convertTopicPostDTOtoEntity(TopicPostDTO topicPostDTO);

  @Mapping(source = "topicId" , target = "id")
  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "description", target = "description")
  TopicGetDTO convertEntityToTopicGetDTO(Topic topic);


  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "likes", target = "likes")
  @Mapping(source = "topicId", target = "topicId")
  Item convertItemPostDTOtoEntity(ItemPostDTO itemPostDTO);  

  @Mapping(source = "itemId" , target = "itemId")
  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "likes", target = "likes")
  @Mapping(source = "topicId", target = "topicId")
  ItemGetDTO convertEntityToItemGetDTO(Item item);
  

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "itemId", target = "itemId")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  Comment convertCommentPostDTOtoEntity(CommentPostDTO commentPostDTO);

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "itemId", target = "itemId")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  CommentGetDTO converEntityToCommentGetDTO(Comment comment);

}
