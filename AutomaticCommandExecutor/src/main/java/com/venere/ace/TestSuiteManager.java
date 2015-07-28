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
package com.venere.ace;

import com.venere.ace.abstracts.AManager;
import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.dtos.ConfigurationDTO;
import com.venere.ace.exception.EManagerException;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.utility.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author quatrini
 */
public class TestSuiteManager {

   protected Log oLogger = LogFactory.getLog(getClass());
   protected Document users;

   public void startTestSuite(String sPropertiesFilePath) {



      long lStartTimeTest = System.currentTimeMillis();
      Properties oEnvironmentProperties = new OrderedProperties();
      InputStream in = null;
      Element suite = null;
      try {
         //XML REPORT      
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         users = docBuilder.newDocument();
         suite = users.createElement("testsuite");
         users.appendChild(suite);

         Attr failures = users.createAttribute("failures");
         failures.setValue("0");
         suite.setAttributeNode(failures);

         Attr time = users.createAttribute("time");
         time.setValue("0");
         suite.setAttributeNode(time);

         Attr errors = users.createAttribute("errors");
         errors.setValue("0");
         suite.setAttributeNode(errors);

         Attr skipped = users.createAttribute("skipped");
         skipped.setValue("0");
         suite.setAttributeNode(skipped);

         Attr tests = users.createAttribute("tests");
         tests.setValue("0");
         suite.setAttributeNode(tests);

         Attr name = users.createAttribute("name");
         name.setValue(sPropertiesFilePath);
         suite.setAttributeNode(name);

         //   
         in = (InputStream) this.getClass().getResourceAsStream(sPropertiesFilePath);
         if (in == null) {
            in = (InputStream) new FileInputStream(sPropertiesFilePath);
         }
      } catch (Exception ex) {
         oLogger.error("Error while try to open file " + sPropertiesFilePath);
      }
      try {
         oEnvironmentProperties.load(in);
      } catch (Exception ex) {
         oLogger.error("Error while try to read file " + sPropertiesFilePath);
      }
      try {
         in.close();
      } catch (IOException ex) {
         oLogger.error("Error while try to close file " + sPropertiesFilePath);
      }

      Set oSetTest = oEnvironmentProperties.entrySet();

      List<ResponseTest> oList = new ArrayList<ResponseTest>();
      int iTestCount = 0;
      String sTestName = "";
      String sCurrentResult = "";
      ResponseTest oResTest = new ResponseTest();
      for (Iterator it = oSetTest.iterator(); it.hasNext();) {
         try {
            iTestCount++;
            Entry oEntry = (Entry) it.next();
            sTestName = (String) oEntry.getKey();
            String sTestRequest = (String) oEntry.getValue();
            List<String> oInputTest = StringUtil.firstPipeSplit(sTestRequest);
            oResTest = playTest(sTestName, oInputTest.get(0), oInputTest.get(1));

         } catch (IndexOutOfBoundsException e) {
            String message = "Malformed test [" + sTestName + "]: the row must contain a = after the test name and a | after the test plan name. Test skipped";
            oResTest = setResultFail(message, new ResponseTest());
         } catch (Exception ex) {
            String message = "Test [" + sTestName + "] fail. Reason is: " + oResTest.getResponseMessage()!=null?oResTest.getResponseMessage():ex.getMessage();
            oResTest = setResultFail(message, new ResponseTest());
         }

         sCurrentResult = "TEST NAME : " + oResTest.getNameTest() + " TEST PLAN NAME : " + oResTest.getScenarioNameTest()
                 + " TEST NUMBER : " + iTestCount + " REMAINING TESTS : " + (oSetTest.size() - iTestCount);
         oLogger.info("*******************************************************");
         if (oResTest.isCorrect()) {
            oLogger.info("SUCCESS - " + sCurrentResult);
         } else {
            oLogger.warn("FAIL - " + sCurrentResult);
         }
         oLogger.info("*******************************************************");
         oList.add(oResTest);
         createReport(suite, sPropertiesFilePath, oResTest, iTestCount);
      }
      oLogger.info("################## TESTS RESULT ##############");
      int iCount = 0;

      boolean boIsSuccessTitleShown = false;
      for (Iterator<ResponseTest> oIt = oList.iterator(); oIt.hasNext();) {
         ResponseTest oResponseTest = oIt.next();
         if (oResponseTest.isCorrect()) {
            if (!boIsSuccessTitleShown) {
               oLogger.info("################## CORRECT TESTS");
               boIsSuccessTitleShown = true;
            }
            oLogger.info("TEST NAME :" + oResponseTest.getNameTest() + " TEST PLAN NAME : " + oResponseTest.getScenarioNameTest());
            iCount++;
         }
      }

      boolean boIsFailTitleShown = false;
      for (Iterator<ResponseTest> oIt = oList.iterator(); oIt.hasNext();) {
         ResponseTest oResponseTest = oIt.next();
         if (!oResponseTest.isCorrect()) {
            if (!boIsFailTitleShown) {
               oLogger.info("============================================");
               oLogger.info("============================================");
               oLogger.info("################## FAILURE TESTS");
               boIsFailTitleShown = true;
            }
            oLogger.info("TEST NAME :" + oResponseTest.getNameTest() + " TEST PLAN NAME : " + oResponseTest.getScenarioNameTest() + " REASON MESSAGE : " + oResponseTest.getResponseMessage());

         }
      }
      float fCorrectPercentage = ((float) iCount / (float) oList.size()) * 100;

      oLogger.info("Result Percentage " + fCorrectPercentage + " %. Number Correct Test " + iCount + "  on total test number " + oList.size());
      oLogger.info("Time of execution for " + oList.size() + " tests is " + ((System.currentTimeMillis() - lStartTimeTest) / 1000));


   }

