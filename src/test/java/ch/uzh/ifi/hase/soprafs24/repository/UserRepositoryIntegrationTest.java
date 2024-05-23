package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

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
  @MockBean
  private ServerEndpointExporter serverEndpointExporter; // Mock ServerEndpointExporter to avoid loading WebSocket configuration

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User user;
  private User admin;

  @BeforeEach
  public void setup(){
    user = new User();
    user.setUsername("testuser");
    user.setPassword("testpassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setIdentity(UserIdentity.STUDENT);
    user.setAvatar("this is a image text");
    entityManager.persist(user);
    entityManager.flush();

    admin = new User();
    admin.setUsername("testadmin");
    admin.setPassword("testpassword");
    admin.setStatus(UserStatus.OFFLINE);
    admin.setToken("2");
    admin.setIdentity(UserIdentity.ADMIN);
    admin.setAvatar("this is a image text");
    entityManager.persist(admin);
    entityManager.flush();
  }

  //@AfterEach
  //public void cleanUp

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
  @Test
  public void findByIdentity_success() {
    // when
    List<User> students = userRepository.findByIdentity(UserIdentity.STUDENT);
    List<User> admins = userRepository.findByIdentity(UserIdentity.ADMIN);

    // then
    assertEquals(1, students.size());
    assertEquals("testuser", students.get(0).getUsername());

    assertEquals(1, admins.size());
    assertEquals("testadmin", admins.get(0).getUsername());
}
}
