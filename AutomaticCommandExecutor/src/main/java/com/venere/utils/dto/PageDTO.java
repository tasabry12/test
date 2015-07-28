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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.utils.dto;

import com.venere.ace.dtos.CheckConditionDTO;
import com.venere.ace.interfaces.ICommandResult;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.Cookie;

/**
 *
 * @author quatrini
 */
public class PageDTO implements ICommandResult{
   
   private String      sPageHtml;
   private URL         oOriginUrl;
   private Set<Cookie> oCookies;
   private String      sFilePrintScreen;
   private String      sText;
   private boolean     boIsCorrect;
   
   public void setFilePrintScreen(String sFilePrintScreen) {
        this.sFilePrintScreen = sFilePrintScreen;
   }

   public void setText(String sText) {
      this.sText = sText;
   }
   

   public Set<Cookie> getCookies() {
      return oCookies;
   }

   public void setCookies(Set<Cookie> sCookies) {
      this.oCookies = sCookies;
   }

   public URL getOriginUrl() {
      return oOriginUrl;
   }

   public void setOriginUrl(URL sOriginUrl) {
      this.oOriginUrl = sOriginUrl;
   }

   public String getPageHtml() {
      return sPageHtml;
   }

   public void setPageHtml(String sPageHtml) {
      this.sPageHtml = sPageHtml;
   }
   
  
   @Override
   public Object getResultTask() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public boolean isCorrect() {
      return true;
   }

   @Override
   public Map<String, Object> getResultContent() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String getMessage() {
      return oOriginUrl.toString();
   }

    @Override
    public String getPrintScreenPath() {
        return sFilePrintScreen;
    }

    @Override
    public boolean isStopped() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIsStopped(boolean isStopped) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIsCorrect(boolean boIsCorrect) {
        this.boIsCorrect = boIsCorrect;
    }

   @Override
   public void setMessage(String sMessage) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

    @Override
    public void setDiagnostic(DiagnosticDTO oDiagnosticDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DiagnosticDTO getDiagnostic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   @Override
   public String getClassName() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setClassName(String sClassName) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public CheckConditionDTO getCondition() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setCondition(CheckConditionDTO oCondition) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
      
   
}
