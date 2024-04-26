package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
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

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "password", target = "password")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password") //not necessary to send back the password, considering deleting this one
  @Mapping(source = "token", target = "token")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "identity", target = "identity")
  UserGetDTO convertEntityToUserGetDTO(User user);


  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "topicIntroduction", target = "topicIntroduction")
  Topic convertTopicPostDTOtoEntity(TopicPostDTO topicPostDTO);

  @Mapping(source = "topicId", target = "topicId")
  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "fatherTopicId", target = "fatherTopicId")
  @Mapping(source = "sonTopicId", target = "sonTopicId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "topicIntroduction", target = "topicIntroduction")
  TopicGetDTO convertTopicGetDTOtoEntity(Topic topic);

//  @Mapping(source = "itemId", target = "itemId")
  @Mapping(source = "itemname", target = "itemname")
  @Mapping(source = "itemTopicId", target = "itemTopicId")
  @Mapping(source = "itemIntroduction", target = "itemIntroduction")
  Item convertItemPostDTOtoEntity(ItemPostDTO ItemPostDTO);

//  @Mapping(source = "itemId", target = "itemId")
  @Mapping(source = "itemname", target = "itemname")
  @Mapping(source = "itemTopicId", target = "itemTopicId")
  @Mapping(source = "itemIntroduction", target = "itemIntroduction")
  ItemGetDTO convertItemGetDTOtoEntity(Item item);

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "commentOwnerId", target = "commentOwnerId")
  @Mapping(source = "commentOwnerName", target = "commentOwnerName")
  @Mapping(source = "commentItemId", target = "commentItemId")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  Comment convertCommentPostDTOtoEntity(CommentPostDTO commentPostDTO);

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "commentOwnerId", target = "commentOwnerId")
  @Mapping(source = "commentOwnerName", target = "commentOwnerName")
  @Mapping(source = "commentItemId", target = "commentItemId")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  CommentGetDTO converEntityToCommentGetDTO(Comment comment);

}
