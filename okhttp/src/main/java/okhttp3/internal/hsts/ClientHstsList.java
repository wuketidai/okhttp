package okhttp3.internal.hsts;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Short lived HSTS registry, storing HSTS responses for a single
 * OkHttpClient.
 *
 * TODO use some mechanism to permanently store updates from responses
 */
public class ClientHstsList implements HstsList {
  public Map<String, String> lastHostHeaders = new LinkedHashMap<>();
  public Map<String, Entry> hostConfig = new LinkedHashMap<>();

  public boolean isHsts(String host) throws IOException {
    for (Entry e: HstsPreloadList.instance().entries) {
      if (e.name.equals(host)) {
        return true;
      }
    }

    return false;
  }

  public void updateHsts(String host, String responseHeader) {
    System.out.println(responseHeader);
  }
}
