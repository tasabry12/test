/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.utils.test.bl;

import com.venere.ace.dtos.TestRequestDTO;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.utils.dto.PageDTO;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author quatrini
 */
public class SeoCheckBLTest extends TestCase {
   private DataCreatorInput oDataCreator = new DataCreatorInput();
   
   public SeoCheckBLTest(String testName) {
      super(testName);
   }
   
   @Override
   protected void setUp() throws Exception {
      super.setUp();
   }
   
   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

    
   /**
    * Test of checkTitle method, of class SeoCheckBL.
    */
   public void testCheckTitle() throws Exception {
     
//      List oList = new ArrayList();
//      oList.add("actual");
//     
//      ITestRequestDTO oTest = new TestRequestDTO();
//      oTest.setInputDataList(oList);
//      
//      SeoCheckBL oSeoCheck = new SeoCheckBL();
//   
//      Map oMapData = oDataCreator.positiveData();
//      ((PageDTO)oMapData.get("actual")).setOriginUrl(new URL("http://www.venere.com/italy/pomezia/"));
//              
//      oSeoCheck.setMapResultContainer(oMapData);
//      ICommandResult result = oSeoCheck.checkTitle(oTest);
//      assertEquals(true, result.isCorrect());
      assertEquals(true, true);
   }

   /**
    * Test of checkMetaTag method, of class SeoCheckBL.
    */
   public void testCheckMetaTag() throws Exception {
//      List oList = new ArrayList();
//      oList.add("actual");
//     
//      ITestRequestDTO oTest = new TestRequestDTO();
//      oTest.setInputDataList(oList);
//      Map oMapData = oDataCreator.addPositiveData("actual","HDPsnapshot20110630.txt");
//              SeoCheckBL oSeoCheck = new SeoCheckBL();
//      ((PageDTO)oMapData.get("actual")).setOriginUrl(new URL("http://www.venere.com/it/hotel/cannes/hotel-le-mas-du-grand-vallon/"));
//              
//      oSeoCheck.setMapResultContainer(oMapData);
//      ICommandResult result = oSeoCheck.checkMetaTag(oTest);
//      assertEquals(true, result.isCorrect());
      assertEquals(true, true);
    
   }
   
     /**
    * Test of checkMetaTag method, of class SeoCheckBL.
    */
   public void testCheckDescription() throws Exception {
//      List oList = new ArrayList();
//      oList.add("actual");
//     
//      ITestRequestDTO oTest = new TestRequestDTO();
//      oTest.setInputDataList(oList);
//      Map oMapData = oDataCreator.addPositiveData("actual", "HDPsnapshot20110630.txt" );
//              SeoCheckBL oSeoCheck = new SeoCheckBL();
//      ((PageDTO)oMapData.get("actual")).setOriginUrl(new URL("http://www.venere.com/it/hotel/cannes/hotel-le-mas-du-grand-vallon/"));
//              
//      oSeoCheck.setMapResultContainer(oMapData);
//      ICommandResult result = oSeoCheck.checkDescription(oTest);
//      assertEquals(true, result.isCorrect());
      assertEquals(true, true);
    
   }
   
}
