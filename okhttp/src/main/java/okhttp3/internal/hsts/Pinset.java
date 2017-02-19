package okhttp3.internal.hsts;

import java.util.List;

public class Pinset {
  public final String name;
  public final List<String> static_spki_hashes;
  public final List<String> bad_static_spki_hashes;
  public final String report_uri;

  public Pinset(String name, List<String> static_spki_hashes, List<String> bad_static_spki_hashes,
      String report_uri) {
    this.name = name;
    this.static_spki_hashes = static_spki_hashes;
    this.bad_static_spki_hashes = bad_static_spki_hashes;
    this.report_uri = report_uri;
  }
}
