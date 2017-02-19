package okhttp3.internal.hsts;

import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientHstsListTest {
  private ClientHstsList list = new ClientHstsList();

  @Test
  public void loadsDefaults() throws IOException {
    assertTrue(list.isHsts("twitter.com"));
    assertTrue(list.isHsts("www.twitter.com"));
    assertTrue(list.isHsts("google.com"));
    assertTrue(list.isHsts("google.co.uk"));
    assertTrue(list.isHsts("www.google.co.uk"));
    assertFalse(list.isHsts("www.google.com"));
    assertFalse(list.isHsts("pastebin.com"));
  }

  @Test
  public void clearedByMaxAge0() throws IOException {
    assertTrue(list.isHsts("google.co.uk"));
    assertTrue(list.isHsts("www.google.co.uk"));

    list.updateHsts("google.co.uk", "max-age=0");

    assertFalse(list.isHsts("google.co.uk"));
    assertTrue(list.isHsts("www.google.co.uk"));
  }
}
