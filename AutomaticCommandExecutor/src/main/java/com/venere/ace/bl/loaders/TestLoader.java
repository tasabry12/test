/*                   Copyright (c) 2007 Venere Net S.p.A.
 *                             All Rights Reserved
 *
 * This software is the confidential and proprietary information of
 * Venere Net S.p.A. ("Confidential  Information"). You  shall not disclose
 * such  Confidential Information and shall use it only in accordance with
 * the terms  of the license agreement you entered into with Venere Net S.p.A.
 *
 * VENERE NET S.P.A. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR NON-INFRINGEMENT. VENERE NET S.P.A. SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS
 * SOFTWARE OR ITS DERIVATIVES.
 */

package com.venere.ace.bl.loaders;

import com.venere.ace.abstracts.ATestLoader;
import com.venere.ace.bl.SmokeTest;
import com.venere.ace.bl.TestTask;
import com.venere.ace.dtos.ConfigurationDTO;
import com.venere.ace.exception.ELoaderIO;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.utility.Constants;
import com.venere.ace.utility.EnumTestTask;
import com.venere.ace.utility.FolderManager;
import com.venere.utils.test.bl.TestManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author quatrini
 */
public class TestLoader  extends ATestLoader {
  
   private Map  oTestMap;
   private Map  oPropertiesMap;
   private String sCurrentMetaplanID;
   private String sCurrentTestInjectionsID;
   private String sDefaultTask = EnumTestTask.PERFORM.getActionCode();
   private TestManager oTestManager;
   
   public TestManager getTestManager() {
      return oTestManager;
   }

   public void setTestManager(TestManager oTestManager) {
      this.oTestManager = oTestManager;
   }
 
   @Override
   public void startDocument() throws SAXException {
      
   }

   @Override
   public void endDocument() throws SAXException {


   }



   @Override
   public void characters(char[] ch, int start, int len) {
       try {
         contents.write(new String(ch, start, len));
      } catch (IOException ex) {
      }
   }

   @Override
   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)  {
      contents.reset();
      if (localName.equals("testCase")) {
         oCurrentConfigurationDTO= new ConfigurationDTO();
         oCurrentTest =   new TestTask("TestTask");
         if (atts.getLength() != 0 ) {
            oCurrentTestID = atts.getValue("id");
            if(!isValidAttribute(oCurrentTestID)){
               oCurrentTestID = "UnknownAction";
            }
            String sCurrentTask = atts.getValue("task");
            if(!isValidAttribute(sCurrentTask)){
               sCurrentTask = sDefaultTask;
            }
            try{
               oCurrentTest.setTaskId(EnumTestTask.valueOf(sCurrentTask.trim().toUpperCase()));
            }catch(IllegalArgumentException ex){
               String sMessage = "Unknown task "+sCurrentTask.trim().toUpperCase();
               throw new ELoaderIO(ELoaderIO.LoaderErrorCode.TASK_UNKNOWN, sMessage, ex);

            }
            oCurrentScenarioID  = atts.getValue("scenarioId");
            if(!isValidAttribute(oCurrentScenarioID)){
               oCurrentScenarioID = "UnknownAction";
            }
            String sActualPropertiesPath  = atts.getValue("scenarioProperties");
//            if(!isValidAttribute(sActualPropertiesPath)){
//               oLogger.debug("Tag \"scenarioProperties\" missing! for test "+sCurrentTask);
////                       sActualPropertiesPath = "UnknownPropertiesFile";
//            }
            oCurrentTest.setTestID(oCurrentTestID);
            oCurrentTest.setScenarioID(oCurrentScenarioID);
            oCurrentTest.setHandler(oCurrentTestHandler);
            oCurrentTest.setMainProperties(oCurrentProperties);
            oCurrentConfigurationDTO.setMainProps(mergeWithCurrentProps(addPropertiesFiles(sActualPropertiesPath)));
            oCurrentConfigurationDTO.setPathFileToLoad(FolderManager.ScenariousFolder+oCurrentScenarioID+".xml");
            oCurrentTest.setRequest(oCurrentConfigurationDTO);
            oCommandExecutor.add(oCurrentTest);
         }
      }
      
      if (localName.equals("metaPlanProperties")) {
         oPropertiesMap = new HashMap();
      }

      if (localName.equals("propertiesFile")) {
         if (atts.getLength() != 0 ) {
             sCurrentMetaplanID = atts.getValue("metaPlanId");
            if(!isValidAttribute(sCurrentMetaplanID)){
               sCurrentMetaplanID = "UnknownAction";
            }
         }
      }

      if (localName.equals("testInjections")) {
         oTestMap = new HashMap();
      }

      if (localName.equals("testInjection")) {
         if (atts.getLength() != 0 ) {
            sCurrentTestInjectionsID = getValue(atts.getValue("injectionId"));            
            if(isValidAttribute(sCurrentTestInjectionsID)){
                  String[] sCheck = sCurrentTestInjectionsID.split(Constants.CHECKS_SEPARATOR);
                  for (int i = 0; i < sCheck.length; i++) {
                     sCurrentTestInjectionsID=sCheck[i].trim();
                     oTestManager.loadTestXml(sCurrentTestInjectionsID,oCurrentProperties);
                  }                  
            }
         }
      }
      
      if (localName.equals("pointCut")) {
         if (atts.getLength() != 0 ) {
            String sCurrentPointCut = atts.getValue("position");
            if(!isValidAttribute(sCurrentPointCut)){
               sCurrentPointCut = "UnknownAction";
            }
            List oListTestOnPointCut = (List) oTestMap.get(sCurrentPointCut);
            if(oListTestOnPointCut == null){
               oListTestOnPointCut = new ArrayList();
               oTestMap.put(sCurrentPointCut, oListTestOnPointCut);
            }
            
            String sCurrentTestInstance = atts.getValue("typeTest");
            if(!isValidAttribute(sCurrentTestInstance)){
               sCurrentTestInstance = "UnknownAction";
            }
            if(sCurrentTestInstance.equals("smoke")){
               ICommandExecutor oTask = new SmokeTest();
               oListTestOnPointCut.add(oTask);
            }
         }
      }

}

@Override
   public void endElement(String namespaceURI,
           String localName,
           String qName) throws SAXException {

      if (localName.equals("propertiesFile")) {
          String sPathPropertiesFile = getValue(contents.toString().trim());
          oPropertiesMap.put(sCurrentMetaplanID, addPropertiesFiles(sPathPropertiesFile));
      }
      if (localName.equals("metaPlanProperties")) {
          oCurrentTest.setPropertiesMap(oPropertiesMap);
      }

      if (localName.equals("testInjections")) {
         oCurrentTest.setTestMap(oTestMap);
      }

      if (localName.equals("testCase")) {
         Map oAddsMap = new HashMap();
         oAddsMap.put("properties", oPropertiesMap);
         oAddsMap.put("test", oTestMap);
         oCurrentConfigurationDTO.setConfigurationAdds(oAddsMap);
      }


   }
 


   private boolean isValidAttribute(String sAttributeValid){
      boolean boResult = true;
      if ( sAttributeValid == null ||  sAttributeValid.isEmpty()) {
         boResult = false;
      }
      return boResult;
   }

}
