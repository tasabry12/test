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
import com.venere.utils.dto.PageDTO;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 *
 * @author quatrini
 */
public class CheckPriceBL extends AClassTestBL{
   
  
   
   private String sPriceSliceSRP         = "//div[@class='hotelstriprice']/div[@class='hotelprice']/span";
   private String sPriceHDP              = "//span[@class='availroom_price_tot_num']";
   private String sPriceSliceDiscountSRP = "//div[@class='hotelstriprice']/div[@class='pricediscount']/span";
   protected Log  oLogger                = LogFactory.getLog(getClass());;
  
   private HtmlCleaner oHcleaner;
   
   public CheckPriceBL(){
      super("CheckPriceBL");
      oHcleaner = new HtmlCleaner();
   }

   public Map<String, Object> getMapResultContainer() 
   {
      return oMapResultContainer;
   }

   
   public ICommandResult checkPrice(ITestRequestDTO oTest) throws     EHandlerException{
      
     
      String sActualPrice   = "ACTUAL";
      String sExpectedPrice = "EXCEPTED";
      List oListelement = oTest.getInputDataList();
      if(oListelement.size() == 2){
         try{
            sActualPrice   = getStringFromSRP ((String)oListelement.get(0),sPriceSliceSRP);
            sExpectedPrice = getStringFromHDP ((String)oListelement.get(1),sPriceHDP);

         }catch(Exception ex){
            String sMessage = ex.getMessage();
            oLogger.error(sMessage);
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
         }
      }else{
      }
      
      ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
      oResult.setIsCorrect(sActualPrice.equals(sExpectedPrice));
      oResult.setMessage("PriceCheck Done");

      return oResult;
   }
   
   public ICommandResult checkLand(ITestRequestDTO oTest) throws  EHandlerException{
      
      ATaskResultDTO oResult = new ATaskResultDTO("CheckTaskResult");
      oResult.setIsCorrect(true);
      oResult.setMessage("PriceCheck Done");

      return oResult;
      
   }
   
   private String getStringFromHDP(String sElementPage,String sXpathElementtoGet) throws  EHandlerException{
      
      String sResult = "";
      Object[] oResults = getContent(sElementPage, sXpathElementtoGet);
      try{
         List     oM       = ((TagNode) oResults[0]).getChildren();
         sResult           = ((ContentNode) oM.get(0)).getContent().toString();
      }catch(Exception ex){
         String sMessage = ex.getMessage();
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }
      
      String[] sResultSet =  sResult.split(" ");
      return sResultSet[sResultSet.length-1];
      
   }
   
   private String getStringFromSRP(String sElementage,String sXpathElementtoGet) throws  EHandlerException {
      
      String sResult = "";
      String sXpathElementtoGetStrip ="//div[@class='hotelstrip']";
      
      Object[] oStrips = getContent(sElementage, sXpathElementtoGetStrip);
      Object[] oElements =null;
      try{
         TagNode  oStrip   = (TagNode) oStrips[0];
         oElements = oStrip.evaluateXPath(sXpathElementtoGet);
         if(oElements.length == 0){
            oElements          = oStrip.evaluateXPath(sPriceSliceDiscountSRP);
         }
         List     oM       = ((TagNode) oElements[0]).getChildren();
         sResult           = ((ContentNode) oM.get(0)).getContent().toString();
      }catch(Exception ex){
         String sMessage = ex.getMessage();
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }
      
      String[] sResultSet =  sResult.split(" ");
      return sResultSet[sResultSet.length-1];
   }
   
   private Object[] getContent(String sElementage,String sXpathElementtoGet) throws  EHandlerException {
      
      Object[] oResults = null;
      try{
         PageDTO oP  = (PageDTO) oMapResultContainer.get(sElementage);
//       CODE TO SAVE PAGE HTML     //
//       FileWriter out = new FileWriter ("/home.local/quatrini/"+sElementPage+System.currentTimeMillis()+".txt");
//       out.write(oP.getPageHtml());
//       out.close();
         TagNode  oT       = oHcleaner.clean(oP.getPageHtml());
         oResults = oT.evaluateXPath(sXpathElementtoGet);
      }catch(Exception ex){
         String sMessage = ex.getMessage();
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }
      return oResults;
      
   }
}