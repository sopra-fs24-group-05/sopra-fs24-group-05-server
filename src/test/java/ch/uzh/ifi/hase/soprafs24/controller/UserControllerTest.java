package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FollowTopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FollowUserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private CommentService commentService;

  @MockBean
  private ItemService itemService;

  @MockBean
  private ChatService chatService;

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    //user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        //.andExpect(jsonPath("$[0].name", is(user.getName())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setUserId(1L);
    //user.setName("Test User");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    //userPostDTO.setName("Test User");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/registration")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId", is(user.getUserId().intValue()))) // $$.id→$.userId
        //.andExpect(jsonPath("$.name", is(user.getName())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }

  @Test
  public void followUser_validInput_success() throws Exception {
    // Given
    Long userId = 1L;
    Long followUserId = 2L;
    Mockito.doNothing().when(userService).followUser(userId, followUserId);

    // When
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followUsers", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(followUserId));

    // Then
    mockMvc.perform(putRequest)
        .andExpect(status().isOk());
  }

  @Test
  public void followUser_userNotFound_throwsException() throws Exception {
    // Given
    Long userId = 1L;
    Long followUserId = 2L;
    doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"))
        .when(userService).followUser(userId, followUserId);

    // When
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followUsers", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(followUserId));

    // Then
    mockMvc.perform(putRequest)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void followUser_targetUserNotFound_throwsException() throws Exception {
    // Given
    Long userId = 1L;
    Long followUserId = 3L;
    doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "target userId not found"))
        .when(userService).followUser(userId, followUserId);

    // When
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followUsers", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(followUserId));

    // Then
    mockMvc.perform(putRequest)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void followUser_alreadyFollowing_throwsException() throws Exception {
    // Given
    Long userId = 1L;
    Long followUserId = 2L;
    doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target user"))
        .when(userService).followUser(userId, followUserId);

    // When
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followUsers", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(followUserId));

    // Then
    mockMvc.perform(putRequest)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getFollowUserList_validInput_success() throws Exception {
      // Given
      Long userId = 1L;
      User followUser = new User();
      followUser.setUserId(2L);
      followUser.setUsername("testUser");
      List<User> followUsers = Collections.singletonList(followUser);

      given(userService.getFollowUsers(userId)).willReturn(followUsers);

      // When
      MockHttpServletRequestBuilder getRequest = get("/users/{userId}/followUsers", userId)
          .contentType(MediaType.APPLICATION_JSON);

      // Then
      mockMvc.perform(getRequest)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].followUserId", is(followUser.getUserId().intValue())))
          .andExpect(jsonPath("$[0].followUsername", is(followUser.getUsername())));
  }

    @Test
    public void followItem_validInput_success() throws Exception {
        // Given
        Long userId = 1L;
        Long followItemId = 100L;
        doNothing().when(userService).followItem(userId, followItemId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followItems", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followItemId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void followItem_userNotFound_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followItemId = 100L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"))
                .when(userService).followItem(userId, followItemId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followItems", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followItemId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void followItem_itemNotFound_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followItemId = 200L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "target item not found"))
                .when(userService).followItem(userId, followItemId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followItems", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followItemId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void followItem_alreadyFollowing_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followItemId = 100L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target item"))
                .when(userService).followItem(userId, followItemId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followItems", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followItemId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

  @Test
  public void getFollowItemList_validInput_success() throws Exception {
    // Given
    Long userId = 1L;
    Item followItem = new Item();
    followItem.setItemId(2L);
    followItem.setItemName("testItem");
    List<Item> followItems = Collections.singletonList(followItem);

    given(userService.getFollowItems(userId)).willReturn(followItems);

    // When
    MockHttpServletRequestBuilder getRequest = get("/users/{userId}/followItems", userId)
        .contentType(MediaType.APPLICATION_JSON);

    // Then
    mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].followItemId", is(followItem.getItemId().intValue())))
        .andExpect(jsonPath("$[0].followItemname", is(followItem.getItemName())));
  }

  @Test
    public void followTopic_validInput_success() throws Exception {
        // Given
        Long userId = 1L;
        Long followTopicId = 100L;
        doNothing().when(userService).followTopic(userId, followTopicId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followTopics", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followTopicId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void followTopic_userNotFound_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followTopicId = 100L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId not found"))
                .when(userService).followTopic(userId, followTopicId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followTopics", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followTopicId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void followTopic_topicNotFound_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followTopicId = 200L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "target topic not found"))
                .when(userService).followTopic(userId, followTopicId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followTopics", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followTopicId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void followTopic_alreadyFollowing_throwsException() throws Exception {
        // Given
        Long userId = 1L;
        Long followTopicId = 100L;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "already followed target topic"))
                .when(userService).followTopic(userId, followTopicId);

        // When
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}/followTopics", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(followTopicId));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getFollowTopicList_validInput_success() throws Exception {
        // Given
        Long userId = 1L;
        Topic followTopic = new Topic();
        followTopic.setTopicId(2);
        followTopic.setTopicName("testTopic");
        List<Topic> followTopics = Collections.singletonList(followTopic);

        //FollowTopicGetDTO followTopicGetDTO = new FollowTopicGetDTO();
        //followTopicGetDTO.setFollowTopicId(followTopic.getTopicId());
        //followTopicGetDTO.setFollowTopicName(followTopic.getTopicName());

        given(userService.getFollowTopics(userId)).willReturn(followTopics);
        //given(dtoMapper.converEntityToFollowTopicGetDTO(followTopic)).willReturn(followTopicGetDTO);

        // When
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}/followTopics", userId)
                .contentType(MediaType.APPLICATION_JSON);

        // Then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].followTopicId", is(followTopic.getTopicId().intValue())))
                .andExpect(jsonPath("$[0].followTopicname", is(followTopic.getTopicName())));
    }

    @Test
    public void banUser_validAdminAndTarget_success() throws Exception {
        // given
        Long adminId = 1L;
        Long targetId = 2L;

        // do nothing when the userService.banUser is called (default behavior)
        Mockito.doNothing().when(userService).banUser(adminId, targetId);

        // when
        MockHttpServletRequestBuilder putRequest = put("/admin/banUser/{adminId}", adminId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(targetId.toString());

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).banUser(adminId, targetId);
    }

    @Test
    public void getBannedList_validAdmin_success() throws Exception {
        // given: mock banned users
        User bannedUser1 = new User();
        bannedUser1.setUserId(1L);
        bannedUser1.setUsername("bannedUser1");
        bannedUser1.setIdentity(UserIdentity.BANNED);

        User bannedUser2 = new User();
        bannedUser2.setUserId(2L);
        bannedUser2.setUsername("bannedUser2");
        bannedUser2.setIdentity(UserIdentity.BANNED);

        List<User> bannedUsers = new ArrayList<>();
        bannedUsers.add(bannedUser1);
        bannedUsers.add(bannedUser2);

        // mock userService behavior
        Mockito.when(userService.getAllBannedUsers(Mockito.any(Long.class)))
                .thenReturn(bannedUsers); // mock banned users list

        // when
        MockHttpServletRequestBuilder getRequest = get("/admin/getBannedList/{adminId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(bannedUser1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(bannedUser2.getUsername())));
    }


}