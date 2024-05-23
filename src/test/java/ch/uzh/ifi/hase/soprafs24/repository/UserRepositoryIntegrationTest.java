package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql(scripts = "/schema.sql")
public class UserRepositoryIntegrationTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        // Add any setup code here
    }

    @AfterEach
    public void tearDown() {
        // Add any teardown code here
    }

  @BeforeEach
  public void setup(){
      // 保存数据
      user = new User();
      user.setUsername("testuser");
      user.setPassword("testpassword");
      user.setStatus(UserStatus.OFFLINE);
      user.setToken("1");
      user.setIdentity(UserIdentity.STUDENT);
      user.setAvatar("this is a image text");
      entityManager.persist(user);
      entityManager.flush();
//
//    backUpData = userRepository.findAll(); // 保存数据
//    userRepository.deleteAll();
//    //entityManager.getEntityManager().createQuery("DELETE FROM User").executeUpdate();// may cause inconsistence in database
//
//    //given
//    User user = new User();
//    user = new User();
//    user.setUsername("firstname@lastname");
//    user.setPassword("testpassword");
//    user.setStatus(UserStatus.OFFLINE);
//    user.setToken("1");
//    user.setIdentity(UserIdentity.STUDENT);
//    user.setAvatar("this is a image text");
  }

  @Test
  public void findByUserName_success() {
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
    entityManager.persist(user);
    entityManager.flush();

    // when
    boolean existsByUsername = userRepository.existsByUsername(user.getUsername());

    // then
    assertEquals(existsByUsername, true);
  }
}
