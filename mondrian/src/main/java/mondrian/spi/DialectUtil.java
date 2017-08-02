/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (c) 2015-2017 Pentaho Corporation.
// All rights reserved.
 */
package mondrian.spi;

import java.util.regex.Pattern;

public class DialectUtil {

  private static final Pattern UNICODE_CASE_FLAG_IN_JAVA_REG_EXP_PATTERN = Pattern.compile( "\\|\\(\\?u\\)" );
  private static final String EMPTY = "";

  public static String cleanUnicodeCaseFlagInRegularExpression( String javaRegExp, Dialect dialect ) {
    String cleaned = javaRegExp;
    if ( cleaned != null && !dialect.isUnicodeCaseFlagSupported() ) {
      cleaned = UNICODE_CASE_FLAG_IN_JAVA_REG_EXP_PATTERN.matcher( cleaned ).replaceAll( EMPTY );
    }
    return cleaned;
  }
}
