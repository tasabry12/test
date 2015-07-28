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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author cilia
 */
public class CheckFilters extends AClassTestBL {

    protected Log oLogger = LogFactory.getLog(getClass());

    public CheckFilters() {
        super("CheckFilters");
    }

    public Map<String, Object> getMapResultContainer() {
        return oMapResultContainer;
    }

    public ICommandResult checkHotelName(ITestRequestDTO oTest) throws EHandlerException {

        boolean boresult = true;
        List oListElement = oTest.getInputDataList();
        String sXpath =  (String) oListElement.get(0);
        String sWord =  (String) oListElement.get(1);
        String sMessage = "";
        List<WebElement> oListWebElement = oBrowser.findElements(By.xpath(sXpath));
        for (Iterator<WebElement> oIt = oListWebElement.iterator(); oIt.hasNext();) {
            try {
                WebElement oElement = oIt.next();
                String sHotelName = getContent((String) oListElement.get(2), oElement);
                
                sHotelName = sHotelName.toLowerCase();
                sWord = sWord.toLowerCase();
                
                if(!sHotelName.contains(sWord) && !sHotelName.equals("null")){
                    boresult = false;
                    sMessage ="The hotel name don't contains the word searched.";
                    oLogger.error(sMessage);
                    break;
                }
                
            } catch (Exception e) {
                oLogger.error(e);
            }
        }

        ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
        oResult.setIsCorrect(boresult);
        oResult.setMessage("Check done!!");

        return oResult;
    }
    
    
    public ICommandResult checkDeals(ITestRequestDTO oTest) throws EHandlerException {

        boolean boresult = true;
        List oListElement = oTest.getInputDataList();
        String sXpath =  (String) oListElement.get(0);
        String sMessage = "";
        List<WebElement> oListWebElement = oBrowser.findElements(By.xpath(sXpath));
        for (Iterator<WebElement> oIt = oListWebElement.iterator(); oIt.hasNext();) {
            try {
                WebElement oElement = oIt.next();
                String sDealsValue = getContent((String) oListElement.get(1), oElement);

                if(sDealsValue.isEmpty() && !sDealsValue.equals("null")){
                    boresult = false;
                    sMessage ="The Deals there aren't!!";
                    oLogger.error(sMessage);
                    break;
                }
                
            } catch (Exception e) {
                oLogger.error(e);
            }
        }

        ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
        oResult.setIsCorrect(boresult);
        oResult.setMessage("Check done!!");

        return oResult;
    }

    
    public ICommandResult checkStars(ITestRequestDTO oTest) throws EHandlerException {

        boolean boresult = true;
        List oListElement = oTest.getInputDataList();
        String sXpathStarsList =  (String) oListElement.get(0);
        String sXpathStarsFilter =  (String) oListElement.get(1);
        
        WebElement oElementFilter = oBrowser.findElement(By.xpath(sXpathStarsFilter));
        String sNStarsFilter_el = getContent((String) oListElement.get(3), oElementFilter);
        String sNStarsFilter = sNStarsFilter_el.trim();
                
        String sMessage = "";
        List<WebElement> oListWebElement = oBrowser.findElements(By.xpath(sXpathStarsList));
        for (Iterator<WebElement> oIt = oListWebElement.iterator(); oIt.hasNext();) {
            try {
                WebElement oElement = oIt.next();
                
                String sNStarsHotel = getContent((String) oListElement.get(2), oElement);
                
                if(!sNStarsHotel.equals(sNStarsFilter) && !sNStarsFilter.equals("null") && !sNStarsHotel.equals("null")){
                    boresult = false;
                    sMessage ="The Stars aren't equals!!";
                    oLogger.error(sMessage);
                    break;
                }
                
            } catch (Exception e) {
                oLogger.error(e);
            }
        }

        ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
        oResult.setIsCorrect(boresult);
        oResult.setMessage("Check done!!");

        return oResult;
    }
    
    
    public ICommandResult checkCityAreas(ITestRequestDTO oTest) throws EHandlerException {

        boolean boresult = true;
        List oListElement = oTest.getInputDataList();
        String sXpathCityAreasList =  (String) oListElement.get(0);
        
        String sXpathCityAreasFilter =  (String) oListElement.get(1);
        WebElement oElementFilter = oBrowser.findElement(By.xpath(sXpathCityAreasFilter));
        String sNCityAreasFilter = oElementFilter.getText();
        String sNCityAreas = sNCityAreasFilter.trim();
                
        String sMessage = "";
        List<WebElement> oListWebElement = oBrowser.findElements(By.xpath(sXpathCityAreasList));
        for (Iterator<WebElement> oIt = oListWebElement.iterator(); oIt.hasNext();) {
            try {
                WebElement oElement = oIt.next();
                
                String sNCityAreasHotel = getContent((String) oListElement.get(2), oElement);
                String[] sNCityAreasHotel_split = sNCityAreasHotel.split(",");
                String sNCityAreasHotelList = sNCityAreasHotel_split[0].trim();
                
                if(!sNCityAreasHotelList.equals(sNCityAreas) && !sNCityAreas.equals("null") && !sNCityAreasHotelList.equals("null")){
                    boresult = false;
                    sMessage ="The City Areas aren't equals!!";
                    oLogger.error(sMessage);
                    break;
                }
                
            } catch (Exception e) {
                oLogger.error(e);
            }
        }

        ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
        oResult.setIsCorrect(boresult);
        oResult.setMessage("Check done!!");

        return oResult;
    }
    
    
    private String getContent(String sElement, WebElement oWebElement) throws EHandlerException {
        
        String sResult = oWebElement.getAttribute(sElement);
        
        if(sElement.equals("class")){
            sResult = sResult.replace("stars stars-", "");
        }
        
        if(sElement.equals("id")){
            sResult = sResult.replace("star", "");
        }
        
        return sResult;
    }
}