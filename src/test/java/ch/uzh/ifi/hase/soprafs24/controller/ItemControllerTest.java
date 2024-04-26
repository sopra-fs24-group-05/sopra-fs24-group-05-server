package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;

import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(ItemController.class)
public class ItemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private ItemService itemService;

  @MockBean
  private CommentService commentService;

  @Test
  public void addItem_whenValidInput_thenReturnCreated() throws Exception {
      // given
      Long itemId = 1L;
      Integer topicId = 1;
      Item item = new Item();
      item.setItemId(itemId);
      item.setItemName("Test Item");
      item.setContent("this is a test item");
      item.setScore(1.0);
      item.setTopicId(1);

      ItemPostDTO itemPostDTO = new ItemPostDTO();
      itemPostDTO.setItemId(1L);
      itemPostDTO.setItemName("Test Item");
      itemPostDTO.setContent("this is a test item");
      itemPostDTO.setScore(1.0);
      itemPostDTO.setTopicId(1);

      given(itemService.addItem(Mockito.any())).willReturn(item);

      MockHttpServletRequestBuilder postRequest = post("/items/byTopicId/"+topicId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(itemPostDTO));

      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.itemId", is(item.getItemId().intValue())))
              .andExpect(jsonPath("$.itemName", is(item.getItemName())))
              .andExpect(jsonPath("$.content", is(item.getContent())))
              .andExpect(jsonPath("$.score", is(item.getScore())))
              .andExpect(jsonPath("$.topicId", is(item.getTopicId())));
  }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
    
    @Test
    public void addItem_already_exists() throws Exception {
        // given
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setItemId(1L);
        itemPostDTO.setItemName("Test Item");
        itemPostDTO.setContent("this is a test item");
        itemPostDTO.setScore(1.0);
        itemPostDTO.setTopicId(1);


        given(itemService.addItem(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "add User failed because item name already exists"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/items/byTopicId/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void likeItem_whenValidInput_thenReturns200() throws Exception {
        // given
        Long itemId = 1L;

        // when
        doNothing().when(itemService).likeItem(itemId);

        // then
        mockMvc.perform(post("/items/{itemId}/like", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService).likeItem(itemId);
    }

    @Test
    public void getItemsByTopicSortedByScore_whenValidTopicId_thenReturnsSortedItems() throws Exception {
        // given
        Integer topicId = 1;
        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("Test Item 1");
        item1.setContent("Description 1");
        item1.setScore(9.0);
        item1.setTopicId(topicId);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("Test Item 2");
        item2.setContent("Description 2");
        item2.setScore(8.5);
        item2.setTopicId(topicId);

        List<Item> itemList = Arrays.asList(item1, item2);
        List<ItemGetDTO> itemGetDTOs = itemList.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());

        when(itemService.getItemsByTopicSortedByScore(anyInt())).thenReturn(itemList);

        // when & then
        mockMvc.perform(get("/items/sortedByScore/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Checking if the size of the list in the response is 2
                .andExpect(jsonPath("$[0].itemId", is(item1.getItemId().intValue())))
                .andExpect(jsonPath("$[0].itemName", is(item1.getItemName())))
                .andExpect(jsonPath("$[0].score", is(item1.getScore())))
                .andExpect(jsonPath("$[1].itemId", is(item2.getItemId().intValue())))
                .andExpect(jsonPath("$[1].itemName", is(item2.getItemName())))
                .andExpect(jsonPath("$[1].score", is(item2.getScore())));
    }

    @Test
    public void getItemsByTopicId_whenValidTopicId_thenReturnsItems() throws Exception {
        // given
        Integer topicId = 1;
        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("Test Item 1");
        item1.setContent("Content of Item 1");
        item1.setScore(9.5);
        item1.setTopicId(topicId);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("Test Item 2");
        item2.setContent("Content of Item 2");
        item2.setScore(8.5);
        item2.setTopicId(topicId);

        List<Item> itemList = Arrays.asList(item1, item2);

        // Convert Items to ItemGetDTOs
        List<ItemGetDTO> itemGetDTOs = itemList.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());

        when(itemService.getItemsByTopicId(anyInt())).thenReturn(itemList);

        // when & then
        mockMvc.perform(get("/items/byTopicId/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].itemId", is(item1.getItemId().intValue())))
                .andExpect(jsonPath("$[0].itemName", is(item1.getItemName())))
                .andExpect(jsonPath("$[0].content", is(item1.getContent())))
                .andExpect(jsonPath("$[1].itemId", is(item2.getItemId().intValue())))
                .andExpect(jsonPath("$[1].itemName", is(item2.getItemName())))
                .andExpect(jsonPath("$[1].content", is(item2.getContent())));
    }

    @Test
    public void getItemsByTopicName_whenValidTopicName_thenReturnsItems() throws Exception {
        // given
        String topicName = "Technology";
        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("Test Item 1");
        item1.setContent("Description 1");
        item1.setScore(9.0);
        item1.setTopicId(1);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("Test Item 2");
        item2.setContent("Description 2");
        item2.setScore(8.5);
        item2.setTopicId(1);

        List<Item> itemList = Arrays.asList(item1, item2);
        List<ItemGetDTO> itemGetDTOs = itemList.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());

        when(itemService.getItemsByTopicName(anyString())).thenReturn(itemList);

        // when & then
        mockMvc.perform(get("/items/byTopicName/{topicName}", topicName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Check if the size of the list in the response is 2
                .andExpect(jsonPath("$[0].itemId", is(item1.getItemId().intValue())))
                .andExpect(jsonPath("$[0].itemName", is(item1.getItemName())))
                .andExpect(jsonPath("$[0].score", is(item1.getScore())))
                .andExpect(jsonPath("$[1].itemId", is(item2.getItemId().intValue())))
                .andExpect(jsonPath("$[1].itemName", is(item2.getItemName())))
                .andExpect(jsonPath("$[1].score", is(item2.getScore())));
    }

    @Test
    public void getItemAverageScore_whenValidItemId_thenReturnsScore() throws Exception {
        // given
        Long itemId = 1L;
        Double averageScore = 8.5;

        when(itemService.getItemAverageScore(itemId)).thenReturn(averageScore);

        // when & then
        mockMvc.perform(get("/{itemId}/score", itemId)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(averageScore)));
    }

    @Test
    public void deleteItemByItemName_whenValidItemName_thenItemIsDeleted() throws Exception {
        // given
        Integer topicId = 1;
        String itemName = "TestItem";

        // when & then
        mockMvc.perform(delete(String.format("/items/%d/%s", topicId, itemName)))
                .andExpect(status().isNoContent());

        // verify that the service method was called with the correct parameter
        verify(itemService, times(1)).deleteItemByItemName(eq(itemName));
    }

    @Test
    public void deleteItemByItemId_whenValidItemId_thenItemIsDeleted() throws Exception {
        // given
        Long itemId = 1L;

        // when & then
        mockMvc.perform(delete(String.format("/items/%d", itemId)))
                .andExpect(status().isNoContent());

        // verify that the service method was called with the correct parameter
        verify(itemService, times(1)).deleteItemByItemId(eq(itemId));
    }
}