   private void createReport(Element suite, String suiteName, ResponseTest oResTest, int iTestCount) {
      try {


         //TESTCASE
         Element testcase = users.createElement("testcase");
         suite.appendChild(testcase);

         Attr tcTime = users.createAttribute("time");
         tcTime.setValue("0");
         testcase.setAttributeNode(tcTime);

         Attr classname = users.createAttribute("classname");
         classname.setValue(suiteName);
         testcase.setAttributeNode(classname);

         Attr tcName = users.createAttribute("name");
         tcName.setValue(oResTest.getNameTest());
         testcase.setAttributeNode(tcName);

         if (!oResTest.isCorrect()) {
            Element failure = users.createElement("failure");
            testcase.appendChild(failure);

            Attr messageFail = users.createAttribute("message");
            messageFail.setValue("Test Failure");
            failure.setAttributeNode(messageFail);
            failure.appendChild(users.createTextNode(oResTest.getResponseMessage()));
         }

         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();

         DOMSource source = new DOMSource(users);
         String browserSelect = System.getProperty("com.venere.ace.browserSelect");
         String fileName = browserSelect+"Report.xml";
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

         String xmlPath = ".";
         boolean isFileCreated = new File(xmlPath, fileName).createNewFile();
        // oLogger.info("isFileCreated = " + isFileCreated);

         StreamResult result = new StreamResult(new File(xmlPath, fileName).getPath());

         transformer.transform(source, result);

      } catch (TransformerException ex) {
         Logger.getLogger(TestSuiteManager.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception e) {
         oLogger.error(e.getMessage());
      }


   }

   private String delayInputProp(String sStartDatesDelay, String sKey, String sValue) {
      if (sStartDatesDelay != null && (sKey.equals("com.venere.ace.checkin") || sKey.equals("com.venere.ace.checkout"))) {
         try {
            Integer.parseInt(sValue);
            int iDelayedDate = new Integer(sValue).intValue() + new Integer(sStartDatesDelay).intValue();
            sValue = "" + iDelayedDate;
         } catch (NumberFormatException e) {
            oLogger.warn("Delay not allowed for current input: " + sValue);
         }
      }
      return sValue;
   }

   private ResponseTest setResultFail(String message, ResponseTest oResTest) {
      oResTest.setIsCorrect(false);
      oResTest.setResponseMessage(message);
      oLogger.error(message);
      return oResTest;
   }

   private ResponseTest playTest(String sTestName, String sTestPlanName, String sProperties) {

      ClassPathXmlApplicationContext oClass = new ClassPathXmlApplicationContext(new String[]{"spring/spring-config.xml"});
      AManager oTestCaseManager = (AManager) oClass.getBean("testManager");
      String sStartDatesDelay = System.getProperty("com.venere.ace.datesDelay");
      String[] oPropertiesFile = sProperties.split(";");

      Properties oProps = new Properties();
      ResponseTest oResponse = new ResponseTest();
      oResponse.setIsCorrect(true);

      for (int i = 0; i < oPropertiesFile.length; i++) {
         if (oPropertiesFile[i].startsWith(Constants.PROPERTIES_TAG)) {
            if (!managePropertyInjection(oProps, oPropertiesFile[i])) {
               oResponse.setIsCorrect(false);
               oResponse.setResponseMessage("Injection properties failed due to malformed property line excepted format properties:{K1=V1,K2=v2}");
            }
         } else {
            Properties oTempProps = new Properties();
            String sString = oPropertiesFile[i];
            InputStream in;
            try {
               in = (InputStream) this.getClass().getResourceAsStream(sString);
               if (in == null) {

                  in = (InputStream) new FileInputStream(sString);
               }

               oTempProps.load(in);

               in.close();

               Set oEntryProps = oTempProps.entrySet();
               for (Iterator oIt = oEntryProps.iterator(); oIt.hasNext();) {
                  Entry oEntry = (Entry) oIt.next();
                  String sValue = delayInputProp(sStartDatesDelay, (String) oEntry.getKey(), (String) oEntry.getValue());
                  oProps.setProperty((String) oEntry.getKey(), sValue);

               }

            } catch (FileNotFoundException fnfEx) {
               oLogger.error(fnfEx);
               oResponse.setIsCorrect(false);
               oResponse.setResponseMessage("Missing file " + sString);
               break;
            } catch (Exception ex) {
               oLogger.error(ex);
               oResponse.setIsCorrect(false);
               oResponse.setResponseMessage(ex.getMessage());
               break;
            }

         }
      }

      oResponse.setNameTest(sTestName);
      oResponse.setScenarioNameTest(sTestPlanName);

      if (!oResponse.isCorrect()) {
         sTestPlanName = "tp_closeSession";
      }

      ConfigurationDTO oConfigurationDTO = new ConfigurationDTO();
      oConfigurationDTO.setPathFileToLoad(FolderManager.TestFolder + sTestPlanName + ".xml");
      oConfigurationDTO.setMainProps(oProps);
      oLogger.info("Start TEST :    " + sTestName);
      System.setProperty("com.venere.ace.currentTestCase", sTestName);
      try {

         List<ICommandResult> oResultList = oTestCaseManager.manager(oConfigurationDTO);
         ATaskResultDTO oTemDto = createTestSuiteResult(oResultList);
         oResponse.setIsCorrect(oTemDto.isCorrect());
         oResponse.setResponseMessage(oTemDto.getMessage());


      } catch (EManagerException ex) {
         oResponse.setIsCorrect(false);
         oResponse.setResponseMessage(ex.getMessage());
         try {
            oConfigurationDTO.setPathFileToLoad(FolderManager.TestFolder + "tp_closeSession.xml");
            oTestCaseManager.manager(oConfigurationDTO);
         } catch (EManagerException ex2) {
         }
      }

      oLogger.info("End TEST :    " + sTestName);
      oClass.close();
      return oResponse;
   }

   private boolean managePropertyInjection(Properties oProps, String sPropertyToInject) {
      //properties:{pageToLoad=http://www.venere.com/?ref=6074,DUE=JJJ}
      boolean isCorrectInjection = true;
      try {
         String sStartDatesDelay = System.getProperty("com.venere.ace.datesDelay");
         String[] sProperty = sPropertyToInject.split(Constants.PROPERTIES_TAG);
         String[] sElementsProperty = sProperty[1].substring(1, sProperty[1].length() - 1).split(Constants.PAIR_PROPERTIES_SEPARATOR);
         for (int i = 0; i < sElementsProperty.length; i++) {
            String[] sPairProperty = sElementsProperty[i].split(Constants.EQUALS_TAG, 2);
            String sKey = Constants.PREFIX_PROPERTIES + sPairProperty[0].trim();
            String sValue = delayInputProp(sStartDatesDelay, sKey, sPairProperty[1].trim());

            oProps.setProperty(sKey, sValue);
         }
      } catch (Exception ex) {
         isCorrectInjection = false;
         oLogger.error("Injection properties failed due to malformed property line, actual line " + sPropertyToInject + " , excepted format properties:{K1=V1,K2=v2}");
      }
      return isCorrectInjection;
   }

   private ATaskResultDTO createTestSuiteResult(List<ICommandResult> oResultList) {
      ATaskResultDTO oTestSuiteResult = new ATaskResultDTO("TestSuiteTaskResult");
      boolean isCorrectResult = true;
      boolean isStopped = false;
      oTestSuiteResult.setIsCorrect(true);
      oTestSuiteResult.setMessage("TestSuite executed");

      for (Iterator<ICommandResult> oIt = oResultList.iterator(); oIt.hasNext();) {
         ICommandResult oCurrResult = (ICommandResult) oIt.next();
         if (oCurrResult != null) {
            isCorrectResult &= oCurrResult.isCorrect();
            isStopped |= oCurrResult.isStopped();

            oTestSuiteResult.setIsCorrect(isCorrectResult);
            oTestSuiteResult.setIsStopped(isStopped);

            if (isStopped) {
               oTestSuiteResult.setMessage(oCurrResult.getMessage());
               break;
            }
         }
      }

      return oTestSuiteResult;
   }
}
class ResponseTest {

   private String sNameTest;
   private String sScenarioNameTest;
   private boolean isCorrect;
   private String sResponseMessage;

   public String getResponseMessage() {
      return sResponseMessage;
   }

   public void setResponseMessage(String responseMessage) {
      this.sResponseMessage = responseMessage;
   }

   public String getScenarioNameTest() {
      return sScenarioNameTest;
   }

   public void setScenarioNameTest(String sScenarioNameTest) {
      this.sScenarioNameTest = sScenarioNameTest;
   }

   public boolean isCorrect() {
      return isCorrect;
   }

   public void setIsCorrect(boolean isCorrect) {
      this.isCorrect = isCorrect;
   }

   public String getNameTest() {
      return sNameTest;
   }

   public void setNameTest(String sNameTest) {
      this.sNameTest = sNameTest;
   }
}