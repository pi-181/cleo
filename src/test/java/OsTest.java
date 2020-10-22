import com.css.cleo.os.FeatureFactory;
import org.junit.Assert;
import org.junit.Test;

public class OsTest {

    @Test
    public void testDesktopApps() {
        Assert.assertNotNull(FeatureFactory.getNativeFeature().orElseThrow().getDesktopApps());
    }

    @Test
    public void testTools() {
        Assert.assertNotNull(FeatureFactory.getNativeFeature().orElseThrow().getTools());
    }

}
