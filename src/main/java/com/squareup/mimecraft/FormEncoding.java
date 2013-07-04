// Copyright 2013 Square, Inc.
package com.squareup.mimecraft;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/**
 * <a href="http://www.w3.org/MarkUp/html-spec/html-spec_8.html#SEC8.2.1">HTML 2.0</a>-compliant
 * form data.
 */
public final class FormEncoding implements Part {
   /** Fluent API to build {@link FormEncoding} instances. */
  public static class Builder {
    private final StringBuilder content = new StringBuilder();

    private String charsetName;

    public Builder() {
        this("UTF-8");
    }

    public Builder(String charsetName) {
        this.charsetName = charsetName;
    }

    /** Add new key-value pair. */
    public Builder add(String name, String value) {
      if (content.length() > 0) {
        content.append('&');
      }
      try {
        content.append(URLEncoder.encode(name, charsetName))
            .append('=')
            .append(URLEncoder.encode(value, charsetName));
      } catch (UnsupportedEncodingException e) {
        throw new AssertionError(e);
      }
      return this;
    }

    /** Create {@link FormEncoding} instance. */
    public FormEncoding build() {
      if (content.length() == 0) {
        throw new IllegalStateException("Form encoded body must have at least one part.");
      }
      return new FormEncoding(content.toString(), charsetName);
    }
  }

  private String charsetName;
  private String data;

  private FormEncoding(String data, String charsetName) {
    this.data = data;
    this.charsetName = charsetName;
  }

  @Override public Map<String, String> getHeaders() {
    return Collections.singletonMap("Content-Type", "application/x-www-form-urlencoded; charset=" + charsetName);
  }

  @Override public void writeBodyTo(OutputStream stream) throws IOException {
      byte[] bytes;
      try {
          bytes = data.getBytes(charsetName);
      } catch (UnsupportedEncodingException e) {
          throw new IllegalArgumentException("Unable to convert input to " + charsetName + ": " + data, e);
      }
      if (bytes != null) {
        stream.write(bytes);
      }
  }
}
