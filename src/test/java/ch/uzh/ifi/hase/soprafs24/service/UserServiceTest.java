package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setUserId(1L);
    //testUser.setName("testName");
    testUser.setUsername("testUsername");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getUserId(), createdUser.getUserId());
    //assertEquals(testUser.getName(), createdUser.getName());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
  }  

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    //Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
    //模拟了在createUser中，如果userRepository.findByUsername被调用了，将会返回一个testUser
    //但是由于creatUser中判断username是否重复的方法改为了用existByUsername，返回一个布尔值，这个测试案例也需要相应的修改
    Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);
    //Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void setAvater_validInput_success(){
    Long userId = 1L;
    String avatar = "this is a test image text";
    User user = new User();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.setAvater(1L, avatar);

    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(userRepository, Mockito.times(1)).flush();

    assertEquals(avatar, user.getAvater());
  }

  @Test
  public void setAvater_userIdNotFound_throwsException() {
      Long userId = 1L;
      String avatar = "this is a image text";

      //Mockito.when(userRepository.existsById(userId)).thenReturn(false);
      Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
      //userService.setAvater(userId, avatar);
      assertThrows(ResponseStatusException.class, ()->userService.setAvater(userId, avatar));
      verify(userRepository, never()).save(any(User.class));

  }

}
