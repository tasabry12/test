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
import com.venere.utils.dto.ResultContentDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.knallgrau.utils.textcat.TextCategorizer;
import org.openqa.selenium.WebElement;

/**
 *
 * @author quatrini
 */
public class SeoCheckBL extends AClassTestBL {

   private Map oMapLanguages = new HashMap<String, String>() {

      {
         put("english", "en");
         put("french", "fr");
         put("spanish", "es");
         put("german", "de");
         put("italian", "it");

      }
   };
   private String sTitleElement = "//title";
   private String sMetaTagRobots = "//meta[@content='noindex,follow']";
   private String sMetaTagDescription = "//meta[@name='Description']";
   private String sMetaTagCanonical = "//head//link[@rel='canonical']";
   private String sDescriptionBody = "//div[@id='hotel-description-body']";
   String sPrintScreenFilePath;
   //private ITestRequestDTO oTest;

   public SeoCheckBL() {
      super("SeoCheckBL");
   }

   public Map<String, Object> getMapResultContainer() {
      return oMapResultContainer;
   }

   //TODO REFACTORING
   public ICommandResult checkTitle(ITestRequestDTO oTest) throws EHandlerException {
      ATaskResultDTO oResult = new ATaskResultDTO("SeoCheckTaskResult");
      oResult.setIsCorrect(false);

      ResultContentDTO oResultContent = HelperHtmlBL.getElements(oBrowser, sTitleElement);

      if (oResultContent != null && !oResultContent.getText().isEmpty()) {
         try {
            checkUrlvsTitle(new URL(oBrowser.getCurrentUrl()), oResultContent.getText());
            oResult.setIsCorrect(true);
         } catch (MalformedURLException ex) {
            oLogger.error("Malformed url finded " + oBrowser.getCurrentUrl());
            oResult.setIsCorrect(false);
         }

      }
      oLogger.info("checkTitle : " + oResult.isCorrect());
      oResult.setMessage("SEO Check Done");
      return oResult;
   }

   
   public ICommandResult checkMetaTag(ITestRequestDTO oTest) throws EHandlerException {
      ATaskResultDTO oResult = new ATaskResultDTO("SeoCheckTaskResult");
      oResult.setIsCorrect(true);
      ResultContentDTO oResultContent = HelperHtmlBL.getElement(oBrowser, sMetaTagRobots);
      String sMessage = "Check " + oTest.getCheckDescription() + " - ";
      boolean boHaveQueryString = false;
      try {
         boHaveQueryString = (new URL(oBrowser.getCurrentUrl()).getQuery() != null);
      } catch (MalformedURLException ex) {
         oLogger.error("Malformed url finded " + oBrowser.getCurrentUrl());
         oResult.setIsCorrect(false);
      }
      boolean boSameLg = checkDescriptionLgVsUrlLg();
      if (oResultContent.getElement() != null) {
         sMessage += "noindex follow Element Found. ";
         if (!boHaveQueryString) {
            oResult.setIsCorrect(!boSameLg);
         }
      } else {
         sMessage += "noindex follow Element Not Found. ";
         if (boHaveQueryString || !boSameLg) {
            oResult.setIsCorrect(false);
         }
      }
      sMessage += " Have Query String=" + boHaveQueryString + "- Same Language=" + boSameLg;
      oResult.setMessage(sMessage);
      if (oResult.isCorrect()) {
         oLogger.info(sMessage);
      } else {
         oLogger.error(sMessage);
      }
      return oResult;

   }

   // al posto di questo metodo si usa checkIsNotEmptyAttribute di CheckHelperContentBL
   public ICommandResult checkDescription(ITestRequestDTO oTest) throws EHandlerException {
      ATaskResultDTO oResult = new ATaskResultDTO("SeoCheckTaskResult");
      oResult.setIsCorrect(false);

      ResultContentDTO oResultContent = HelperHtmlBL.getElements(oBrowser, sMetaTagDescription);

      if (oResultContent != null && oResultContent.getText().isEmpty()) {
         oResultContent = HelperHtmlBL.getElements(oBrowser, sMetaTagDescription.toLowerCase());
      }

      if (oResultContent != null) {
         oResult.setIsCorrect(!oResultContent.getText().isEmpty());
      }

      oResult.setMessage("Description Check " + (oResult.isCorrect() ? "Done" : "Failure. Missing Tag"));
      oLogger.info("checkDescription : " + oResult.isCorrect());
      return oResult;
   }

