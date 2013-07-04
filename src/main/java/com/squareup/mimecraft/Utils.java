// Copyright 2013 Square, Inc.
package com.squareup.mimecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class Utils {
  private Utils() {
    // No instances.
  }

  public static final String DEFAULT_CHARSET = "UTF-8";

  private static final String CHARSET_DELIMITER = "charset=";

  static void copyStream(InputStream in, OutputStream out, byte[] buffer) throws IOException {
    int count;
    while ((count = in.read(buffer)) != -1) {
      out.write(buffer, 0, count);
    }
  }

  static void isNotNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalStateException(message);
    }
  }

  static void isNull(Object obj, String message) {
    if (obj != null) {
      throw new IllegalStateException(message);
    }
  }

  static void isNotEmpty(String thing, String message) {
    isNotNull(thing, message);
    if ("".equals(thing.trim())) {
      throw new IllegalStateException(message);
    }
  }

  static void isNotZero(int value, String message) {
    if (value != 0) {
      throw new IllegalStateException(message);
    }
  }

  static String extractCharsetFromContentType(String contentType, String defaultCharset) {
      if (contentType != null) {
        int indexOfCharsetKey = contentType.lastIndexOf(CHARSET_DELIMITER);
        int indexOfCharsetValue = indexOfCharsetKey + CHARSET_DELIMITER.length();
        if (indexOfCharsetKey != -1 && (contentType.length() - (indexOfCharsetValue + 1)) > 0) {
            return contentType.substring(indexOfCharsetValue);
        }
      }
      return defaultCharset;
  }
}
