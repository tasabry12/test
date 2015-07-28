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
package com.venere.utils.test.bl;

import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.utility.Constants;
import com.venere.ace.utility.ScreenUtil;
import com.venere.utils.dto.ResultContentDTO;
import static com.venere.utils.test.bl.CheckHelperContentBL.LOCATION_ELEMENT_POSITION;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 *
 * @author quatrini
 */
public class CheckHelperContentBL extends AClassTestBL {

   public static final int LOCATION_ELEMENT_POSITION = 0;
      
   
   public CheckHelperContentBL() {
      super("CheckHelperContentBL");
   }

   public Map<String, Object> getMapResultContainer() {
      return oMapResultContainer;
   }

   /**
    * Control if an element is present/visible X times If the caller method name
    * is COUNTALLELEMENTS the elements are counted also if aren't visible
    *
    * @param oTest hold all information to perform test
    *
    * @return ICommandResult with test result
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkCountElement(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      if (oListelement != null && oListelement.size() >= 2) {
         String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
         String sExceptedNumberElement = (String) oListelement.get(1);
         HelperHtmlBL.setLocatorType(oTest.getLocatorType());
         
         if (sExceptedNumberElement.contains("{")) {
            sExceptedNumberElement = sExceptedNumberElement.replace("{", "").replace("}", "");
            sExceptedNumberElement = (String) oMapResultContainer.get(sExceptedNumberElement);
            if (sExceptedNumberElement == null) {
               sExceptedNumberElement = "";
               oLogger.warn("Missing element in map , i look for  {" + oListelement.get(1) + "}. This field will feel with blank ");
            }
         }

         boolean boShowsOnlyDisplayed = true;
         if ("COUNTALLELEMENTS".equalsIgnoreCase(oTest.getMethodName())) {
            boShowsOnlyDisplayed = false;
         }
         boolean isNumber = true;
         int iExceptedNumberElement = 0;
         try {
            iExceptedNumberElement = Integer.parseInt(sExceptedNumberElement);
         } catch (Exception ex) {
            isNumber = false;
         }
         try {
            ResultContentDTO oResults = null;
            if (!isNumber) {
               oResults = HelperHtmlBL.getElements(oBrowser, sExceptedNumberElement);
               for (Object object : oResults.getElements()) {
                  if (((WebElement) object).isDisplayed() || !boShowsOnlyDisplayed) {
                     iExceptedNumberElement++;
                  }
               }
            }
            oResults = HelperHtmlBL.getElements(oBrowser, sElementPosition);
            int iCountNumberElements = 0;
            for (Object object : oResults.getElements()) {
               if (((WebElement) object).isDisplayed() || !boShowsOnlyDisplayed) {
                  iCountNumberElements++;
               }
            }
            if (oResults.getElements() != null && iCountNumberElements == iExceptedNumberElement) {
               sMessageResult += "Element " + sElementPosition + " : Present " + iExceptedNumberElement + " times as Expected";
            } else {
               boResultTest = false;
               if (oResults.getElements() != null) {
                  sMessageResult += "Element " + sElementPosition + " : Present " + iCountNumberElements + " times but excepected " + iExceptedNumberElement + " times";
               } else {
                  sMessageResult += "Element " + sElementPosition + " : Not Found";
               }
            }
         } catch (Exception ex) {
            String sMessage = ex.getMessage();
            setExceptionMessageResult(oTest);
            oLogger.error(sMessageResult);
            oLogger.error(sMessage);
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
         }
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;



   }

   /**
    * Control if an element is present/visible X times If the caller method name
    * is COUNTALLELEMENTS the elements are counted also if aren't visible
    *
    * @param oTest hold all information to perform test
    *
    * @return ICommandResult with test result
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkNonEmptyElements(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      if (oListelement != null && oListelement.size() >= 2) {
         String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
         String sExceptedNumberElement = (String) oListelement.get(1);
         HelperHtmlBL.setLocatorType(oTest.getLocatorType());
         
         
         if (sExceptedNumberElement.contains("{")) {
            sExceptedNumberElement = sExceptedNumberElement.replace("{", "").replace("}", "");
            sExceptedNumberElement = (String) oMapResultContainer.get(sExceptedNumberElement);
            if (sExceptedNumberElement == null) {
               sExceptedNumberElement = "";
               oLogger.warn("Missing element in map , i look for  {" + oListelement.get(1) + "}. This field will feel with blank ");
            }
         }

         boolean boShowsOnlyDisplayed = true;
         if ("COUNTNOTEMPTYELEMENTS".equalsIgnoreCase(oTest.getMethodName())) {
            boShowsOnlyDisplayed = false;
         }
         boolean isNumber = true;
         int iExceptedNumberElement = 0;
         try {
            iExceptedNumberElement = Integer.parseInt(sExceptedNumberElement);
         } catch (Exception ex) {
            isNumber = false;
         }
         try {
            ResultContentDTO oResults = null;

            if (!isNumber) {
               oResults = HelperHtmlBL.getElements(oBrowser, sExceptedNumberElement);
               //CONTROLLA LA NUMEROSITA' DI UN ALTRO ELEMENTO
               for (Object oWeb : oResults.getElements()) {
                  if (((WebElement) oWeb).isDisplayed() || !boShowsOnlyDisplayed) {
                     String s = (String) ((JavascriptExecutor) oBrowser).executeScript("return arguments[0].textContent", oWeb);
                     String[] Strings = s.split("\n");
                     if (Strings.length >= 2) {
                        iExceptedNumberElement++;
                     }
                  }
               }
            }
            oResults = HelperHtmlBL.getElements(oBrowser, sElementPosition);
            int iCountNumberElements = 0;
            for (Object oWeb : oResults.getElements()) {
               if (((WebElement) oWeb).isDisplayed() || !boShowsOnlyDisplayed) {
                  String s = (String) ((JavascriptExecutor) oBrowser).executeScript("return arguments[0].textContent", oWeb);
                  String[] Strings = s.split("\n");
                  if (Strings.length >= 2) {
                     iCountNumberElements++;
                  }

               }
            }
            if (oResults.getElements() != null && iCountNumberElements == iExceptedNumberElement) {
               sMessageResult += "Element " + sElementPosition + " : Present " + iExceptedNumberElement + " times as Expected";
            } else {
               boResultTest = false;
               if (oResults.getElements() != null) {
                  sMessageResult += "Element " + sElementPosition + " : Present " + iCountNumberElements + " times but excepected " + iExceptedNumberElement + " times";
               } else {
                  sMessageResult += "Element " + sElementPosition + " : Not Found";
               }
            }
         } catch (Exception ex) {
            String sMessage = ex.getMessage();
            setExceptionMessageResult(oTest);
            oLogger.error(sMessageResult);
            oLogger.error(sMessage);
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
         }
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;



   }

   


   /**
    * Exists Element on Page
    *
    * @param oTest that hold information about test
    *
    * @return ICommandResult with test result
    *
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkExistElement(ITestRequestDTO oTest) throws EHandlerException {
      
      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      if (oListelement != null && oListelement.size() >= 1) {

         String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
         HelperHtmlBL.setLocatorType(oTest.getLocatorType());
         try {
            String sTypologyTest = "positive";
            if (oListelement.size() == 2) {
               sTypologyTest = (String) oListelement.get(1);
            }
            ResultContentDTO oResults = HelperHtmlBL.getElement(oBrowser, sElementPosition);

            if (oResults.getElement() != null && sTypologyTest.equals("positive")) {
               sMessageResult += "Element " + sElementPosition + " : Present as Expected";
            } else {
               if (oResults.getElement() == null && sTypologyTest.equals("negative")) {
                  sMessageResult += "Element " + sElementPosition + " : Missing as Expected";
               } else {
                  boResultTest = false;
                  if (sTypologyTest.equals("positive")) {
                     sMessageResult += "Element " + sElementPosition + " : Missing but Expected";
                  } else {
                     sMessageResult += "Element " + sElementPosition + " : Present but not Expected ";
                  }
               }
            }

         } catch (Exception ex) {
            String sMessage = ex.getMessage();
            setExceptionMessageResult(oTest);
            oLogger.error(sMessageResult);
            oLogger.error(sMessage);
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
         }
      }
      ATaskResultDTO oResult = populateResult(oTest);
      
      
      
      return oResult;
   }

   public ATaskResultDTO checkElementStyle(ITestRequestDTO oTest) throws EHandlerException {

      //lista : [0] xpath es. //div[@id='payment_method']
      //lista : [1] style property da confrontare
      //lista : [2] positive| negative optional 

      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      String sAttributeName = "style";

      boResultTest = false;
      String sTypologyTest = "positive";

      try {

         if (oListelement != null && oListelement.size() >= 1) {

            String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
            HelperHtmlBL.setLocatorType(oTest.getLocatorType());
            
            if (oListelement.size() == 3) {
               sTypologyTest = (String) oListelement.get(2);
            }

            ResultContentDTO oResults = HelperHtmlBL.getElement(oBrowser, sElementPosition);

            if (oResults.getElement() != null) {
               WebElement oElement = (WebElement) oResults.getElement();
               String sStyleValue = oElement.getAttribute(sAttributeName);

               if (sStyleValue == null || "".equals(sStyleValue)) {
                  sMessageResult += "Attribute " + sAttributeName + "not found";
                  boResultTest = false;
               } else {
                  String sTrimmedLWStyle = sStyleValue.replace(" ", "").toLowerCase();
                  String sTrimmedLWInputStyle = ((String) oListelement.get(1)).replace(" ", "").toLowerCase();

                  boResultTest = (sTrimmedLWStyle.contains(sTrimmedLWInputStyle) && "positive".equals(sTypologyTest))
                          || (!sTrimmedLWStyle.contains(sTrimmedLWInputStyle) && "negative".equals(sTypologyTest)) ? true : false;

                  if ("positive".equals(sTypologyTest)) {
                     sMessageResult += "Attribute " + sAttributeName + " " + oListelement.get(1) + (boResultTest ? " : Present as Expected" : " : Missing but Expected");
                  } else {
                     sMessageResult += "Attribute " + sAttributeName + " " + oListelement.get(1) + (boResultTest ? " : Missing as Expected" : " : Something is wrong");
                  }
               }
            } else {
               sMessageResult += "Tag node for element " + oListelement.get(0) + " not found";
            }
         }

      } catch (Exception ex) {
         String sMessage = ex.getMessage();
         setExceptionMessageResult(oTest);
         oLogger.error(sMessageResult);
         oLogger.error(sMessage);
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(oTest.getMethodName());
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }

      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;
   }

   public ATaskResultDTO checkCssValue(ITestRequestDTO oTest) throws EHandlerException {

      //lista : [0] xpath es. //div[@id='payment_method']
      //lista : [1] css element da confrontare (elemento=valore)
      //lista : [2] positive| negative optional 

      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);

      String[] sGropElements = ((String) oListelement.get(1)).split(";");

      boResultTest = false;
      String sTypologyTest = "positive";

      try {
         boolean boGroupTestResult = false;
         if (oListelement != null && oListelement.size() >= 1) {

            for (String sCssPairElement : sGropElements) {
               String[] sCssElementValue = sCssPairElement.split("=");
               String sCssElement = sCssElementValue[0];
               String sCssValue = sCssElementValue[1];

               String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
               HelperHtmlBL.setLocatorType(oTest.getLocatorType());
               
               if (oListelement.size() == 3) {
                  sTypologyTest = (String) oListelement.get(2);
               }

               ResultContentDTO oResults = HelperHtmlBL.getElement(oBrowser, sElementPosition);

               if (oResults.getElement() != null) {
                  WebElement oElement = (WebElement) oResults.getElement();
                  String sStyleValue = oElement.getCssValue(sCssElement);

                  if (sStyleValue == null || "".equals(sStyleValue)) {
                     sMessageResult += "Css " + sCssElement + "not found";
                     boGroupTestResult |= false;
                  } else {
                     String sTrimmedLWStyle = sStyleValue.replace(" ", "").toLowerCase();
                     String sTrimmedLWInputStyle = (sCssValue).replace(" ", "").toLowerCase();

                     boGroupTestResult |= (sTrimmedLWStyle.contains(sTrimmedLWInputStyle) && "positive".equals(sTypologyTest))
                             || (!sTrimmedLWStyle.contains(sTrimmedLWInputStyle) && "negative".equals(sTypologyTest)) ? true : false;

                     if ("positive".equals(sTypologyTest)) {
                        sMessageResult += "Css Element " + oListelement.get(0) + " with " + oListelement.get(1) + (boGroupTestResult ? " : Present as Expected" : " : Missing but Expected. Found Element with value " + sStyleValue);
                     } else {
                        sMessageResult += "Css Element " + oListelement.get(0) + " with " + oListelement.get(1) + (boGroupTestResult ? " : Missing as Expected" : " : Present but Not Expected");
                     }
                  }
               } else {
                  if ("positive".equals(sTypologyTest)) {
                     boGroupTestResult |= false;
                     sMessageResult += "Tag node for element " + oListelement.get(0) + " : Missing but Expected.";
                  } else {
                     boGroupTestResult |= true;
                     sMessageResult += "Tag node for element " + oListelement.get(0) + " : Missing as Expected";
                  }
               }
            }
         }
         boResultTest = boGroupTestResult;
      } catch (Exception ex) {
         String sMessage = ex.getMessage();
         setExceptionMessageResult(oTest);
         oLogger.error(sMessageResult);
         oLogger.error(sMessage);
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(oTest.getMethodName());
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }

      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;
   }

   /**
    * Exists Text into element tag
    *
    * @param oTest that hold information about test
    *
    * @return ICommandResult with test result
    *
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkExistText(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      ATaskResultDTO oResult = (ATaskResultDTO) compareText(oTest, HelperHtmlBL.getTextElement(oBrowser, (String) oTest.getInputDataList().get(LOCATION_ELEMENT_POSITION)).getText(), Constants.COMPARATION_EQUALS);
      return oResult;
   }

   /**
    * Check if a text is contained and not necessary equals to the text present
    * into element tag
    *
    * @param oTest that hold information about test
    * @return ICommandResult with test result
    * @throws EHandlerException
    */
   public ICommandResult checkIsContainedText(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      HelperHtmlBL.setLocatorType(oTest.getLocatorType());
      ATaskResultDTO oResult =null;
      String sPosition = (String) oTest.getInputDataList().get(LOCATION_ELEMENT_POSITION);
      if(sPosition.equals("//html")){
         //*** If you need to select the source code you need to extract the Source With Page Source Method
         oResult = (ATaskResultDTO) compareText(oTest, oBrowser.getPageSource(), Constants.COMPARATION_CONTAINED);
      }else{
         oResult = (ATaskResultDTO) compareText(oTest, HelperHtmlBL.getTextElement(oBrowser, (String) oTest.getInputDataList().get(LOCATION_ELEMENT_POSITION)).getText(), Constants.COMPARATION_CONTAINED);
      }
      return oResult;
   }
   
