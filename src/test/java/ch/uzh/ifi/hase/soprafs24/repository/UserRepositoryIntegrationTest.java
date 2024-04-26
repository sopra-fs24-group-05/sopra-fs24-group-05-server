package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUserName_success() {
    // given
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testpassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setIdentity(UserIdentity.STUDENT);

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername());

    // then
    assertNotNull(found.getUserId());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getIdentity(), user.getIdentity());
  }

  @Test
  public void findByToken_success() {
    // given
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testpassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setIdentity(UserIdentity.STUDENT);

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByToken(user.getToken());

    // then
    assertNotNull(found.getUserId());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getIdentity(), user.getIdentity());
  }

  @Test
  public void existsByUsername_success() {
    // given
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testpassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setIdentity(UserIdentity.STUDENT);

    entityManager.persist(user);
    entityManager.flush();

    // when
    boolean existsByUsername = userRepository.existsByUsername(user.getUsername());

    // then
    assertEquals(existsByUsername, true);
  }
}
