package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;



import java.util.List;
import java.util.UUID;

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

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUserById(Long id){
    if(!userRepository.existsById(id)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User Id not found");
    }
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
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);

    if(userRepository.existsByUsername(newUser.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
    }else if(userRepository.findByName(newUser.getName())!=null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    //checkIfUserExists(newUser); // original check for the username uniqueness
    
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
    }

    userByUsername.setStatus(UserStatus.ONLINE);
    userRepository.saveAndFlush(userByUsername);
    return userByUsername;
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
    User userByUsername = userRepository.findByUsername(logoutUser.getUsername());
    if(userByUsername == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username not found");
    }

    userByUsername.setStatus(UserStatus.OFFLINE);
    userRepository.saveAndFlush(userByUsername);
  }

  /**
   * This is a helper method that handle the request to edit specific users files
   * 
   * @param editUser
   * @param token use to judge whether the user are editing their own file
   * @throws org.springframework.web.server.ResponseStatusException
   */
  public void editUser(User editUser, String token){
    if(userRepository.existsById(editUser.getId())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
    }else if(userRepository.existsByUsername(editUser.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
    }else if(userRepository.findById(editUser.getId()).get().getToken().equals(token)){ //existence of target user has been checked before(first if)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"access deny");
    }
    User targetUser = userRepository.findById(editUser.getId()).get();
    if(editUser.getUsername()!=null){
      targetUser.setUsername(editUser.getUsername());
    }
    if(editUser.getPassword()!=null){
      targetUser.setPassword(editUser.getPassword());
    }

    userRepository.saveAndFlush(targetUser);
    return;
  }
}
