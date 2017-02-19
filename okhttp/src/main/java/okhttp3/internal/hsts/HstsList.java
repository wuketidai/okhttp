package okhttp3.internal.hsts;

import java.io.IOException;

public interface HstsList {
  boolean isHsts(String host) throws IOException;
  void updateHsts(String host, String responseHeader);
}
