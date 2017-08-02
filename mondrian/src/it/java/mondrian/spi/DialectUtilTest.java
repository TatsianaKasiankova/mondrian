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

import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DialectUtilTest extends TestCase {
  private Dialect dialectMock = mock( Dialect.class );

  public void testCleanedUnicodeCaseFlagInRegularExpression_WhenNotSupportedInDBDialect() {
    when( dialectMock.isUnicodeCaseFlagSupported() ).thenReturn( false );
    String expression = "(?i)|(?u).*ａ.*";
    String expectedExpression = "(?i).*ａ.*";
    String cleaned = DialectUtil.cleanUnicodeCaseFlagInRegularExpression( expression, dialectMock );
    assertEquals( expectedExpression, cleaned );
  }

  public void testNotCleanedUnicodeCaseFlagInRegularExpression_WhenSupportedInDBDialect() {
    when( dialectMock.isUnicodeCaseFlagSupported() ).thenReturn( true );
    String expression = "(?i)|(?u).*ａ.*";
    String expectedExpression = expression;
    String cleaned = DialectUtil.cleanUnicodeCaseFlagInRegularExpression( expression, dialectMock );
    assertEquals( expectedExpression, cleaned );
  }

}
