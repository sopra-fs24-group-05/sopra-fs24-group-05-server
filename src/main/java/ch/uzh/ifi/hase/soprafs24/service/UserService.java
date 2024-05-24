package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 * 
 * @Service marks a Java class as a service class, which holds business logic. 
 * It's a specialization of @Component, and it indicates that the class holds business logic. 
 * Usually, it is used to define service-layer classes.
 * 
 * @Transactional is used to declare that a method or class should be executed within a transactional context. 
 * It means that if anything goes wrong during the method execution, 
 * all changes made to the database within that method will be rolled back, preventing partial updates.
 * 
 * @Autowired is used to automatically wire beans in the Spring container. 
 * It can be used to autowire bean on the setter method, a constructor, a property, 
 * or methods with arbitrary names and/or multiple arguments.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final TopicRepository topicRepository;

  //
  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("itemRepository") ItemRepository itemRepository, @Qualifier("topicRepository") TopicRepository topicRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.topicRepository = topicRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUserById(Long id){
    if(!userRepository.existsById(id)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User Id not found");
    }
    log.info("get!");
    return userRepository.findById(id).get();
  }

  /**
   * This is a method that help to handle request to create a new user in the database
   * It will check the uniqueness of username and throw error if input is not valid
   * @param newUser
   * @throws org.springframework.web.server.ResponseStatusException
   * @return User
   * @see User
   * @see UserRepository
   */
  public User createUser(User newUser) throws ResponseStatusException{
    if(userRepository.existsByUsername(newUser.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
    }
    if (newUser.getToken() != null) {
        switch (newUser.getToken()) {
            case "teacher123":
                newUser.setIdentity(UserIdentity.TEACHER);
                break;
            case "admin123":
                newUser.setIdentity(UserIdentity.ADMIN);
                break;
        }
    } else {
        newUser.setIdentity(UserIdentity.STUDENT);
    }
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);
    newUser.setCreateDate(new Date());
    newUser.setAvatar("data:image/jpeg;base64,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");
    //default
    //newUser.setIdentity(UserIdentity.STUDENT);
    
    // saves the given entity but data is only persisted in the database once
    // flush() is called, used to synchronize any changes made to entities managed by persistence context with the underlying database.
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a method that help to handle login request. It is used in endpoint that handle put request
   * always make sure update is made to the right entity(primary key)
   * here the return of findByUsername() include primary key: Long id
   * It will check the uniqueness of username and throw error if input is not valid
   * @param loginUser
   * @throws org.springframework.web.server.ResponseStatusException
   * @return User
   * @see User
   * @see UserRepository
   */
  public User loginUser(User loginUser) throws ResponseStatusException{
    User userByUsername = userRepository.findByUsername(loginUser.getUsername());
    if(userByUsername == null){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username not found");
    }else if(!loginUser.getPassword().equals(userByUsername.getPassword())){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"incorrect password");
    }else if(userByUsername.getIdentity() == UserIdentity.BANNED){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"user have been banned");
    }else if(userByUsername.getStatus() == UserStatus.ONLINE){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user already login");
    }

    userByUsername.setStatus(UserStatus.ONLINE);
    userRepository.saveAndFlush(userByUsername);
    return userByUsername;
  }

  public void followUser(Long userId, Long followUserId){
    User currentUser = userRepository.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"));
    if(!userRepository.existsById(followUserId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "target userId not found");
    }
    List<Long> followUserList = currentUser.getFollowUserList();
    if(followUserList.contains(followUserId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target user");
    }
    followUserList.add(followUserId);
    currentUser.setFollowUserList(followUserList);
    userRepository.save(currentUser);
    userRepository.flush();
  }

  public void followItem(Long userId, Long newFollowedItemId) {
    User currentUser = userRepository.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"));
    if(!itemRepository.existsById(newFollowedItemId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "target item not found");
    }
    List<Long> followItemList = currentUser.getFollowItemList();
    if(followItemList.contains(newFollowedItemId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target item");
    }
    followItemList.add(newFollowedItemId);
    currentUser.setFollowItemList(followItemList);
    userRepository.save(currentUser);
    userRepository.flush();
  }

  public void setAvater(Long userId, String avatar){
    User editUser=userRepository.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found"));
    editUser.setAvatar(avatar.substring(1,avatar.length()-2));
    userRepository.save(editUser);
    userRepository.flush();
  }

  public List<User> getFollowUsers(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    List<Long> userIdList = user.getFollowUserList();
    List<User> userList = new ArrayList<>();
    for (Long id : userIdList) {
      User userToBeAdd = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
      userList.add(userToBeAdd);
    }
    return userList;
  }

  public List<Item> getFollowItems(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    List<Long> itemIdList = user.getFollowItemList();
    List<Item> itemList = new ArrayList<>();
    for (Long itemId : itemIdList) {
      if(!itemRepository.findByItemId(itemId).equals(null)){
        Item itemToBeAdd = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("item not found"));
        if(!topicRepository.findByTopicId(itemToBeAdd.getTopicId()).equals(null)){
          itemList.add(itemToBeAdd);
        }
      }
    }
    return itemList;
  }

  public List<Topic> getFollowTopics(Long userId){
    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    List<Long> topicIdList = user.getFollowTopicList();
    List<Topic> topicList = new ArrayList<>();
    for(Long topicId : topicIdList){
      if(!topicRepository.findByTopicId(topicId.intValue()).equals(null)){
        topicList.add(topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("topic not found")));
      }
    }
    return topicList;
  }

  public void followTopic(Long userId, Long newFollowedTopicId){
    User currentUser = userRepository.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"));
    if(!topicRepository.existsById(newFollowedTopicId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "target topic not found");
    }
    List<Long> followTopicList = currentUser.getFollowTopicList();
    if(followTopicList.contains(newFollowedTopicId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target topic");
    }
    followTopicList.add(newFollowedTopicId);
    currentUser.setFollowTopicList(followTopicList);
    userRepository.save(currentUser);
    userRepository.flush();
  }

  /**
   * This method is similar to loginUser but do the oppsite job
   * invalid input need to be handle is also different
   * @param logoutUser
   * @throws org.springframework.web.server.ResponseStatusException
   * @return User
   * @see User loginUser(User logingUser)
   * 
   * toDO: needs to specify which field should be used to logout target user, id? username? token? 
   * 
   */
  public void logoutUser(User logoutUser) throws ResponseStatusException{
    User userByToken = userRepository.findByToken(logoutUser.getToken());
    if(userByToken == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username not found");
    }

    userByToken.setStatus(UserStatus.OFFLINE);
    userRepository.saveAndFlush(userByToken);
  }

  /**
   * This is a helper method that handle the request to edit specific users files
   * 
   * @param editUser
   * @param token use to judge whether the user are editing their own file
   * @throws org.springframework.web.server.ResponseStatusException
   */
  public void editUser(User editUser, String token){
    if(!userRepository.existsById(editUser.getUserId())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
    }
    User targetUser = userRepository.findById(editUser.getUserId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found"));
    if(userRepository.existsByUsername(editUser.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
    }else if(!targetUser.getToken().equals(token)){ //existence of target user has been checked before(first if)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"access deny");
    }
    if(editUser.getUsername()!=null){
      targetUser.setUsername(editUser.getUsername());
    }
    if(editUser.getPassword()!=null){
      targetUser.setPassword(editUser.getPassword());
    }

    userRepository.saveAndFlush(targetUser);
    return;
  }

  public void banUser(Long adminId, Long targetId){
    User admin = userRepository.findById(adminId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "admin not found"));
    if(admin.getIdentity()!=UserIdentity.ADMIN){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not authorized");
    }
    User targetUser = userRepository.findById(targetId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "target not found"));
    targetUser.setIdentity(UserIdentity.BANNED);
    userRepository.save(targetUser);
    userRepository.flush();
  }

  public void unblockUser(Long adminId, Long targetId){
    User admin = userRepository.findById(adminId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "admin not found"));
    if(admin.getIdentity()!=UserIdentity.ADMIN){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not authorized");
    }
    User targetUser = userRepository.findById(targetId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "target not found"));
    targetUser.setIdentity(UserIdentity.STUDENT);
    userRepository.save(targetUser);
    userRepository.flush();
  }

  public List<User> getAllBannedUsers(Long adminId){
    User admin = userRepository.findById(adminId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "admin not found"));
    if(admin.getIdentity()!=UserIdentity.ADMIN){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not authorized");
    }
    return userRepository.findByIdentity(UserIdentity.BANNED);
  }
}
