package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private TopicRepository topicRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private User targetUser;

  private Long validItemId;
  private Long invalidItemId;
  private Long alreadyFollowedItemId;

  private Long validTopicId;
  private Long invalidTopicId;
  private Long alreadyFollowedTopicId;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setUserId(1L);
    //testUser.setName("testName");
    testUser.setUsername("testUsername");
    testUser.setFollowUserList(new ArrayList<>());
    testUser.setFollowItemList(new ArrayList<>());

    targetUser = new User();
    targetUser.setUserId(2L);
    targetUser.setUsername("targetUsername");

    validItemId = 100L;
    invalidItemId = 200L;
    alreadyFollowedItemId = 300L;
    testUser.getFollowItemList().add(alreadyFollowedItemId);

    validTopicId = 100L;
    invalidTopicId = 200L;
    alreadyFollowedTopicId = 300L;
    testUser.getFollowTopicList().add(alreadyFollowedTopicId);

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.existsById(testUser.getUserId())).thenReturn(true);
    Mockito.when(userRepository.existsById(targetUser.getUserId())).thenReturn(true);
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

    Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
    Mockito.when(itemRepository.existsById(validItemId)).thenReturn(true);
    Mockito.when(itemRepository.existsById(invalidItemId)).thenReturn(false);
    Mockito.when(itemRepository.existsById(alreadyFollowedItemId)).thenReturn(true);

    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(topicRepository.existsById(validTopicId)).thenReturn(true);
    when(topicRepository.existsById(invalidTopicId)).thenReturn(false);
    when(topicRepository.existsById(alreadyFollowedTopicId)).thenReturn(true);
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

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.setAvater(1L, avatar);

    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(userRepository, Mockito.times(1)).flush();

    assertEquals(avatar.substring(1,avatar.length()-2), user.getAvatar());
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
  @Test
  public void followUser_userExistsAndNotFollowing_success() {
    // Act
    userService.followUser(1L, 2L);

    // Assert
    verify(userRepository, times(1)).save(testUser);
    assertTrue(testUser.getFollowUserList().contains(2L));
  }

  @Test
  public void followUser_targetUserNotFound_throwsException() {
    // given
    Mockito.when(userRepository.existsById(3L)).thenReturn(false);

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.followUser(1L, 3L);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("target userId not found", exception.getReason());
  }

  @Test
  public void followUser_alreadyFollowing_throwsException() {
    // given
    testUser.getFollowUserList().add(2L);

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.followUser(1L, 2L);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("already followed target user", exception.getReason());
  }

  @Test
  public void followItem_validItem_success() {
    Long validItemId = 100L;
    userService.followItem(testUser.getUserId(), validItemId);

    // Assert
    assertEquals(2, testUser.getFollowItemList().size());
    assertEquals(validItemId, testUser.getFollowItemList().get(1));
    verify(userRepository, times(1)).save(testUser);
    verify(userRepository, times(1)).flush();
  }

  @Test
  public void followItem_userNotFound_throwsException() {
    Long validItemId = 100L;
    Long nonExistentUserId = 2L;
    Mockito.when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
        userService.followItem(nonExistentUserId, validItemId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("userId not found", exception.getReason());
  }

  @Test
  public void followItem_itemNotFound_throwsException() {   
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
        userService.followItem(testUser.getUserId(), invalidItemId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("target item not found", exception.getReason());
  }

  @Test
  public void followItem_alreadyFollowingItem_throwsException() {    
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
        userService.followItem(testUser.getUserId(), alreadyFollowedItemId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("already followed target item", exception.getReason());
  }

  @Test
  public void followTopic_validTopic_success() {
    userService.followTopic(testUser.getUserId(), validTopicId);

    assertEquals(2, testUser.getFollowTopicList().size());
    assertEquals(validTopicId, testUser.getFollowTopicList().get(1));
    verify(userRepository, times(1)).save(testUser);
    verify(userRepository, times(1)).flush();
  }

  @Test
  public void followTopic_userNotFound_throwsException() {
    Long nonExistentUserId = 2L;
    when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.followTopic(nonExistentUserId, validTopicId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("userId not found", exception.getReason());
  }

  @Test
  public void followTopic_topicNotFound_throwsException() {
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.followTopic(testUser.getUserId(), invalidTopicId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("target topic not found", exception.getReason());
  }

  @Test
  public void followTopic_alreadyFollowingTopic_throwsException() {
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.followTopic(testUser.getUserId(), alreadyFollowedTopicId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("already followed target topic", exception.getReason());
  }
}
