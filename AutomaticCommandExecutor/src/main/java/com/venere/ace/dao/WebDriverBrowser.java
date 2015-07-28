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
import com.venere.ace.abstracts.AException;
import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ICommandRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.ITaskHandler;
import com.venere.ace.utility.DateUtil;
import com.venere.ace.utility.FolderManager;
import com.venere.ace.utility.ScreenUtil;
import com.venere.utils.dto.PageDTO;
import com.venere.utils.dto.RetrieveDataDTO;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author quatrini
 */
public class WebDriverBrowser
        extends ABrowser
        implements ITaskHandler {

    private String sExpectedURL;

    public WebDriverBrowser() {
        super("WebDriver");
    }

    @Override
    public void startBrowser() throws EHandlerException {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public ICommandResult closeAll(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult = setResult(true, true, "Action Stopping executed");

        if (oBrowser != null) {
            oBrowser.manage().deleteAllCookies();
            oLogger.debug("Close all page handles ");
            try {
                oBrowser.quit();
                oBrowser = null;
            } catch (RuntimeException ex) {
                ScreenUtil oUtil = new ScreenUtil(oBrowser);
                oUtil.makeAPrintScreen(obj.getTaskName());
                String sMessage = ex.getMessage();
                oLogger.error(sMessage);
                throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
            }
            //printRequest(obj);
        }
        return oResult;
    }

    @Override
    public ICommandResult click(ICommandRequestDTO obj) throws EHandlerException {

        ATaskResultDTO oResult = null;
        By oByElement = getWebElement(obj);
        Set oSetInitialHandles = oBrowser.getWindowHandles();
        String sCurrentWindowHandle = oBrowser.getWindowHandle();

        try {
            sExpectedURL = "";
            try {
                sExpectedURL = oBrowser.findElement(oByElement).getAttribute("href");
            } catch (RuntimeException ex2) {
                oLogger.debug("No href element for clickable element Element: " + obj.toString());
            }

            oBrowser.findElement(oByElement).click();

            oResult = setResult(true, false, "Click Operation Executed");
            printRequest(obj);
            try {
                Thread.sleep(lDelayonChangeClick);
            } catch (InterruptedException ex) { //TO ANALIZE
            }
            if (!isAlertPresent()) {
                Set oSetAfterCommandHandles = oBrowser.getWindowHandles();
                if (oSetInitialHandles.size() != oSetAfterCommandHandles.size()) {
                    for (Iterator oIt = oSetAfterCommandHandles.iterator(); oIt.hasNext();) {
                        String sHandles = (String) oIt.next();
                        if (!oSetInitialHandles.contains(sHandles)) {
                            oBrowser.switchTo().window(sHandles);
                            if (isOpenedWindowBlacklisted()) {
                                oBrowser.switchTo().window(sCurrentWindowHandle);
                                oBrowser.findElement(new By.ByTagName("body")).sendKeys(Keys.TAB);
                            }
                        }
                    }
                }
                if (sExpectedURL != null && !sExpectedURL.equals("") && !sExpectedURL.contains("javascript")) {
                    if (!checkLandingURL(sExpectedURL)) {
                        oLogger.warn("Url Mismatch  - Expected : " + sExpectedURL + "  Actual : " + oBrowser.getCurrentUrl());
                    }
                }
            }
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

    public ICommandResult scrollToElem(ICommandRequestDTO obj) throws AException {

        By oByElement = getWebElement(obj);
        ATaskResultDTO oResult = null;;

        try {
            try {
                RemoteWebElement elem = (RemoteWebElement) oBrowser.findElement(oByElement);
                Point p = elem.getLocation();
                JavascriptExecutor js = (JavascriptExecutor) oBrowser;
                js.executeScript("window.scrollTo(" + p.getX() + "," + (p.getY()) + ");");
                waitFor(obj.getDelayMillisecond());
            } catch (UnreachableBrowserException e) {
                oLogger.warn(e.getMessage());
                waitFor(3000);                
            }
            oResult = setResult(true, false, "Action Scrolling Executed");

        } catch (WebDriverException ex) {
            ScreenUtil oScreenUtil = new ScreenUtil(oBrowser);
            oScreenUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "WebDriver Exception in page: " + oBrowser.getCurrentUrl() + " for 'Type'. Request " + obj.toString();
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        } catch (RuntimeException ex) {
            ScreenUtil oScreenUtil = new ScreenUtil(oBrowser);
            oScreenUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        printRequest(obj);
        return oResult;
    }

    public boolean isAlertPresent() {
        try {
            oBrowser.switchTo().alert();
            oLogger.info("Alert Present");
            return true;
        } catch (NoAlertPresentException Ex) {

            return false;
        }
    }

    protected boolean isOpenedWindowBlacklisted() {
        for (String sTitle : oFollowWindowBlacklist) {
            if (oBrowser.getTitle().contains(sTitle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Switches to a frame using its WebElement identified by name or id
     *
     * @param obj
     * @return
     * @throws EHandlerException
     */
    public ICommandResult switchToFrame(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult;
        try {
            By oByElement = getWebElement(obj);
            WebElement oFrame = oBrowser.findElement(oByElement);
            oBrowser.switchTo().frame(oFrame);
            oResult = setResult(true, false, "Switch to Frame Operation Executed");
            printRequest(obj);
        } catch (Exception e) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "Switch To Frame " + obj.getLocationElement() + " Failed";
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        }
        return oResult;
    }

    public ICommandResult confirmAlert(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult;
        try {
            oBrowser.switchTo().alert().accept();
            oResult = setResult(true, false, "Confirmation on Alert Executed");
            oLogger.info("Confirmation on Alert Executed");
        } catch (Exception e) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "Confirmation on Alert Failed";
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        }
        return oResult;
    }

    /**
     *
     * @param obj
     * @return
     * @throws EHandlerException
     */
    public ICommandResult switchToDefaultContent(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult;
        try {
            oBrowser.switchTo().defaultContent();
            oResult = setResult(true, false, "Switch to Default Content Operation Executed");
            printRequest(obj);
        } catch (Exception e) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "Switch To Frame Default Content Failed";
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        }
        return oResult;
    }

    @Override
    public ICommandResult hover(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult;
        By oByElement = getWebElement(obj);

        //get the element that shows menu with the mouseOver event
        //build and perform the mouseOver with Advanced User Interactions API
        try {
            WebElement oElementHover = oBrowser.findElement(oByElement);
            Actions builder = new Actions(oBrowser);
            builder.moveToElement(oElementHover).build().perform();

            printRequest(obj);
            waitFor(obj.getDelayMillisecond());

            oResult = setResult(true, false, "Hover Action Executed");

        } catch (WebDriverException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Submit'. Request " + oByElement.toString();
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
    public ICommandResult submit(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult;
        By oByElement = getWebElement(obj);
        Set oSetInitialHandles = oBrowser.getWindowHandles();
        try {
            oBrowser.findElement(oByElement).submit();

            printRequest(obj);

            Set oSetAfterCommandHandles = oBrowser.getWindowHandles();
            if (oSetInitialHandles.size() != oSetAfterCommandHandles.size()) {
                for (Iterator oIt = oSetAfterCommandHandles.iterator(); oIt.hasNext();) {
                    String sHandles = (String) oIt.next();
                    if (!oSetInitialHandles.contains(sHandles)) {
                        oBrowser.switchTo().window(sHandles);

                        oLogger.debug("Leave curren Handles Page versus new Handles : " + sHandles + " for the page " + oBrowser.getTitle());
                    }
                }
            }
            waitFor(obj.getDelayMillisecond());

            oResult = setResult(true, false, "Submit Action Executed");
        } catch (WebDriverException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Submit'. Request " + obj.toString();
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
    public PageDTO getHtmlPage(ICommandRequestDTO obj) throws EHandlerException {
        PageDTO oDto = new PageDTO();
        oDto.setPageHtml(oBrowser.getPageSource());
        oDto.setCookies(oBrowser.manage().getCookies());
        String sPathString;
        try {
            sPathString = FolderManager.PrintScreenFolder + System.getProperty("com.venere.ace.currentTestCase") + "_" + DateUtil.dateFormatter(new Date()) + ".png";
            makeAPrintScreen(null);
            oDto.setFilePrintScreen(sPathString);
            oDto.setOriginUrl(new URL(oBrowser.getCurrentUrl()));
        } catch (Exception ex) {
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }
        return oDto;
    }

    @Override
    public String getText(ICommandRequestDTO obj) throws EHandlerException {

        String sText = "";
        try {
            By oByElement = getWebElement(obj);
            sText = oBrowser.findElement(oByElement).getText();
        } catch (Exception e) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "Error performing getText for:" + obj.toString();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, e);
        }

        return sText;
    }

    /**
     * This method sets a property with the value of the text retrieved by
     * gettext function. The key of the property is defined by obj.getValue()
     *
     * @param obj
     * @return
     * @throws EHandlerException
     */
    public ICommandResult setPropertyFromText(ICommandRequestDTO obj) throws EHandlerException {

        RetrieveDataDTO oRet = new RetrieveDataDTO();
        String sPropertyname = "";
        String sRetrievedText = "";
        try {
            sPropertyname = (obj.getValue() != null && obj.getValue().contains("com.venere.ace")) ? obj.getValue() : "com.venere.ace" + obj.getValue();
            sRetrievedText = getText(obj);
            if (!sRetrievedText.isEmpty()) {
                oRet = setSettingResult(true, false, "Action Set Property Executed");
                oRet.setPropName(sPropertyname);
                oRet.setPropValue(sRetrievedText);
            } else {
                ScreenUtil oUtil = new ScreenUtil(oBrowser);
                oUtil.makeAPrintScreen(obj.getTaskName());
                closeAll(obj);
                String sMessage = "Error during retrieving the text for property setting. Action Stopping Executed";
                oLogger.error(sMessage);
                oRet = setSettingResult(false, true, sMessage);
            }
        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }


        return oRet;
    }

    /**
     * This method sets a property using the value of a specified attribute. The
     * attribute is defined by xpath and name
     *
     * @param obj
     * @return
     * @throws EHandlerException
     */
    public ICommandResult setPropertyFromAttributeValue(ICommandRequestDTO obj) throws EHandlerException {

        RetrieveDataDTO oRet = new RetrieveDataDTO();
        String[] sObjectValue = obj.getValue().split("\\|");
        String sAttribute = sObjectValue[0];
        String sPropertyname = sObjectValue[1];
        String sAttributeValue = "";
        try {
            sPropertyname = sPropertyname.contains("com.venere.ace") ? sPropertyname : "com.venere.ace" + sPropertyname;
            By oByElement = getWebElement(obj);
            sAttributeValue = oBrowser.findElement(oByElement).getAttribute(sAttribute);
            oRet = setSettingResult(true, false, "Action Set Property Executed");
            oRet.setPropName(sPropertyname);
            oRet.setPropValue(sAttributeValue);
        } catch (WebDriverException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "Error during setting property by attribute " + sAttribute;
            oLogger.error(sMessage);
            oRet = setSettingResult(false, true, sMessage);

        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        return oRet;
    }

    /**
     * This method sets a property with the reservation number retrieved from
     * the text of the confirmation. The key of the property is defined by
     * obj.getValue()
     *
     * @param obj
     * @return
     * @throws EHandlerException
     */
    public ICommandResult setReservationNumberProperty(ICommandRequestDTO obj) throws EHandlerException {

        RetrieveDataDTO oRet = new RetrieveDataDTO();
        String sReservNum = "";
        String sPropertyname = "";
        String sReservMsg;
        try {
            try {
                sReservMsg = getText(obj);
            } catch (Exception e) {
                sReservMsg = null;
            }
            sPropertyname = (obj.getValue() != null && obj.getValue().contains("com.venere.ace")) ? obj.getValue() : "com.venere.ace." + obj.getValue();
            if (sReservMsg != null && !sReservMsg.isEmpty()) {
                String[] oSplittedMsg = sReservMsg.split(" ");
                String sTemp = (oSplittedMsg[oSplittedMsg.length - 1].replace(")", "")).trim();
                sReservNum = sTemp.startsWith("IHR") ? sTemp.replace("IHR", "").trim() : sTemp.trim();
                oRet = setSettingResult(true, false, "Action Set Reservation Number Property Executed");
                oRet.setPropName(sPropertyname);
                oRet.setPropValue(sReservNum);
            } else {
                ScreenUtil oUtil = new ScreenUtil(oBrowser);
                oUtil.makeAPrintScreen(obj.getTaskName());
                closeAll(obj);
                String sMessage = "Error during retrieving the Reservation Number. Action Stopping Executed";
                oLogger.error(sMessage);
                oRet = setSettingResult(false, true, sMessage);
            }

        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }


        return oRet;
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
//            if (!oBrowser.findElement(oByElement).getAttribute("value").equals(obj.getValue())) {
//                closeAll(obj);
//                String sMessage = "Bad Typing Action Task on element " + obj.toString() + ". Action Stopping Executed";
//                ScreenUtil oUtil = new ScreenUtil(oBrowser);
//                oUtil.makeAPrintScreen(obj.getTaskName());
//                oLogger.error(sMessage);
//                oResult = setResult(false, true, sMessage);
//            }

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

    @Override
    public ICommandResult select(ICommandRequestDTO obj) throws AException {
        ATaskResultDTO oResult = null;
        By oByElement = getWebElement(obj);
        try {
            Select select = new Select(oBrowser.findElement(oByElement));
            select.selectByVisibleText(obj.getValue());

            WebElement oElement = select.getFirstSelectedOption();

            if (!oElement.getText().equals(obj.getValue())) {
                ScreenUtil oUtil = new ScreenUtil(oBrowser);
                oUtil.makeAPrintScreen(obj.getTaskName());
                String sMessage = "Select Action Task Failed on element " + oElement.getText();
                oLogger.error(sMessage);
                oResult = setResult(false, false, sMessage);
            }

            printRequest(obj);
            waitFor(obj.getDelayMillisecond());

        } catch (WebDriverException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Select'. Request " + obj.toString();
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        if (oResult == null) {
            oResult = setResult(true, false, "Select Action Executed");
        }

        return oResult;
    }

    @Override
    public ICommandResult selectValue(ICommandRequestDTO obj) throws AException {
        ATaskResultDTO oResult = null;
        By oByElement = getWebElement(obj);
        Select select = null;
        try {
            select = new Select(oBrowser.findElement(oByElement));
            select.selectByValue(obj.getValue());

            WebElement oElement = select.getFirstSelectedOption();

            if (!oElement.getAttribute("value").equals(obj.getValue())) {
                ScreenUtil oUtil = new ScreenUtil(oBrowser);
                oUtil.makeAPrintScreen(obj.getTaskName());
                String sMessage = "Select Action Task Failed on element " + oElement.getText();
                oLogger.error(sMessage);
                oResult = setResult(false, false, sMessage);
            }

            printRequest(obj);
            waitFor(obj.getDelayMillisecond());

        } catch (WebDriverException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "No Element Found in page: " + oBrowser.getCurrentUrl() + " for 'Select Value'. Request " + obj.toString();
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        if (oResult == null) {
            oResult = setResult(true, false, "Select Value Action Executed");
        }

        return oResult;
    }

    @Override
    public void setMapSettings(Map oMapSettings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isTextPresent(ICommandRequestDTO obj) throws EHandlerException {
        System.out.println("Operation : isTextPresent ");
        printRequest(obj);
        return true;
    }

    protected void printRequest(ICommandRequestDTO obj) {
        String sValueString = obj.getValue();

        if (obj.getTaskName().contains("password")) {
            sValueString = "**********";
        }
        oLogger.info("TaskName : " + obj.getTaskName() + "  Location Element :" + obj.getLocationElement() + " Locator Type: " + obj.getLocatorType() + " Value : " + sValueString);

    }

    protected By getWebElement(ICommandRequestDTO obj) throws EHandlerException {
        By oByElement = null;

        Method oInvokingMethod = null;

        try {
            oInvokingMethod = By.class.getMethod(obj.getLocatorType(), new Class[]{String.class});
        } catch (NoSuchMethodException ex) {
            String sMessage = "Failure accessing element by locator " + obj.getLocatorType();
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        } catch (SecurityException ex) {
            String sMessage = "Failure accessing element by locator " + obj.getLocatorType();
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        try {
            oByElement = (By) oInvokingMethod.invoke(By.class, obj.getLocationElement());
        } catch (Throwable ex) {
            String sMessage = "Failure during get Web Element. Location Element " + obj.getLocationElement() + ". Locator Type " + obj.getLocatorType();
            oLogger.error(sMessage + "    " + ex);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }
        return oByElement;

    }

    protected void waitFor(int iMillisecond) {
        if (iMillisecond > 0) {
            oLogger.debug("TimeOut setted to : " + iMillisecond);
            try {
                Thread.sleep(iMillisecond);
            } catch (InterruptedException ex) {
                oLogger.error("TIMEOUT ERROR " + ex);
            }
        }
    }

    @Override
    public ICommandResult checkUrl(Map oValues) throws EHandlerException {
        ATaskResultDTO oResult = setResult(true, false, "Check URL Executed");
        String sExpectedUrl = (String) oValues.get("exceptedUrl");
        if (!oBrowser.getCurrentUrl().equals(sExpectedUrl)) {                     //TO ANALIZE
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sExpectedUrl, null);
        }
        return oResult;
    }

    @Override
    /**
     * This method implements the execution of a print screen in case of the
     * PRINTSCREEN action in metaplan
     */
    public ICommandResult makeAPrintScreen(ICommandRequestDTO obj) throws EHandlerException {
        ScreenUtil oUtil = new ScreenUtil(oBrowser);
        ICommandResult oResult = setResult(false, false, "incorrect value found in task: " + obj.getTaskName() + "allowed values [BOTH,FULL,BROWSER]");
        if (obj.getValue() == null || obj.getValue().equalsIgnoreCase(ScreenUtil.BOTH)) {
            oResult = oUtil.makeFullScreenShot("FS");
            oUtil.makeAPrintScreen(null);
        } else if (obj.getValue().equalsIgnoreCase(ScreenUtil.FULL)) {
            oResult = oUtil.makeFullScreenShot("FS");
        } else if (obj.getValue().equalsIgnoreCase(ScreenUtil.BROWSER)) {
            oResult = oUtil.makeAPrintScreen(null);
        } else {
            oResult = oUtil.makeFullScreenShot("FS");
            oUtil.makeAPrintScreen(null);
        }
        return oResult;
    }

    private boolean checkLandingURL(String sExpectedString) {
        return sExpectedString.equals(oBrowser.getCurrentUrl());
    }

    protected ATaskResultDTO setResult(boolean boIsCorrect, boolean boIsStopped, String sMessage) {
        ATaskResultDTO oResult = new ATaskResultDTO("ActionTaskResult");
        oResult.setIsCorrect(boIsCorrect);
        oResult.setIsStopped(boIsStopped);
        oResult.setMessage(sMessage);

        return oResult;
    }

    private RetrieveDataDTO setSettingResult(boolean boIsCorrect, boolean boIsStopped, String sMessage) {
        RetrieveDataDTO oResult = new RetrieveDataDTO();
        oResult.setIsCorrect(boIsCorrect);
        oResult.setIsStopped(boIsStopped);
        oResult.setMessage(sMessage);

        return oResult;
    }

    @Override
    public ICommandResult setCookie(ICommandRequestDTO obj) throws AException {
        String sName = obj.getLocationElement();
        String sValue = obj.getValue();
        String sParams = obj.getLocatorType();

        String sPath = sDefaultCookiePath;
        String sDomain = sDefaultCookieDomain;
        Date dExpires = null;
        String rMessage = null;
        try {
            StringTokenizer stParams = new StringTokenizer(sParams, ",");
            while (stParams.hasMoreTokens()) {
                String[] asKeyValue = stParams.nextToken().split("=");
                String sKey = asKeyValue[0];
                switch (sKey) {
                    case "path":
                        sPath = asKeyValue[1];
                        break;
                    case "domain":
                        sDomain = asKeyValue[1];
                        break;
                    case "expires":
                        String sExpires = asKeyValue[1];
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.SECOND, Integer.parseInt(sExpires));
                        dExpires = cal.getTime();
                        break;
                }
            }

            if (sName == null || sName.isEmpty()) {
                throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, "When setting a cookie object Location and Value are mandatory");

            } else if (sValue == null || sValue.isEmpty()) {
                rMessage = "Cookie " + sName + " not set";

            } else {
                rMessage = "Cookie " + sName + " successful set";
                Cookie oNewCookie = new Cookie(sName, sValue, sDomain, sPath, dExpires);
                oBrowser.manage().addCookie(oNewCookie);
            }

        } catch (RuntimeException ex) {
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        return setResult(true, false, rMessage);
    }

    public ICommandResult setDatesJs(ICommandRequestDTO obj) throws EHandlerException {
        ATaskResultDTO oResult = null;
        try {

            JavascriptExecutor js = (JavascriptExecutor) oBrowser;
            String dateFormatScript = "var output = $('#" + obj.getLocationElement() + "').datepicker( \"option\", \"dateFormat\" ); return output;";
            String dateFormat = (js.executeScript(dateFormatScript)).toString();
            dateFormat = dateFormat.replace("y", "yy");
            dateFormat = dateFormat.replace("m", "M");

//         SimpleDateFormat df = new SimpleDateFormat(dateFormat);                  
//         Date date = new Date(DateUtil.getDataValue(obj.getValue(), dateFormat));               
//         String formattedDate = df.format(date);

            String formattedDate = DateUtil.getDataValue(obj.getValue(), dateFormat);

            String script = "$('#" + obj.getLocationElement() + "').datepicker(\"setDate\", \"" + formattedDate + "\")";
            js.executeScript(script);
            obj.setValue(formattedDate);
            oResult = setResult(true, false, "Set Dates Operation Executed");
            printRequest(obj);
            waitFor(obj.getDelayMillisecond());



        } catch (WebDriverException ex) {
            ScreenUtil oScreenUtil = new ScreenUtil(oBrowser);
            oScreenUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = "WebDriver Exception in page: " + oBrowser.getCurrentUrl() + " for 'Set Dates'. Request " + obj.toString();
            oLogger.error(sMessage);
            oResult = setResult(false, false, sMessage);
        } catch (RuntimeException ex) {
            ScreenUtil oScreenUtil = new ScreenUtil(oBrowser);
            oScreenUtil.makeAPrintScreen(obj.getTaskName());
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        return oResult;
    }

    @Override
    public void toggle(ICommandRequestDTO obj) throws AException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
