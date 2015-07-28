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
import com.venere.ace.utility.ScreenUtil;
import com.venere.utils.dto.PageDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 *
 * @author quatrini
 */
public class CheckContentBL extends AClassTestBL {

    String sPathImage;
    private String sOkMessageBooking = "//div[@class='titlesectionOK']";
  
   private HtmlCleaner oHcleaner;

    public CheckContentBL() {
       super("CheckContentBL");
        oHcleaner = new HtmlCleaner();
    }

    public ICommandResult checkExistBookingConfirm(ITestRequestDTO oTest) throws   EHandlerException {
        oTest.getInputDataList().add(sOkMessageBooking);
        return checkExistElement(oTest);
    }
  
    public ICommandResult checkExistElement(ITestRequestDTO oTest) throws   EHandlerException {

        boResultTest = true;
        oListelement = oTest.getInputDataList();
        sMessageResult = "Element " + (String) oListelement.get(1) + " : Something is wrong";
        if (oListelement.size() >= 2) {
            try {
                String sTypologyTest = "positive";
                if (oListelement.size() == 3) {
                    sTypologyTest = (String) oListelement.get(2);
                }
                Object[] oResults = getContent((String) oListelement.get(0), (String) oListelement.get(1));
                try {
                    if (oResults.length > 0 && sTypologyTest.equals("positive")) {
                        List oM = ((TagNode) oResults[0]).getChildren();
                        String sResult = ((ContentNode) oM.get(0)).getContent().toString();
                        sMessageResult = "Element " + (String) oListelement.get(1) + " : Present as Expected";
                        if (sResult == null || sResult.isEmpty()) {
                            boResultTest = false;
                            sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing but Expected";
                        }
                    } else {
                        if (oResults.length == 0 && sTypologyTest.equals("negative")) {
                            sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing as Expected";
                        } else {
                            boResultTest = false;
                            if (sTypologyTest.equals("positive")) {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing but Expected";
                            } else {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Something is wrong";
                            }
                        }
                    }
                } catch (Exception ex) {
                    String sMessage = ex.getMessage();
                    throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
                }
            } catch (Exception ex) {
                String sMessage = ex.getMessage();
                oLogger.error(sMessage);
                throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
            }
        }
        ATaskResultDTO oResult = populateResult(oTest);

        return oResult;
        
    }
    
    public ICommandResult checkExistTag(ITestRequestDTO oTest) throws   EHandlerException {

        boResultTest = true;
        oListelement = oTest.getInputDataList();
        sMessageResult = "Element " + (String) oListelement.get(1) + " : Something is wrong";
        if (oListelement.size() >= 2) {
            try {
                String sTypologyTest = "positive";
                if (oListelement.size() == 3) {
                    sTypologyTest = (String) oListelement.get(2);
                }
                Object[] oResults = getContent((String) oListelement.get(0), (String) oListelement.get(1));
                try {
                    if (oResults.length > 0 && sTypologyTest.equals("positive")) {
                        sMessageResult = "Element " + (String) oListelement.get(1) + " : Present as Expected";
                    } else {
                        if (oResults.length == 0 && sTypologyTest.equals("negative")) {
                            sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing as Expected";
                        } else {
                            boResultTest = false;
                            if (sTypologyTest.equals("positive")) {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing but Expected";
                            } else {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Not Expected but Present";
                            }
                        }
                    }
                } catch (Exception ex) {
                    String sMessage = ex.getMessage();
                    throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
                }
            } catch (Exception ex) {
                String sMessage = ex.getMessage();
                oLogger.error(sMessage);
                throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
            }
        }
        ATaskResultDTO oResult = populateResult(oTest);

        return oResult;
    }