   public ICommandResult checkCanonicalTag(ITestRequestDTO oTest) throws EHandlerException {
      ATaskResultDTO oResult = new ATaskResultDTO("SeoCheckTaskResult");
      oResult.setIsCorrect(false);
      String sMessage = "Check " + oTest.getCheckDescription() + " - ";
      ResultContentDTO oResultContent = HelperHtmlBL.getElement(oBrowser, sMetaTagCanonical);

      if (oResultContent.getElement() == null) {
         oResultContent = HelperHtmlBL.getElement(oBrowser, sMetaTagCanonical.toLowerCase());
      }

      String sCleanBrowserUrl = "";
      String sUrl = "";

      URL oBrowserUrl = null;
      try {
         oBrowserUrl = new URL(oBrowser.getCurrentUrl());
      } catch (MalformedURLException ex) {
         oLogger.error("Malformed url finded " + oBrowser.getCurrentUrl());
         oResult.setIsCorrect(false);
      }
      sCleanBrowserUrl = oBrowserUrl.getProtocol() + "://" + oBrowserUrl.getHost() + oBrowserUrl.getPath();
      if (oResultContent.getElement() != null) {
         sUrl = ((WebElement) oResultContent.getElement()).getAttribute("href");
         oResult.setIsCorrect(sCleanBrowserUrl.equals(sUrl));
      }
      oResult.setMessage(oTest.getCheckDescription() + ((oResult.isCorrect() ? " Done: Browser URL is equal to Canonical URL" : "Failure Excepted :" + sCleanBrowserUrl + " but found :" + sUrl)));

      if (oResult.isCorrect()) {
         oLogger.info(oResult.getMessage());
      } else {
         oLogger.error(oResult.getMessage());
      }
      return oResult;
   }

   private void checkUrlvsTitle(URL oUrl, String sTitle) {

      String[] sUrlComponets = oUrl.getPath().split("/");
      if (sUrlComponets.length > 1) {
         String sLastChild = sUrlComponets[sUrlComponets.length - 1].replace("-", " ").toLowerCase();
         if (!sTitle.toLowerCase().contains(sLastChild)) {
            oLogger.warn("Title present but may be wrong. Current Url :" + oUrl.toString() + " and title " + sTitle);
         }
      }
   }

   private boolean checkUrlLanguage(URL sOriginUrl, String sLanguage) {
      boolean boResult = false;

      if (sLanguage != null) {
         if (sLanguage.equals("en")) {
            String[] sPathElement = sOriginUrl.getPath().split("/");
            if (sPathElement.length > 2) {
               boResult |= (sPathElement[1].length() != 2);
            }
         } else {
            boResult |= !sOriginUrl.getPath().contains("/" + sLanguage + "/");
         }
      } else {
         boResult = true;
         oLogger.warn("ATTENTION: System can't identify the right language, this check will be skipped ");
      }


      return boResult;
   }

   private boolean checkDescriptionLgVsUrlLg() throws EHandlerException {
      boolean boResult = false;


      ResultContentDTO oResultContent = HelperHtmlBL.getElement(oBrowser, sDescriptionBody);

      if (oResultContent.getElement() != null && !(((WebElement) oResultContent.getElement()).getText()).isEmpty()) {
         TextCategorizer oLanguageReognizer = new TextCategorizer();
         String sLanguage = (String) oMapLanguages.get(oLanguageReognizer.categorize(((WebElement) oResultContent.getElement()).getText()));
         try {
            boResult |= checkUrlLanguage(new URL(oBrowser.getCurrentUrl()), sLanguage);
         } catch (MalformedURLException ex) {
            boResult = false;
            oLogger.error("Malformed url finded " + oBrowser.getCurrentUrl());
         }
      } else {
         boResult = true;
         //oLogger.error("No Tag Body found  into text for this url " + oBrowser.getCurrentUrl() );
      }

      return boResult;
   }

   //TODO Refactoring
   public ICommandResult checkLinks(ITestRequestDTO oTestRequest) throws EHandlerException {
      ATaskResultDTO oResult = new ATaskResultDTO("SeoCheckTaskResult");
      oResult.setIsCorrect(true);

      String sXpathLinksToCheck = (String) oTestRequest.getInputDataList().get(1);
      String sStartLinks = (String) oTestRequest.getInputDataList().get(2);

      ResultContentDTO oResultContent = HelperHtmlBL.getElements(oBrowser, sXpathLinksToCheck);

      oResult.setFilePrintScreen(sPrintScreenFilePath);
      for (Object oB1 : oResultContent.getElements()) {
         WebElement oWebEl = (WebElement) oB1;
         String sUrl = oWebEl.getAttribute("href");
         System.out.println(sUrl);
         if (!sUrl.startsWith(sStartLinks)) {
            oResult.setIsCorrect(false);
            oResult.setMessage("Expected Starting url " + sStartLinks + " but found :" + sUrl);
         }

      }


      return oResult;

   }
}