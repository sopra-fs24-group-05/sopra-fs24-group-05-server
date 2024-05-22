package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
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
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password") //not necessary to send back the password, considering deleting this one
  @Mapping(source = "token", target = "token")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "identity", target = "identity")
  @Mapping(source = "avatar", target = "avatar")
  UserGetDTO convertEntityToUserGetDTO(User user);


  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "searchCount", target = "searchCount")
  @Mapping(target = "chatPool", ignore = true)
  Topic convertTopicPostDTOtoEntity(TopicPostDTO topicPostDTO);

  @Mapping(source = "topicId" , target = "id")
  @Mapping(source = "topicName", target = "topicName")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "editAllowed", target = "editAllowed")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "searchCount", target = "searchCount")
  TopicGetDTO convertEntityToTopicGetDTO(Topic topic);


  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "likes", target = "likes")
  @Mapping(source = "topicId", target = "topicId")
  @Mapping(source = "popularity", target = "popularity")
  Item convertItemPostDTOtoEntity(ItemPostDTO itemPostDTO);  

  @Mapping(source = "itemId" , target = "itemId")
  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "score", target = "averageScore")
  @Mapping(source = "likes", target = "likes")
  @Mapping(source = "topicId", target = "topicId")
  @Mapping(source = "popularity", target = "popularity")
  ItemGetDTO convertEntityToItemGetDTO(Item item);
  

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "commentOwnerId", target = "commentOwnerId")
  @Mapping(source = "commentOwnerName", target = "commentOwnerName")
  @Mapping(source = "commentItemId", target = "commentItemId")
  @Mapping(source = "score", target = "score")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  @Mapping(source = "fatherCommentId", target = "fatherCommentId")
  Comment convertCommentPostDTOtoEntity(CommentPostDTO commentPostDTO);

  @Mapping(source = "comment.commentId", target = "commentId")
  @Mapping(source = "comment.commentOwnerId", target = "commentOwnerId")
  @Mapping(source = "comment.commentOwnerName", target = "commentOwnerName")
  @Mapping(source = "comment.commentItemId", target = "commentItemId")
  @Mapping(source = "comment.score", target = "score")
  @Mapping(source = "comment.content", target = "content")
  @Mapping(source = "comment.thumbsUpNum", target = "thumbsUpNum")
  @Mapping(source = "commentOwnerAvatar", target = "commentOwnerAvatar")
  CommentGetDTO converEntityToCommentGetDTO(Comment comment, String commentOwnerAvatar);

  @Mapping(source = "isAlreadyLiked", target = "isAlreadyLiked")
  @Mapping(source = "thumbsUpNum", target = "thumbsUpNum")
  CommentStatusGetDTO converParamToCommentStatusGetDTO(Boolean isAlreadyLiked, Integer thumbsUpNum);

  @Mapping(source = "commentId", target = "commentId")
  @Mapping(source = "commentOwnerId", target = "commentOwnerId")
  @Mapping(source = "commentOwnerName", target = "commentOwnerName")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "fatherCommentId", target = "fatherCommentId")
  ReplyGetDTO converEntityToReplyGetDTO(Comment reply);

  @Mapping(source = "userId", target = "followUserId")
  @Mapping(source = "username", target = "followUsername")
  FollowUserGetDTO converEntityToFollowUserGetDTO(User user);

  @Mapping(source = "itemId", target = "followItemId")
  @Mapping(source = "itemName", target = "followItemname")
  @Mapping(source = "topicId", target = "followItemTopicId")
  FollowItemGetDTO converEntityToFollowItemGetDTO(Item item);

  @Mapping(source = "topicId", target = "followTopicId")
  @Mapping(source = "topicName", target = "followTopicname")
  FollowTopicGetDTO converEntityToFollowTopicGetDTO(Topic topic);

  @Mapping(source = "messageId", target = "messageId")
  @Mapping(source = "content", target = "content")
  @Mapping(source  = "itemId", target = "itemId")
  @Mapping(source = "userId", target = "userId")
  //@Mapping(source = "userAvatar", target = "userAvatar")
  //@Mapping(source = "messageTime", target = "messageTime")
  ChatMessage converMessagePostDTOToChatMessage(MessagePostDTO messagePostDTO);
  
  @Mapping(source = "messageId", target = "messageId")
  @Mapping(source = "content", target = "content")
  @Mapping(source  = "itemId", target = "itemId")
  @Mapping(source = "userId", target = "userId")
  // the following three field will be set when creating message and before saving it to database
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "userAvatar", target = "userAvatar")
  @Mapping(source = "messageTime", target = "messageTime")
  MessageGetDTO converChatMessageToMessageGetDTO(ChatMessage chatMessage);
}