    public ATaskResultDTO checkElementStyle(ITestRequestDTO oTest) throws   EHandlerException {
        //lista : [0] nome della pagina
        //lista : [1] xpath es. //div[@id='payment_method']
        //lista : [2] style property da confrontare
        //lista : [3] positive| negative optional

        List oListElement = oTest.getInputDataList();
        String sAttributeName = "style";

        sMessageResult = "Attribute " + sAttributeName;
        boolean isCorrect = false;
        String sTypologyTest = "positive";

        try {

            if (oListElement.size() == 4) {
                sTypologyTest = (String) oListElement.get(3);
            }

            Object[] oResults = getContent((String) oListElement.get(0), (String) oListElement.get(1));
            if (oResults != null && oResults.length > 0) {
                TagNode oTag = (TagNode) oResults[0];
                String sStyleValue = oTag.getAttributeByName(sAttributeName);

                if (sStyleValue == null) {
                    sMessageResult = sMessageResult + " not found";
                    isCorrect = false;
                } else {
                    String sTrimmedStyle = sStyleValue.replace(" ", "").toLowerCase();
                    String sTrimmedInputStyle = ((String) oListElement.get(2)).replace(" ", "").toLowerCase();

                    isCorrect = (sTrimmedStyle.contains(sTrimmedInputStyle) && "positive".equals(sTypologyTest))
                            || (!sTrimmedStyle.contains(sTrimmedInputStyle) && "negative".equals(sTypologyTest)) ? true : false;

                    if ("positive".equals(sTypologyTest)) {
                        sMessageResult = sMessageResult + " " + oListElement.get(2) + (isCorrect ? " : Present as Expected" : " : Missing but Expected");
                    } else {
                        sMessageResult = sMessageResult + " " + oListElement.get(2) + (isCorrect ? " : Missing as Expected" : " : Something is wrong");
                    }
                }
            } else {
                sMessageResult = "Tag node for element " + oListElement.get(1) + " not found";

            }

        } catch (Exception ex) {
            sMessageResult = sMessageResult + ": Exception occurred";
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
        }

        ATaskResultDTO oResult = populateResult(oTest);

        return oResult;
    }

    public ICommandResult checkExistText(ITestRequestDTO oTest) throws   EHandlerException {

        boResultTest = true;
        oListelement = oTest.getInputDataList();
        sMessageResult = "Element " + (String) oListelement.get(1) + " : Something is wrong";
        if (oListelement.size() >= 2) {
            try {
                String sTypologyTest = "positive";
                if (oListelement.size() == 4) {
                    sTypologyTest = (String) oListelement.get(3);
                }
                Object[] oResults = getContent((String) oListelement.get(0), (String) oListelement.get(1));
                StringBuffer sStringToCheck = (StringBuffer) oResults[0];
                String s2 = sStringToCheck.toString();
                boolean boResultExistText = s2.equals((String) oListelement.get(2));
                try {
                    if (boResultExistText && sTypologyTest.equals("positive")) {
                        sMessageResult = "Element " + (String) oListelement.get(1) + " : Present as Expected";
                    } else {
                        if (!boResultExistText && sTypologyTest.equals("negative")) {
                            sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing as Expected";
                        } else {
                            boResultTest = false;
                            if (sTypologyTest.equals("positive")) {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Missing but Expected";
                            } else {
                                sMessageResult = "Element " + (String) oListelement.get(1) + " : Something is wrong";
                            }
                        }
                    }
                } catch (Exception ex) {
                    String sMessage = ex.getMessage();
                    throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
                }
            } catch (Exception ex) {
                String sMessage = ex.getMessage();
                oLogger.error(sMessage);
                throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
            }
        }
        ATaskResultDTO oResult = populateResult(oTest);
        return oResult;
    }

    private Object[] getContent(String sElementage, String sXpathElementtoGet) throws   EHandlerException {

        Object[] oResults = null;
        try {
            PageDTO oP = (PageDTO) oMapResultContainer.get(sElementage);
            sPathImage = oP.getPrintScreenPath();
            TagNode oT = oHcleaner.clean(oP.getPageHtml());
            oResults = oT.evaluateXPath(sXpathElementtoGet);
        } catch (Exception ex) {
            String sMessage = ex.getMessage();
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
        }
        return oResults;

    }
    
    
}