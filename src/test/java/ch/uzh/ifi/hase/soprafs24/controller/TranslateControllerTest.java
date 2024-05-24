package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TranslateController.class)
public class TranslateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Translate translate;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemService itemService;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private TopicService topicService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ChatService chatService;

    @Test
    public void translateText_whenValidInput_thenReturnTranslatedText() throws Exception {
        // given
        String originalText = "Hello";
        String translatedText = "Hallo";
        String targetLanguage = "de"; // German

        Translation mockTranslation = Mockito.mock(Translation.class);
        given(mockTranslation.getTranslatedText()).willReturn(translatedText);
        given(translate.translate(anyString(), any(Translate.TranslateOption[].class)))
                .willReturn(mockTranslation);

        MockHttpServletRequestBuilder getRequest = get("/translate")
                .param("text", originalText)
                .param("targetLanguage", targetLanguage)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(translatedText)));
    }

    @Test
    public void translateText_whenMissingParameter_thenReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/translate")
                .param("text", "Hello")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }
}
