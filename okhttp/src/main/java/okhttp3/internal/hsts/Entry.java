package okhttp3.internal.hsts;

public class Entry {
  public final String name;
  public final Boolean include_subdomains;
  public final Boolean include_subdomains_for_pinning;
  public final String mode;
  public final String pins;
  public final Boolean expect_ct;
  public final String expect_ct_report_uri;
  public final Boolean expect_staple;
  public final String expect_staple_report_uri;
  public final Boolean include_subdomains_for_expect_staple;

  public Entry(String name, Boolean include_subdomains, Boolean include_subdomains_for_pinning,
      String mode, String pins, Boolean expect_ct, String expect_ct_report_uri,
      Boolean expect_staple, String expect_staple_report_uri,
      Boolean include_subdomains_for_expect_staple) {
    this.name = name;
    this.include_subdomains = include_subdomains;
    this.include_subdomains_for_pinning = include_subdomains_for_pinning;
    this.mode = mode;
    this.pins = pins;
    this.expect_ct = expect_ct;
    this.expect_ct_report_uri = expect_ct_report_uri;
    this.expect_staple = expect_staple;
    this.expect_staple_report_uri = expect_staple_report_uri;
    this.include_subdomains_for_expect_staple = include_subdomains_for_expect_staple;
  }
}
