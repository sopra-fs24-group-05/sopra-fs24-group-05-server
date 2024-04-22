package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/*
 * M3 implementation in between
 */
/*
 * M3 implementation in between
 */

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

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    userRepository.existsById(newUser.getId());
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }

    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).get();
        return user;
    }

    public User updateUser(User userInput) {
        User user = userRepository.findById(userInput.getId()).get();
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user with userId was not found");
        }
        return userRepository.save(userInput);
    }

    public User login(UserPostDTO userInput) {
        User user = userRepository.findByUsername(userInput.getUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username did not sign up,please signup!");
        }
        if (!userInput.getPassword().equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username or password error!");
        }
        return user;
    }
}
