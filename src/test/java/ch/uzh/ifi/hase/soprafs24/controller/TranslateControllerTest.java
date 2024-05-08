package ch.uzh.ifi.hase.soprafs24.controller;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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

    @Test
    public void translateText_whenValidInput_thenReturnTranslatedText() throws Exception {
        // given
        String originalText = "Hello";
        String translatedText = "Hallo";
        String targetLanguage = "de"; // German (德语)

        // 模拟 Google Translate 客户端返回的结果
        Translation mockTranslation = Mockito.mock(Translation.class);
        given(mockTranslation.getTranslatedText()).willReturn(translatedText);
        given(translate.translate(anyString(), any(Translate.TranslateOption[].class)))
                .willReturn(mockTranslation);

        // 构建 GET 请求
        MockHttpServletRequestBuilder getRequest = get("/api/translate")
                .param("text", originalText)
                .param("targetLanguage", targetLanguage)
                .contentType(MediaType.APPLICATION_JSON);

        // 进行请求并验证结果
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(translatedText)));
    }

    @Test
    public void translateText_whenMissingParameter_thenReturnBadRequest() throws Exception {
        // 构建没有目标语言参数的请求
        MockHttpServletRequestBuilder getRequest = get("/api/translate")
                .param("text", "Hello")
                .contentType(MediaType.APPLICATION_JSON);

        // 执行请求，期望返回 400 Bad Request
        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }
}
