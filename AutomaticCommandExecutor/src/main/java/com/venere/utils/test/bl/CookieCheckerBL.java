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
import java.io.BufferedWriter;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;

/**
 *
 * @author quatrini
 */
public class CookieCheckerBL extends AClassTestBL{
    
   String sPathImage;
 
   protected Log  oLogger = LogFactory.getLog(getClass());
  
 
   public CookieCheckerBL(){ super("CookieCheckerBL");}

   public Map<String, Object> getMapResultContainer() 
   {
      return oMapResultContainer;
   }

   public ICommandResult showAllCookies(ITestRequestDTO oTest) throws   EHandlerException{

      oListelement   = oTest.getInputDataList();
      //String sCookieKey   = (String)oListelement.get(0);
      //String sCookieValue = (String)oListelement.get(1);
      String sCookieFileNameAndPath = (String)oListelement.get(0);
      ATaskResultDTO oResult = new ATaskResultDTO("ShowCookiesResult");
      //boolean isCookieValuePresent = false;
      
       try {
           
      File cookieFile=new File(sCookieFileNameAndPath);
      if (!cookieFile.exists()) {
         
              cookieFile.createNewFile();
          
      }
      FileWriter fw = new FileWriter(cookieFile.getAbsoluteFile(), true);
      BufferedWriter bw = new BufferedWriter(fw);
		
      // prendi tutti i Cookie
      for (Iterator it = oBrowser.manage().getCookies().iterator(); it.hasNext();) {
            Cookie oCookie = (Cookie) it.next();
            
            String name = oCookie.getName();
            String value = oCookie.getValue();           
         
            try{
                bw.write("Cookie: "+name+"\tValue: "+value);	
                bw.newLine();
            }
            catch (IOException e){
                oLogger.error(e.getMessage()); 
            }
      }
      
      bw.close();
      } catch (IOException ex) {
              Logger.getLogger(CookieCheckerBL.class.getName()).log(Level.SEVERE, null, ex);
          }
      //scrivili su un txt
            

      oResult.setIsCorrect(true);
      oResult.setMessage("All cookies have been retrieved from browser");
      
      return oResult;
   }
   
   public ICommandResult checkCookiePresent(ITestRequestDTO oTest) throws   EHandlerException{

      oListelement   = oTest.getInputDataList();
      String sCookieKey   = (String)oListelement.get(0);
      String sCookieValue = (String)oListelement.get(1);
      boolean isCookieValuePresent = false;
      
      String  sInitMessage  = "Check "+ oTest.getCheckDescription()+" - ";
      String sMessage = sInitMessage+ "Cookie "+sCookieKey+" not found";
      ATaskResultDTO oResult = new ATaskResultDTO("CookiesCheckerResult");
      String sTypologyTest = "positive";
      try{
         if (oListelement.size() == 3) {
            sTypologyTest = (String) oListelement.get(2);
         }
         

//   JavascriptExecutor js = (JavascriptExecutor) oBrowser; 
//   js.executeScript("window.alert(document.cookie);");
        
         
         for (Iterator it = oBrowser.manage().getCookies().iterator(); it.hasNext();) {
            Cookie oCookie = (Cookie) it.next();
            if(oCookie.getName().equals(sCookieKey)){
                  
                  if(oCookie.getValue().equals(sCookieValue)){
                     isCookieValuePresent=true;
                     sMessage = sInitMessage+ "Cookie "+sCookieKey+" present with the value '"+sCookieValue+"' as expected";
                  }else{
                     sMessage = sInitMessage+ "Cookie "+sCookieKey+" present but with different value. Searched '"+sCookieValue+"' but found '"+oCookie.getValue()+"'";
                  }
                  break;
            }
         }      

         oResult.setIsCorrect((sTypologyTest.equals("positive") ) ? isCookieValuePresent : !isCookieValuePresent );
         oResult.setMessage(sMessage);
         
         if (oResult.isCorrect()) {
            oLogger.info(oResult.getMessage());
         } else {
            oLogger.error(oResult.getMessage());
         }
      
      } catch (Exception ex) {
           
            oLogger.error(sMessage);
            oLogger.error(ex.getMessage());
            ScreenUtil oUtil = new ScreenUtil(oBrowser);
            oUtil.makeAPrintScreen(oTest.getMethodName());
            throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
         }
      
      return oResult;
   }

}