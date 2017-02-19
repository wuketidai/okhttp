package okhttp3;

import java.io.IOException;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;

import static okhttp3.TestUtil.defaultClient;
import static org.junit.Assert.assertTrue;

public class HstsTest {
  @Rule public final MockWebServer server = new MockWebServer();

  private OkHttpClient client = defaultClient();

  @Test
  public void testEnforcesHttps() throws IOException {
    Request request = new Request.Builder().url("http://twitter.com/robots.txt").build();
    Response response = client.newCall(request).execute();

    assertTrue(response.networkResponse().request().url().isHttps());
  }
}
