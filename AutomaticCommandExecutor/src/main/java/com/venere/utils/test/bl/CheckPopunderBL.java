/*                   Copyright (c) 2013 Venere Net S.p.A.
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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author elandolfi
 */
public class CheckPopunderBL extends AClassTestBL {


   public CheckPopunderBL() {
      super ("CheckPopunderBL");
   }

   public Map<String, Object> getMapResultContainer() {
      return oMapResultContainer;
   }

   

   /**
    * Control if a popunder is shown
    *
    * @param oTest that hold information about test
    *
    * @return ICommandResult with test result
    *
    * @throws EHandlerTaskPerforming
    */
   public ICommandResult checkPopunder(ITestRequestDTO oTest) throws  EHandlerException {

      checkInput(oTest);
      //this first if is due to the deactivation of the pop under in Chrome browser. 
      //It MUST BE deleted when the pop under will be activated also for Chrome.
      if(!(oBrowser instanceof ChromeDriver)){ 
         if (oListelement != null && oListelement.size() >= 2) {

            String sElement = (String) oListelement.get(0);
            String sTypologyTest = (String) oListelement.get(1);

            try {
               ResultContentDTO oResults = this.checkPopunderTitle(sElement);
               if (oResults.getElement() != null && sTypologyTest.equals("positive")) {
                  sMessageResult = "Element " + sElement + " : Shown as Expected";
               } else {
                  if (oResults.getElement() == null && sTypologyTest.equals("negative")) {
                     sMessageResult = "Element " + sElement + " : Not shown as Expected";
                  } else {
                     boResultTest = false;
                     if (sTypologyTest.equals("positive")) {
                        sMessageResult = "Element " + sElement + " : Not shown but Expected";
                     } else {
                        sMessageResult = "Element " + sElement + " : Something is wrong";
                     }
                  }
               }

            } catch (Exception ex) {
               oLogger.error("Someting Wrong in " + this.getClass().getName() + ". During test " + oTest.getMethodName());          
               String sMessage = ex.getMessage();
               oLogger.error(sMessage);
               throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
            }
         }
      }
      ATaskResultDTO oResult = new ATaskResultDTO("CheckTAPopunderTaskResult");
      oResult.setIsCorrect(boResultTest);
      oResult.setMessage(sMessageResult);
  

      return oResult;
   }
   
   
   /**
    * Check if Popunder is open via window title
    * 
    * @param sElement popunder title
    * @return  ResultContentDTO that Hold the element 
    * @throws EHandlerTaskPerforming 
    */
   private ResultContentDTO checkPopunderTitle(String sElement) throws EHandlerException
   {
       ResultContentDTO oResultContentDTO = new ResultContentDTO();
       try {
    	  Set<String> oSetHandles = oBrowser.getWindowHandles();
    	  String sCurrentWindowHandle = oBrowser.getWindowHandle(); 
    	  
    	  for (Iterator<String> oIt = oSetHandles.iterator(); oIt.hasNext();) {
              String sHandles = oIt.next();
              oBrowser.switchTo().window(sHandles);
              if(oBrowser.getTitle().contains(sElement)) {
            	oResultContentDTO .setElement(oBrowser.getTitle());
                break;
              }
           }
    	  
    	   oBrowser.switchTo().window(sCurrentWindowHandle);
          
       } catch (Exception ex) {
          LogFactory.getLog("com.venere.utils.test.bl.CheckPopunderBL").warn("Popunder not shown "+sElement);
          oResultContentDTO.setElement(null);            
       }
       return oResultContentDTO;

   }

}
