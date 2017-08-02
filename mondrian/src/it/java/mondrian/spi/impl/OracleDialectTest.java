/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (c) 2015-2017 Pentaho Corporation.
// All rights reserved.
 */
package mondrian.spi.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import junit.framework.TestCase;
import mondrian.spi.Dialect;

public class OracleDialectTest extends TestCase {
  public void testGenerateRegularExpression_CaseInsensitive() throws Exception {
    String sql = callGenerateRegularExpression( "table.column", "(?i)|(?u).*a.*" );
    assertEquals( "table.column IS NOT NULL AND REGEXP_LIKE(table.column, '.*a.*', 'i')", sql );
  }

  public void testGenerateRegularExpression_CaseSensitive() throws Exception {
    String sql = callGenerateRegularExpression( "table.column", ".*a.*" );
    assertEquals( "table.column IS NOT NULL AND REGEXP_LIKE(table.column, '.*a.*', '')", sql );
  }

  private String callGenerateRegularExpression( String source, String regex ) throws Exception {
    DatabaseMetaData metaData = mock( DatabaseMetaData.class );
    when( metaData.getDatabaseProductName() ).thenReturn( Dialect.DatabaseProduct.ORACLE.name() );

    Connection connection = mock( Connection.class );
    when( connection.getMetaData() ).thenReturn( metaData );

    return new OracleDialect( connection ).generateRegularExpression( source, regex );
  }

}
