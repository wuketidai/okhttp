package okhttp3.internal.hsts;

import java.io.IOException;
import org.junit.Test;

public class HstsPreloadListTest {
  @Test
  public void loadFile() throws IOException {
    HstsPreloadList.instance();
  }
}
