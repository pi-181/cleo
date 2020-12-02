import com.css.cleo.os.FeatureFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Simple tests for {@link FeatureFactory factory}
 */
public class OsTest {

    @Test
    public void testDesktopApps() {
        Assert.assertNotNull(FeatureFactory.getNativeFeature().orElseThrow().getDesktopApps());
    }

    @Test
    public void testTools() {
        Assert.assertNotNull(FeatureFactory.getNativeFeature().orElseThrow().getTools());
    }

    /**
     * By convention, {@link java.util.Optional optional} can't be null
     */
    @Test
    public void notNull() {
        Assert.assertNotNull(FeatureFactory.getNativeFeature());
    }

}
