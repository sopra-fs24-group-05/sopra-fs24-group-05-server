package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FollowItemGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FollowUserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FollowTopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserById(@PathVariable Long userId) {
    User userById=userService.getUserById(userId);
    log.info("In userGetDTO.avatar: {}", DTOMapper.INSTANCE.convertEntityToUserGetDTO(userById).getAvatar());
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userById);
  }

  @GetMapping("/users/{userId}/followUsers")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<FollowUserGetDTO> getFollowUserList(@PathVariable Long userId) {
    List<User> followUsers = userService.getFollowUsers(userId);
    List<FollowUserGetDTO> followUserGetDTOs = new ArrayList<>();
    for(User followUser : followUsers){
      followUserGetDTOs.add(DTOMapper.INSTANCE.converEntityToFollowUserGetDTO(followUser));
    }
    return followUserGetDTOs;
  }

  @GetMapping("/users/{userId}/followItems")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<FollowItemGetDTO> getFollowItemList(@PathVariable Long userId) {
    List<Item> followItems = userService.getFollowItems(userId);
    List<FollowItemGetDTO> followItemGetDTOs = new ArrayList<>();
    for(Item item : followItems){
      followItemGetDTOs.add(DTOMapper.INSTANCE.converEntityToFollowItemGetDTO(item));
    }
    return followItemGetDTOs;
  }

  @PostMapping("/users/registration")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    //createUser will return A User entity with field of: id, username, password, token
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PutMapping("/users/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO){
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User loginUser = userService.loginUser(userInput);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loginUser);
  }

  @PutMapping("/users/{userId}/followUsers")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void followUser(@PathVariable Long userId, @RequestBody Long followUserId){
    userService.followUser(userId, followUserId);
  }

  //frontend should correspondingly delete token
  @PutMapping("/users/logout")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void logoutUser(@RequestBody UserPostDTO userPostDTO){
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    userService.logoutUser(userInput);
  }

  @PutMapping("/users/edit")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void editUser(@RequestBody UserPostDTO userPostDTO, @RequestParam String token){
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    userService.editUser(userInput, token);
  }

  @PutMapping("/users/{userId}/followItems")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void followItem(@PathVariable Long userId, @RequestBody Long followItemId){
    userService.followItem(userId, followItemId);
  }

  @PutMapping("/users/{userId}/followTopics")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void followTopic(@PathVariable Long userId, @RequestBody Long followTopicId){
    userService.followTopic(userId, followTopicId);
  }
  
  @GetMapping("/users/{userId}/followTopics")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<FollowTopicGetDTO> getFollowTopicList(@PathVariable Long userId){
    List<Topic> followTopics = userService.getFollowTopics(userId);
    List<FollowTopicGetDTO> followTopicGetDTOs = new ArrayList<>();
    for(Topic topic : followTopics){
      followTopicGetDTOs.add(DTOMapper.INSTANCE.converEntityToFollowTopicGetDTO(topic));
    }
    return followTopicGetDTOs;
  }

  @PutMapping("/users/editAvatar/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void editAvatar(@PathVariable Long userId, @RequestBody String avatar){
    userService.setAvater(userId, avatar);
  }

  @PutMapping("/admin/banUser/{adminId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void banUser(@PathVariable Long adminId, @RequestBody Long targetId){
    userService.banUser(adminId, targetId);
  }

  @GetMapping("/admin/getBannedList/{adminId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getBannedList(@PathVariable Long adminId){
    List<User> bannedUsers = userService.getAllBannedUsers(adminId);
    List<UserGetDTO> bannedUserGetDTOs = new ArrayList<>();
    for(User bannedUser : bannedUsers){
      bannedUserGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(bannedUser));
    }
    return bannedUserGetDTOs;
  }
}
