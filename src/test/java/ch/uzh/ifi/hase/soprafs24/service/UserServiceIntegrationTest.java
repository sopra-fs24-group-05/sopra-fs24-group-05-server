package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.config.WebSocketConfig;
import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @MockBean
  private WebSocketConfig webSocketConfig;

  @Autowired
  private UserService userService;

  private List<User> backUpData;

  private User testUser;

  @BeforeEach
  public void setup() {
    backUpData=userRepository.findAll();
    userRepository.deleteAll();

    testUser = new User();
    testUser.setUsername("firstname@lastname");
    testUser.setPassword("testpassword");
    testUser.setStatus(UserStatus.OFFLINE);
    testUser.setToken("1");
    testUser.setIdentity(UserIdentity.STUDENT);
    testUser.setAvatar("this is a image text");

  }

  @AfterEach
  public void deleteAndRecover(){
    userRepository.deleteAll();
    userRepository.saveAll(backUpData);
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getUserId(), createdUser.getUserId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();
    testUser2.setUsername("firstname@lastname");
    testUser2.setPassword("testpassword");
    testUser2.setStatus(UserStatus.OFFLINE);
    testUser2.setToken("1");
    testUser2.setIdentity(UserIdentity.STUDENT);
    testUser2.setAvatar("this is a image text");

    // change the name but forget about the username
    //testUser2.setName("testName2");
    testUser2.setUsername("firstname@lastname");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }
}
