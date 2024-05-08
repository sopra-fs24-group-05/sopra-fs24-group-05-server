package ch.uzh.ifi.hase.soprafs24.controller;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    private final Translate translate;

    public TranslateController() {
        // Set API token
        String apiKey = "AIzaSyA5QmPBw6Iaa3bzjYyVJ_yB4n228pa1VEc";
        translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
    }

    @GetMapping
    public String translateText(@RequestParam String text, @RequestParam String targetLanguage) {

        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLanguage)
        );


        return translation.getTranslatedText();
    }
}
