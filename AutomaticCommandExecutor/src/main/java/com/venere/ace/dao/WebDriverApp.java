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
package com.venere.ace.dao;

import com.venere.ace.abstracts.AException;
import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ICommandRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.ITaskHandler;
import com.venere.ace.utility.ScreenUtil;
import org.openqa.selenium.*;

/**
 * 
 * 
 * @author fcastaldi
 */
public class WebDriverApp
        extends WebDriverMobile
        implements ITaskHandler {

   public WebDriverApp() {
      super();
   }


   @Override
   public ICommandResult open(ICommandRequestDTO obj) throws EHandlerException {
      oBrowser.get(obj.getValue());
      waitFor(obj.getDelayMillisecond());
      printRequest(obj);
      return setResult(true, false, "Open Action Executed");
   }

   @Override
   public ICommandResult close(ICommandRequestDTO obj) throws EHandlerException {
      oBrowser.close();
      printRequest(obj);
      return setResult(true, false, "Close Operation Executed");
   }

   @Override
   public ICommandResult click(ICommandRequestDTO obj) throws EHandlerException {

      ATaskResultDTO oResult = null;
      By oByElement = getWebElement(obj);
      try {

         oBrowser.findElement(oByElement).click();

         oResult = setResult(true, false, "Click Operation Executed");
         printRequest(obj);
//         try {
//            Thread.sleep(lDelayonChangeClick);
//         } catch (InterruptedException ex) { //TO ANALIZE
//         }

         waitFor(obj.getDelayMillisecond());

      } catch (WebDriverException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Click'. Request " + obj.toString();
         oLogger.error(sMessage);
         oResult = setResult(false, false, sMessage);
      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }

      return oResult;
   }

   @Override
   public ICommandResult type(ICommandRequestDTO obj) throws AException {
      By oByElement = getWebElement(obj);
      ATaskResultDTO oResult;

      try {
         oBrowser.findElement(oByElement).clear();
         oBrowser.findElement(oByElement).click();
         oBrowser.findElement(oByElement).sendKeys(obj.getValue());

         oResult = setResult(true, false, "Action Typing Executed");
         waitFor(obj.getDelayMillisecond());

      } catch (WebDriverException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Type'. Request " + obj.toString();
         oLogger.error(sMessage);
         oResult = setResult(false, false, sMessage);
      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }

      printRequest(obj);
      return oResult;
   }
}
