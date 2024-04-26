package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

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

  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserById(@PathVariable Long id) {
    User userById=userService.getUserById(id);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userById);
  }

//  @GetMapping("/users/{userId}/followItems")
//  @ResponseStatus(HttpStatus.OK)
//  @ResponseBody
//  public ResponseEntity<List<Item>> getFollowItemList(@PathVariable Long userId) {
//      List<Item> followItems = userService.getFollowItems(userId);
//      return ResponseEntity.ok(followItems);
//  }

  @GetMapping("/users/{userId}/followUsers")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<List<User>> getFollowUserList(@PathVariable Long userId) {
      List<User> followUsers = userService.getFollowUsers(userId);
      return ResponseEntity.ok(followUsers);
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

  @PutMapping("/users/follow/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void followUser(@PathVariable Long userId, @RequestBody String followUserId){
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

}
