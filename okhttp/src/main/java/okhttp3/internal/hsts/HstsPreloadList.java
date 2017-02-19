package okhttp3.internal.hsts;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import okio.Okio;

/**
 * Preload list from
 * https://cs.chromium.org/chromium/src/net/http/transport_security_state_static.json
 *
 * TODO optimise the format to avoid JSON parsing on startup.
 */
public class HstsPreloadList implements HstsList {
  private static HstsPreloadList instance;

  public final List<Pinset> pinsets;
  public final List<Entry> entries;
  public final List<String> domain_ids;

  public HstsPreloadList(List<Pinset> pinsets, List<Entry> entries, List<String> domain_ids) {
    this.pinsets = pinsets;
    this.entries = entries;
    this.domain_ids = domain_ids;
  }

  public static synchronized HstsPreloadList instance() throws IOException {
    if (instance == null) {
      instance = load();
    }

    return instance;
  }

  public static HstsPreloadList load() throws IOException {
    Moshi moshi = new Moshi.Builder().build();

    JsonAdapter<HstsPreloadList> jsonAdapter = moshi.adapter(HstsPreloadList.class);

    try (InputStream is = HstsPreloadList.class
        .getResourceAsStream("transport_security_state_static.json")) {
      HstsPreloadList preloadList = jsonAdapter.fromJson(Okio.buffer(Okio.source(is)));
      return preloadList;
    }
  }

  @Override public boolean isHsts(String host) throws IOException {
    for (Entry e: entries) {
      if (e.name.equals(host)) {
        return true;
      }
    }

    return false;
  }

  @Override public void updateHsts(String host, String responseHeader) {
    // static only
  }
}
