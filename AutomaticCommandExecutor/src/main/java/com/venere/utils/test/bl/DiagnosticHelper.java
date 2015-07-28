/*                   Copyright (c) 2007 Venere Net S.r.l.
 *                             All Rights Reserved
 *
 * This software is the confidential and proprietary information of
 * Venere Net S.r.l. ("Confidential  Information"). You  shall not disclose
 * such  Confidential Information and shall use it only in accordance with
 * the terms  of the license agreement you entered into with Venere Net S.r.l.
 *
 * VENERE NET S.r.l. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR NON-INFRINGEMENT. VENERE NET S.r.l. SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS
 * SOFTWARE OR ITS DERIVATIVES.
 */
package com.venere.utils.test.bl;

import com.venere.ace.dao.WebDriverBrowser;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ICommandRequestDTO;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.IDiagnostic;
import com.venere.utils.dto.DiagnosticDTO;
import com.venere.utils.dto.ResultContentDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author fcastaldi
 */
public class DiagnosticHelper implements IDiagnostic {

   protected Log oLogger = LogFactory.getLog(getClass());
   protected WebDriverBrowser oBrowser;
   private Map<String, String> oDiagnosticCheckMap;
   private ClassPathXmlApplicationContext oClassSpring;

   public DiagnosticHelper() {
      String recoverType = System.getProperty("com.venere.ace.recoverType");
      if("spa".equalsIgnoreCase(recoverType)){
         oClassSpring = new ClassPathXmlApplicationContext(new String[]{"spring/spring-recover-system-SPA.xml"});
      }else{         
         oClassSpring = new ClassPathXmlApplicationContext(new String[]{"spring/spring-recover-system.xml"});
      }
      oDiagnosticCheckMap = (HashMap) oClassSpring.getBean("diagnosysMap");
      
   }

   public void setBrowser(WebDriverBrowser oWebDriver) {
      this.oBrowser = oWebDriver;
   }

   public Map<String, String> getDiagnosticCheckMap() {
      return oDiagnosticCheckMap;
   }

   @Override
   public DiagnosticDTO performDiagnosis(ICommandExecutor oExecutor) throws EHandlerException {


      DiagnosticDTO oDiagnosticDTO = null;
      long lTimeout = oBrowser.getSecondWaitsOnFindElement();
      ICommandRequestDTO oCommandRequest= null;
      String sResult = null;

      if(oExecutor.getRequest() instanceof ICommandRequestDTO){
         oCommandRequest        = (ICommandRequestDTO) oExecutor.getRequest();
         Map oErrorRecognizeMap = (Map) oClassSpring.getBean("errorRecognizedsMap");
         oDiagnosticDTO         = (DiagnosticDTO) oErrorRecognizeMap.get(oCommandRequest.getLocationElement());
      }

   //   if (oDiagnosticDTO == null) {
         try {
            oBrowser.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
            for (Map.Entry<String, String> entry : oDiagnosticCheckMap.entrySet()) {
               String xPath = entry.getValue();
               ResultContentDTO oResults = HelperHtmlBL.getElement(oBrowser.getDriver(), xPath);
               if (oResults.getElement() != null) {
                  try {
                     oDiagnosticDTO = (DiagnosticDTO) oClassSpring.getBean(entry.getKey());
                     break;
                  } catch (Exception ex) {
                     oLogger.warn("Can't create DTO after Diagnosys recognition "+ex.getMessage());
                  }
               }
            }
            oBrowser.getDriver().manage().timeouts().implicitlyWait(lTimeout, TimeUnit.MILLISECONDS);

         } catch (EHandlerException ex) { 
            oBrowser.getDriver().manage().timeouts().implicitlyWait(lTimeout, TimeUnit.MILLISECONDS);
            oLogger.warn("Someting Wrong in " + this.getClass().getName() + ".");
            String sMessage = ex.getMessage();
            oLogger.warn(sMessage);            
            oDiagnosticDTO = null;
         }
     // }

      return oDiagnosticDTO;
   }
}
