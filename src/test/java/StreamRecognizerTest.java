import com.css.cleo.Main;
import com.css.cleo.voice.recognize.StreamVoiceRecognizer;
import edu.cmu.sphinx.api.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class StreamRecognizerTest {
    @Test
    public void testRecognizer() throws Exception {
        final Configuration config = new Configuration();
        final String resAudioRec = "resource:/audio_recognition";
        config.setSampleRate(8000);
        config.setAcousticModelPath(resAudioRec + "/hmm/ru3");
        config.setDictionaryPath(resAudioRec + "/dictionary/ru.dic");
        config.setGrammarPath(resAudioRec + "/lang/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

        String[] hypothesis = new String[1];
        StreamVoiceRecognizer simpleVoiceRecognizer = new StreamVoiceRecognizer(config,
                Main.class.getResourceAsStream("/audio_recognition/test/audio1.wav"),
                (rec, res) -> {
                    hypothesis[0] = res.getHypothesis();
                    System.out.format("Hypothesis: %s\n", hypothesis);
                    if (hypothesis[0].contains("теленок"))
                        rec.setEnabled(false);
                });

        simpleVoiceRecognizer.setEnabled(true);
        simpleVoiceRecognizer.waitForDoneUninterruptibly();

        Assert.assertEquals(hypothesis[0], "илья ильф и евгений петров золотой теленок");
    }
}
