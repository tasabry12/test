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

import com.venere.ace.abstracts.AHandler;
import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ITestContainer;
import com.venere.ace.utility.ScreenUtil;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author cquatrini
 */
public class AClassTestBL extends AHandler implements ITestContainer {

   protected Map<String, Object> oMapResultContainer;
//   protected Log       oLogger = LogFactory.getLog(getClass());
   protected WebDriver oBrowser;
   protected boolean boResultTest = true;
   protected String sMessageResult = "";
   protected List oListelement = null;

   AClassTestBL(String sClassName) {
      super(sClassName);
   }

   @Override
   public void setMapResultContainer(Map<String, Object> oMapResultContainer) {
      this.oMapResultContainer = oMapResultContainer;
   }

   @Override
   public void setTestBrowser(WebDriver oWebDirver) {
      oBrowser = oWebDirver;
   }

   /**
    * Validate input for test
    *
    * @param oTest that hold input parameters
    */
   protected void checkInput(ITestRequestDTO oTest) {
      boResultTest = true;
      oListelement = oTest.getInputDataList();

      if (oListelement != null) {
         sMessageResult = "Something is wrong. Input data size " + oListelement.size() + ". ";
      } else {
         sMessageResult = "Something is wrong. Missing Input Data";
      }

   }

   @Override
   public WebDriver getTestBrowser() {
      return oBrowser;
   }

   protected String initializeMessageResult(ITestRequestDTO oTest) {
      return "Check " + oTest.getCheckDescription() + " - ";
   }

   protected ATaskResultDTO populateResult(ITestRequestDTO oTest) {
      ATaskResultDTO oResult = new ATaskResultDTO("TaskResult");
      boolean boIsCondition = oTest.getConditionDTO() != null;
      if (boIsCondition) {
         oResult.setCondition(oTest.getConditionDTO());
         oResult.getCondition().setConditionResult(boResultTest);
         sMessageResult = "Condition with ID " + oTest.getConditionDTO().getConditionId() + " is " + boResultTest + ". " + sMessageResult;

      }

      oResult.setIsCorrect(boResultTest);
      oResult.setMessage(sMessageResult);
      if (boResultTest || boIsCondition) {
         oLogger.info(oResult.getMessage());
      } else {
         oLogger.error(oResult.getMessage());
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         try {
            oUtil.makeAPrintScreen(oTest.getMethodName());
         } catch (EHandlerException ex) {
            Logger.getLogger(CheckHelperContentBL.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      return oResult;
   }
   
   protected void setExceptionMessageResult(ITestRequestDTO oTest) {
      sMessageResult += ". Someting Wrong in " + this.getClass().getName() + ". During test " + oTest.getMethodName();
   }
   
}
