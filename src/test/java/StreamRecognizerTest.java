import com.css.cleo.Main;
import com.css.cleo.voice.recognize.StreamVoiceRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import main.java.com.goxr3plus.jsfggrammarparser.parser.JSGFGrammarParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test for voice recognition module
 */
public class StreamRecognizerTest {
    /**
     * Validates recognizer with simple
     * example of jsgf grammar and prepared audio.
     *
     * @throws Exception some exception that can be
     *                   dropped during load configurations and files
     */
    @Test
    public void testRecognizer() throws Exception {
        final Configuration config = new Configuration();
        config.setSampleRate(8000);
        config.setAcousticModelPath("resource:/audio_recognition/hmm/ru3");
        config.setDictionaryPath("resource:/test/ru.dic");
        config.setGrammarPath("resource:/test/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

        SpeechResult[] speechResults = new SpeechResult[1];
        StreamVoiceRecognizer simpleVoiceRecognizer = new StreamVoiceRecognizer(config,
                Main.class.getResourceAsStream("/test/audio1.wav"),
                (rec, res) -> {
                    speechResults[0] = res;
                    String hypothesis = res.getHypothesis();
                    System.out.format("Hypothesis: %s\n", hypothesis);
                    if (hypothesis.contains("теленок"))
                        rec.setEnabled(false);
                });

        simpleVoiceRecognizer.setEnabled(true);
        simpleVoiceRecognizer.waitForDoneUninterruptibly();

        SpeechResult speechResult = speechResults[0];
        Assert.assertEquals(speechResult.getHypothesis(), "илья ильф и евгений петров золотой теленок");

        List<String> rulesContainingWords = JSGFGrammarParser.getRulesContainingWords(
                getClass().getResourceAsStream("/test/text.gram"),
                Collections.singletonList("илья ильф"),
                true
        );
        Assert.assertTrue(rulesContainingWords.contains("author"));
    }
}
