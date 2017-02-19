package okhttp3.internal.hsts;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HstsInterceptor implements Interceptor {
  public static final String HEADER = "Strict-Transport-Security";

  private HstsList list;

  public HstsInterceptor() {
    this.list = new ClientHstsList();
  }

  public HstsInterceptor(HstsList list) {
    this.list = list;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    if (!request.isHttps() && list.isHsts(request.url().host())) {
      request = upgradeToHttps(request);
    }

    Response response = chain.proceed(request);

    updateHsts(response);

    return response;
  }

  private void updateHsts(Response response) {
    Response networkResponse = response.networkResponse();

    // not a cached response, and HTTPS response (n.b. may be redirected to https automatically)
    if (networkResponse != null && networkResponse.handshake() != null) {
      String responseHeader = response.header(HEADER);
      if (responseHeader != null) {
        list.updateHsts(networkResponse.request().url().host(), responseHeader);
      }
    }
  }

  private Request upgradeToHttps(Request request) {
    HttpUrl url = request.url().newBuilder().scheme("https").build();
    return request.newBuilder().url(url).build();
  }
}
