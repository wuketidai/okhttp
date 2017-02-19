package okhttp3.internal.hsts;

import java.io.IOException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import okhttp3.CipherSuite;
import okhttp3.Connection;
import okhttp3.Handshake;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TODO: consider replacing with MockWebServer tests?
 */
public class HstsInterceptorTest {
  private HstsList list = new HstsList() {
    @Override public boolean isHsts(String host) throws IOException {
      return hstsHosts.contains(host);
    }

    @Override public void updateHsts(String host, String responseHeader) {
      updatedHeaders.put(host, responseHeader);
    }
  };

  Interceptor.Chain chain = new Interceptor.Chain() {
    @Override public Request request() {
      return originalRequest;
    }

    @Override public Response proceed(Request request) throws IOException {
      return buildResponse(request);
    }

    @Override public Connection connection() {
      return null;
    }
  };

  private Request buildRequest(String url) {
    return new Request.Builder().url(url).build();
  }

  private Response buildResponse(Request request) {
    Response.Builder builder = new Response.Builder().request(request).protocol(Protocol.HTTP_2).code(200);

    if (responseHeaders.containsKey(request.url().host())) {
      builder.header(HstsInterceptor.HEADER, responseHeaders.get(request.url().host()));
    }

    if (request.isHttps()) {
      builder.handshake(Handshake.get(TlsVersion.TLS_1_3, CipherSuite.TLS_PSK_WITH_RC4_128_SHA,
          new ArrayList<Certificate>(), new ArrayList<Certificate>()));
    }

    Response response = builder.build();

    if (!cached) {
      response = response.newBuilder().networkResponse(response).build();
    }

    return response;
  }

  private Request originalRequest = null;
  private Set<String> hstsHosts = new LinkedHashSet<>();
  private Map<String, String> responseHeaders = new LinkedHashMap<>();
  private Map<String, String> updatedHeaders = new LinkedHashMap<>();
  private boolean cached = false;

  private HstsInterceptor interceptor = new HstsInterceptor(list);

  @Test
  public void checksListAndAllowsHttp() throws IOException {
    originalRequest = buildRequest("http://google.com");

    Response response = interceptor.intercept(chain);

    assertFalse(response.request().isHttps());
  }

  @Test
  public void checksListAndEnforcesHttps() throws IOException {
    hstsHosts.add("google.com");
    originalRequest = buildRequest("http://google.com");

    Response response = interceptor.intercept(chain);

    assertTrue(response.request().isHttps());
  }

  @Test
  public void updatesHeaderForHttps() throws IOException {
    responseHeaders.put("google.com", "max-age=31536000");
    originalRequest = buildRequest("https://google.com");

    interceptor.intercept(chain);

    assertTrue(updatedHeaders.containsKey("google.com"));
  }

  @Test
  public void ignoresHeadersForHttp() throws IOException {
    responseHeaders.put("google.com", "max-age=31536000");
    originalRequest = buildRequest("http://google.com");

    interceptor.intercept(chain);

    assertFalse(updatedHeaders.containsKey("google.com"));
  }

  @Test
  public void ignoresHeadersForCachedResponses() throws IOException {
    responseHeaders.put("google.com", "max-age=31536000");
    originalRequest = buildRequest("https://google.com");
    cached = true;

    interceptor.intercept(chain);

    assertFalse(updatedHeaders.containsKey("google.com"));
  }
}
