package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Use H2 database
@Sql(scripts = "/schema.sql")
public class ChatRepositoryTest {

  @Autowired
  private ChatRepository chatRepository;

  private ChatMessage chatMessage1;
  private ChatMessage chatMessage2;
  private List<ChatMessage> backUpData;

  @BeforeEach
  public void setup() {
    backUpData = chatRepository.findAll();
    chatRepository.deleteAll();

    chatMessage1 = new ChatMessage();
    chatMessage1.setItemId(1L);
    chatMessage1.setUserId(1L);
    chatMessage1.setUserName("User1");
    chatMessage1.setUserAvatar("Avatar1");
    chatMessage1.setContent("First chat message");
    chatMessage1.setMessageTime(LocalDate.now().toString());

    chatMessage2 = new ChatMessage();
    chatMessage2.setItemId(1L);
    chatMessage2.setUserId(2L);
    chatMessage2.setUserName("User2");
    chatMessage2.setUserAvatar("Avatar2");
    chatMessage2.setContent("Second chat message");
    chatMessage2.setMessageTime(LocalDate.now().toString());

    chatRepository.saveAndFlush(chatMessage1);
    chatRepository.saveAndFlush(chatMessage2);
  }

  @AfterEach
  public void recover() {
    chatRepository.deleteAll();
    chatRepository.saveAll(backUpData);
  }

  @Test
  public void findByItemId_success() {
    // when
    List<ChatMessage> foundMessages = chatRepository.findByItemId(1L);

    // then
    assertEquals(2, foundMessages.size());
    assertNotNull(foundMessages.get(0).getMessageId());
    assertEquals(chatMessage1.getContent(), foundMessages.get(0).getContent());
    assertEquals(chatMessage2.getContent(), foundMessages.get(1).getContent());
  }
}