    /**
    * Check selected option text from select
    *
    * @param oTest that hold information about test
    *
    * @return ICommandResult with test result
    *
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkExistSelectedText(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      HelperHtmlBL.setLocatorType(oTest.getLocatorType());
      ATaskResultDTO oResult = (ATaskResultDTO) compareText(oTest, HelperHtmlBL.getTextSelectedElement(oBrowser, (String) oTest.getInputDataList().get(LOCATION_ELEMENT_POSITION)).getText(), Constants.COMPARATION_EQUALS);
      return oResult;
   }

   
   /**
    * Exists Element on Page URL
    *
    * @param oTest that hold information about test
    *
    * @return ICommandResult with test result
    *
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkURLElement(ITestRequestDTO oTest) throws EHandlerException {

      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      if (oListelement != null && oListelement.size() >= 1) {
         String sElement = (String) oListelement.get(0);
         HelperHtmlBL.setLocatorType(oTest.getLocatorType());
         try {
            String sTypologyTest = "positive";
            if (oListelement.size() == 2) {
               sTypologyTest = (String) oListelement.get(1);
            }
            ResultContentDTO oResults = HelperHtmlBL.isElementInURL(oBrowser, sElement);

            if ((Boolean) oResults.getElement() && sTypologyTest.equals("positive")) {
               sMessageResult += "Element " + sElement + " : Present as Expected";
            } else {
               if (!(Boolean) oResults.getElement() && sTypologyTest.equals("negative")) {
                  sMessageResult += "Element " + sElement + " : Missing as Expected";
               } else {
                  boResultTest = false;
                  if (sTypologyTest.equals("positive")) {
                     sMessageResult += "Element " + sElement + " : Missing but Expected";
                  } else {
                     sMessageResult += "Element " + sElement + " : Something is wrong";
                  }
               }
            }

         } catch (Exception ex) {
            String sMessage = ex.getMessage();
            setExceptionMessageResult(oTest);
            oLogger.error(sMessageResult);
            oLogger.error(sMessage);
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
         }
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;
   }

   /**
    * Retrieve text from page and compare with input string. If the comparator
    * input parameter is null, the result will be true (boResultExistText=true)
    * if the text is contained or equals
    *
    * @param oTest that hold information about test
    * @param sTextGettedfromPage Text taken from page
    * @return
    * @throws EHandlerTaskPerforming
    */
   private ICommandResult compareText(ITestRequestDTO oTest, String sTextGettedfromPage, String comparation) throws EHandlerException {

      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      if (oListelement != null && oListelement.size() >= 2) {
         String sElementPosition = (String) oListelement.get(LOCATION_ELEMENT_POSITION);         
         try {
            String sTypologyTest = "positive";
            if (oListelement.size() == 3) {
               sTypologyTest = (String) oListelement.get(2);
            }
            String sExceptedText = (String) oListelement.get(1);
            if (sExceptedText.contains("{")) {
               sExceptedText = sExceptedText.replace("{", "").replace("}", "");
               sExceptedText = (String) oMapResultContainer.get(sExceptedText);
               if (sExceptedText == null) {
                  sExceptedText = "";
                  oLogger.warn("Missing element in map , i look for  {" + oListelement.get(1) + "}. This field will feel with blank ");
               }

            }
            boolean boResultExistText = false;
            if (sTextGettedfromPage != null) {
               if (Constants.COMPARATION_EQUALS.equals(comparation)) {
                  boResultExistText = sTextGettedfromPage.equalsIgnoreCase(sExceptedText);
               } else if (Constants.COMPARATION_CONTAINED.equals(comparation)) {
                  boResultExistText = (sTextGettedfromPage.toLowerCase()).contains(sExceptedText.toLowerCase());
               } else {
                  boResultExistText = sTextGettedfromPage.equalsIgnoreCase(sExceptedText) || sTextGettedfromPage.contains(sExceptedText);
               }
            }

            if (boResultExistText && sTypologyTest.equals("positive")) {
               sMessageResult += "Text " + sExceptedText + " present as Expected in Element " + sElementPosition;
            } else {
               if (!boResultExistText && sTypologyTest.equals("negative")) {
                  sMessageResult += "Text " + sExceptedText + " missing as Expected in Element " + sElementPosition;
               } else {
                  boResultTest = false;
                  if (sTypologyTest.equals("positive")) {
                     sMessageResult += "Text " + sExceptedText + " missing but Expected in Element " + sElementPosition;
                  } else {
                     sMessageResult += "Text " + sExceptedText + " present but not Exceptedc in" + sElementPosition ;
                  }
               }
            }
         } catch (Exception ex) {
            String sMessage = ex.getMessage();
            setExceptionMessageResult(oTest);
            oLogger.error(sMessageResult);
            oLogger.error(sMessage);
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
         }
      } else {
         boResultTest = false;
         sMessageResult = "To perform this Check you need at least 3 element.Page, xpath and text to compare are mandatory and optional value is positive or negative ";
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;

   }

   /**
    * Verifies that the content of the attribute is not empty.
    *
    * @param oTest
    * @return
    * @throws EHandlerException
    */
   public ICommandResult checkIsNotEmptyAttribute(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      oListelement.add(2, "");
      oListelement.add(3, "negative");
      oTest.setInputDataList(oListelement);
      return checkAttributeValue(oTest);

   }

   /**
    * Verifies that the content of the attribute exist and is as expected Test
    * pass if: (positive check) exist and has desired value OR (negative) exist
    * and has any other value than the explicit one.
    *
    *
    * @param oTest
    * @return
    * @throws EHandlerException
    */
   public ICommandResult checkAttributeValue(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      sMessageResult = initializeMessageResult(oTest);
      String sPositionElement = (String) oListelement.get(LOCATION_ELEMENT_POSITION);
      String sAttribute = (String) oListelement.get(1);
      String sExplicitedValue = (String) oListelement.get(2);
      HelperHtmlBL.setLocatorType(oTest.getLocatorType());
      String sTypologyTest = "positive";

      if (oListelement.size() == 4) {
         sTypologyTest = (String) oListelement.get(3);
      }
      boResultTest = false;
      boolean bIsPositiveCheck = false;
      if (sTypologyTest.equals("positive")) {
         bIsPositiveCheck = true;
      } else if (sTypologyTest.equals("negative")) {
         bIsPositiveCheck = false;
      }
      ResultContentDTO oResultContent = HelperHtmlBL.getElement(oBrowser, sPositionElement);
      if (oResultContent.getElement() != null) {
         String sContentAttribute = ((WebElement) oResultContent.getElement()).getAttribute(sAttribute);
         if (bAttributeExistCheck(sContentAttribute, "positive")) {
            if (bIsPositiveCheck && sContentAttribute.equals(sExplicitedValue)
                    || !bIsPositiveCheck && !sContentAttribute.equals(sExplicitedValue)) {
               boResultTest = true;
               sMessageResult += sAttribute + " for " + sPositionElement + " " + sContentAttribute + " as Expected";
            } else {
               boResultTest = false;
               sMessageResult += sAttribute + " for " + sPositionElement + " " + sContentAttribute + " not as Expected";
            }
         } else {
            sMessageResult += sAttribute + " for " + sPositionElement + " is null";
         }
      } else {
         boResultTest = false;
         sMessageResult += "Element " + sPositionElement + " Missing";
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;
   }

   private boolean bAttributeExistCheck(String sAttributeValue, String sIsPositiveCheck) {
      boolean bResult = false;
      if ((sAttributeValue != null && sIsPositiveCheck.equals("positive"))
              || (sAttributeValue == null && sIsPositiveCheck.equals("negative"))) {
         bResult = true;
      } else if (!sIsPositiveCheck.equals("negative") || !sIsPositiveCheck.equals("positive")) {
         sMessageResult += "[" + sIsPositiveCheck + "] Not recognized as checks option";
         bResult = false;
      }
      return bResult;
   }


}
