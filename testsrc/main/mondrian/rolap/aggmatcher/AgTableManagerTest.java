/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2014 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package mondrian.rolap.aggmatcher;

import mondrian.olap.Result;
import mondrian.test.FoodMartTestCase;
import mondrian.test.TestContext;
import mondrian.util.Bug;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class AgTableManagerTest extends FoodMartTestCase{
  
  public AgTableManagerTest(String name) {
    super(name);
}

  
  /**
   * DRAFT: Just to reproduce
   */
  public void tesLoadRolapStarAggregatesIsSuccessfulOnSharedDims() {
     
    propSaver.set(propSaver.properties.UseAggregates, true);
    propSaver.set(propSaver.properties.ReadAggregates, true);
          final TestContext testContext = TestContext.instance().create(
              null,
              "<Cube name=\"Sales Two Dimensions\">\n"
              + "  <Table name=\"sales_fact_1997\" alias=\"test\" />\n"
              + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
              + "  <DimensionUsage name=\"Time2\" source=\"Time\" foreignKey=\"product_id\"/>\n"
              + "  <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
              + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\"/>\n"
              + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\" formatString=\"#,###.00\"/>\n"
              + "</Cube>",
              null,
              null,
              null,
              null);
          
          testContext.assertQueryReturns(
              "select\n"
              + " {[Time2].[1997]} on columns,\n"
              + " {[Time].[1997].[Q3]} on rows\n"
              + "From [Sales Two Dimensions]",
              "Axis #0:\n"
              + "{}\n"
              + "Axis #1:\n"
              + "{[Time2].[1997]}\n"
              + "Axis #2:\n"
              + "{[Time].[1997].[Q3]}\n"
              + "Row #0: 16,266\n");

  
  }
  
  /**
   * DRAFT: Just to reproduce
   */
  public void tesLoadRolapStarAggregatesIsSuccessfulWhenLevelTableAttributeAsView() {
      propSaver.set(propSaver.properties.UseAggregates, true);
      propSaver.set(propSaver.properties.ReadAggregates, true);
      
      final TestContext testContext = TestContext.instance().create(
          null,
          "<Cube name=\"GenderCube\">\n"
          + "  <Table name=\"sales_fact_1997\" alias=\"sales_fact_1997_gender\"/>\n"
          + "<Dimension name=\"Gender2\" foreignKey=\"customer_id\">\n"
          + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
          + "    <View alias=\"gender2\">\n"
          + "      <SQL dialect=\"generic\">\n"
          + "        <![CDATA[SELECT * FROM customer]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"oracle\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"derby\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"hsqldb\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"luciddb\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"neoview\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"netezza\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "      <SQL dialect=\"db2\">\n"
          + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
          + "      </SQL>\n"
          + "    </View>\n"
          + "    <Level name=\"Gender\" table=\"gender2\" column=\"gender\" uniqueMembers=\"true\"/>\n"
          + "  </Hierarchy>\n"
          + "</Dimension>"
          + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
          + "      formatString=\"Standard\"/>\n"
          + "</Cube>",
          null, null, null, null);
      
          if (!testContext.getDialect().allowsFromQuery()) {
            return;
        }
    
        Result result = testContext.executeQuery(
            "select {[Gender2].members} on columns from [GenderCube]");
    
        TestContext.assertEqualsVerbose(
            "[Gender2].[All Gender]\n"
            + "[Gender2].[F]\n"
            + "[Gender2].[M]",
            TestContext.toString(
                result.getAxes()[0].getPositions()));
  }

}
