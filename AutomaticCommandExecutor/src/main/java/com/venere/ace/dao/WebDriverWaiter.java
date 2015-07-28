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

import com.venere.ace.abstracts.ABrowser;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ICommandRequestDTO;
import com.venere.ace.interfaces.IWaiterTaskHandler;
import com.venere.ace.utility.ScreenUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

/**
 *
 * @author quatrini
 */
public class WebDriverWaiter
        extends ABrowser
        implements IWaiterTaskHandler {

   protected long iMaxDelay;
   private String sUserEssorBox = "usermessage";
   private String sUserErrorXpath = "//div[@id='usermessage']/img";
   private int iRetries = 3;

   public WebDriverWaiter() {
      super("WebDriverWaiter");
   }

   public void waitBookingBar(ICommandRequestDTO obj)
           throws EHandlerException {
      try {
         By oElement = getWebElement(obj);
         boolean boTryAgain = true;
         long lStartingTime = System.currentTimeMillis();
         do {
            try {
               oBrowser.findElement(oElement);
               boTryAgain = true;

            } catch (NoSuchElementException | ElementNotVisibleException ex) {
               boTryAgain = false;
            } catch (StaleElementReferenceException e) {
               oLogger.warn(obj.getTaskName() + " - Managed Exception: " + e.getClass());
            }

         } while (boTryAgain && (System.currentTimeMillis() - lStartingTime < iMaxDelay));
         while (checkRetry()) {
            try {               
               Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
         }
      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
      iRetries = 3;
   }

   @Override
   public void wait4SearchAvailability(ICommandRequestDTO obj)
           throws EHandlerException {
      try {
         By oElement = getWebElement(obj);
         boolean boTryAgain = true;
         long lStartingTime = System.currentTimeMillis();
         do {
            try {
               if (!oBrowser.findElement(oElement).isDisplayed()) {
                  boTryAgain = false;
               }
            } catch (NoSuchElementException | ElementNotVisibleException ex) {
               boTryAgain = false;
            } catch (StaleElementReferenceException e) {
               oLogger.warn(obj.getTaskName() + " - Managed Exception: " + e.getClass());
            }

         } while (boTryAgain && (System.currentTimeMillis() - lStartingTime < iMaxDelay));
         while (checkRetry()) {
            try {
               // Wait Page Reload
               Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
         }
      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
      iRetries = 3;
   }

   @Override
   public void wait4SearchHotelPage(ICommandRequestDTO obj)
           throws EHandlerException {
      By oElement = getWebElement(obj);
      try {
         wait4Element(obj);
         long lStartingTime = System.currentTimeMillis();
         boolean boElementDisappear = false;
         do {
            try {
               oBrowser.findElement(oElement).click();
            } catch (ElementNotVisibleException ex) {
               boElementDisappear = true;
            }

         } while (!boElementDisappear && (System.currentTimeMillis() - lStartingTime < iMaxDelay));
      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
   }

   @Override
   public void wait4Element(ICommandRequestDTO obj)
           throws EHandlerException {
      try {
         By oElement = getWebElement(obj);
         long lStartingTime = System.currentTimeMillis();
         boolean boTryAgain = false;
         do {
            try {
               boTryAgain = false;
               oBrowser.findElement(oElement);
            } catch (NoSuchElementException | ElementNotVisibleException ex) {
               boTryAgain = true;
            }
         } while (boTryAgain && (System.currentTimeMillis() - lStartingTime < iMaxDelay));

      } catch (RuntimeException ex) {
         ScreenUtil oUtil = new ScreenUtil(oBrowser);
         oUtil.makeAPrintScreen(obj.getTaskName());
         String sMessage = ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
   }

   protected By getWebElement(ICommandRequestDTO obj) throws EHandlerException {
      By oByElement = null;

      Method oInvokingMethod = null;

      try {
         oInvokingMethod = By.class.getMethod(obj.getLocatorType(), new Class[]{String.class});
      } catch (NoSuchMethodException | SecurityException ex) {
         String sMessage = "Failure accessing element by locator " + obj.getLocatorType();
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
      try {
         oByElement = (By) oInvokingMethod.invoke(By.class, obj.getLocationElement());
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
         String sMessage = "Failure during get Web Element. Location Element " + obj.getLocationElement() + ". Locator Type " + obj.getLocatorType();
         oLogger.error(sMessage + "    " + ex);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
      return oByElement;

   }

   protected boolean checkRetry() {
      boolean boRetries = true;
      try {
         By oByElementRetry = By.id(sUserEssorBox);
         if (oBrowser.findElement(oByElementRetry).isDisplayed() && iRetries > 0) {
            iRetries--;
            oByElementRetry = By.xpath(sUserErrorXpath);
            oBrowser.findElement(oByElementRetry).click();
            try {
               Thread.sleep(1000);
            } catch (InterruptedException | StaleElementReferenceException ex) {
            }

         }
      } catch (NoSuchElementException | ElementNotVisibleException ex) {
         boRetries = false;
      }
      return boRetries;
   }

   public void setMaxDelay(String sMaxDelay) {

      if (sMaxDelay == null || sMaxDelay.equals("")) {
         sMaxDelay = "0";
      }
      this.iMaxDelay = (new Long(sMaxDelay)).longValue();
   }
}
