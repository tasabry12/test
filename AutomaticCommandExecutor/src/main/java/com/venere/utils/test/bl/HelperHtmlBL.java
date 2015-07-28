/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.utils.test.bl;

import com.venere.ace.exception.EHandlerException;
import com.venere.utils.dto.ResultContentDTO;
import java.lang.reflect.Method;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author cquatrini
 */
public class HelperHtmlBL {

    private static String sLocatorType = "xpath";

    public static void setLocatorType(String locatorType) {
        sLocatorType = locatorType;
    }

    /**
     * Get an Element from DOM
     *
     * @param oBrowser , hold the browser where do check
     * @param sLocation where the element is located
     * @return ResultContentDTO that Hold the element
     * @throws EHandlerTaskPerforming
     */
    public static ResultContentDTO getElement(WebDriver oBrowser, String sLocation) throws EHandlerException {
        ResultContentDTO oResultContentDTO = new ResultContentDTO();
        try {
            oResultContentDTO.setElement(oBrowser.findElement(getByObject(sLocation)));
        } catch (Exception ex) {

            oResultContentDTO.setElement(null);
        }
        return oResultContentDTO;

    }

    /**
     * Get more element from DOM
     *
     * @param oBrowser , hold the browser where do check
     * @param sLocation where the element is located
     * @return ResultContentDTO that Hold the element
     *
     * @throws EHandlerTaskPerforming
     */
    public static ResultContentDTO getElements(WebDriver oBrowser, String sLocation) throws EHandlerException {
        ResultContentDTO oResultContentDTO = new ResultContentDTO();
        try {
            oResultContentDTO.setElements(oBrowser.findElements(getByObject(sLocation)).toArray());
        } catch (Exception ex) {
            //LogFactory.getLog("com.venere.utils.test.bl.HelperHtmlBL").warn("Element not Found in Location "+sLocation+" "+ex.getMessage());
            oResultContentDTO.setElements(null);
        }
        return oResultContentDTO;

    }

    /**
     * Get text from one element from DOM
     *
     * @param oBrowser , hold the browser where do check
     * @param sLocation where the element is located
     * @return ResultContentDTO that Hold the element
     *
     * @throws EHandlerTaskPerforming
     */
    public static ResultContentDTO getTextElement(WebDriver oBrowser, String sLocation) throws EHandlerException {
        boolean boIsException = false;
        ResultContentDTO oResultContentDTO = new ResultContentDTO();
        try {
            String sResultText = (String) ((JavascriptExecutor) oBrowser).executeScript("return arguments[0].textContent", oBrowser.findElement(getByObject(sLocation)));
            oResultContentDTO.setText(sResultText.trim());
        } catch (Exception ex) {
            boIsException = true;
            oResultContentDTO.setText(null);
        }
        // Firefox do not handle Javascript as Chrome, alternative method follow
        if (boIsException) {
            try {
                oResultContentDTO.setText(oBrowser.findElement(getByObject(sLocation)).getText().trim());
            } catch (Exception ex) {
                oResultContentDTO.setText(null);
            }
        }

        return oResultContentDTO;

    }

    /**
     * Get value of selected element from select into DOM
     *
     * @param oBrowser , hold the browser where do check
     * @param sLocation where the element is located
     * @return ResultContentDTO that Hold the element
     *
     * @throws EHandlerTaskPerforming
     */
    public static ResultContentDTO getTextSelectedElement(WebDriver oBrowser, String sLocation) throws EHandlerException {
        ResultContentDTO oResultContentDTO = new ResultContentDTO();
        try {
            Select oSelect = new Select(oBrowser.findElement(getByObject(sLocation)));
            oResultContentDTO.setText(oSelect.getFirstSelectedOption().getText());
        } catch (Exception ex) {
            LogFactory.getLog("com.venere.utils.test.bl.HelperHtmlBL").warn("Element not Found in Location " + sLocation + " " + ex.getMessage());
            oResultContentDTO.setText(null);
        }
        return oResultContentDTO;

    }

    /**
     * Check if an element has contained in URL
     *
     * @param oBrowser , hold the browser where do check
     * @param sElement element to check
     * @return ResultContentDTO that Hold the element
     * @throws EHandlerTaskPerforming
     */
    public static ResultContentDTO isElementInURL(WebDriver oBrowser, String sElement) throws EHandlerException {
        ResultContentDTO oResultContentDTO = new ResultContentDTO();
        try {
            oResultContentDTO.setElement(oBrowser.getCurrentUrl().contains(sElement.trim()));
        } catch (Exception ex) {
            LogFactory.getLog("com.venere.utils.test.bl.HelperHtmlBL").warn("Element: " + sElement + " not contained in URL");
            oResultContentDTO.setElement(false);
        }
        return oResultContentDTO;

    }

    private static By getByObject(String sLocation) throws EHandlerException {
        By oByElement = null;

        Method oInvokingMethod = null;

        try {
            oInvokingMethod = By.class.getMethod(sLocatorType, new Class[]{String.class});
        } catch (NoSuchMethodException ex) {
            String sMessage = "Failure accessing element by locator " + sLocatorType;
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        } catch (SecurityException ex) {
            String sMessage = "Failure accessing element by locator " + sLocatorType;
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }

        try {
            oByElement = (By) oInvokingMethod.invoke(By.class, sLocation);
        } catch (Throwable ex) {
            String sMessage = "Failure during get Web Element. Location Element " + sLocation + ". Locator Type " + sLocatorType;
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
        }
        return oByElement;

    }
}
