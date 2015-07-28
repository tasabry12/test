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
import java.util.Map;

/**
 *
 * @author quatrini
 */
public class RetrieveDataDTO implements ICommandResult {

   private String sPropName;
   private String sPropValue;
   private String sFilePrintScreen;
   private Map<String, Object> oResultContent;
   private String sClassName;
   private String sMessage;
   boolean boIsCorrect;
   private boolean boIsStopped;
   private DiagnosticDTO oDiagnostic;

   public RetrieveDataDTO() {
      this.sClassName = "RetrieveDataDTO";
   }

   public String getPropName() {
      return sPropName;
   }

   public void setPropName(String sPropName) {
      this.sPropName = sPropName;
   }

   public String getPropValue() {
      return sPropValue;
   }

   public void setPropValue(String sPropValue) {
      this.sPropValue = sPropValue;
   }

   @Override
   public String getPrintScreenPath() {
      return sFilePrintScreen;
   }

   @Override
   public boolean isStopped() {
      return boIsStopped;
   }

   @Override
   public void setIsStopped(boolean boIsStopped) {
      this.boIsStopped = boIsStopped;
   }

   @Override
   public boolean isCorrect() {
      return boIsCorrect;
   }

   @Override
   public void setIsCorrect(boolean boIsCorrect) {
      this.boIsCorrect = boIsCorrect;
   }

   @Override
   public String getMessage() {
      return sMessage;
   }

   @Override
   public void setMessage(String sValue) {
      this.sMessage = sValue;
   }

   @Override
   public Object getResultTask() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Map<String, Object> getResultContent() {
      return oResultContent;
   }

   public void setResultContent(Map<String, Object> oResultContent) {
      this.oResultContent = oResultContent;
   }

   public void setDiagnostic(DiagnosticDTO oDiagnosticDTO) {
      this.oDiagnostic = oDiagnosticDTO;
   }

   @Override
   public DiagnosticDTO getDiagnostic() {
      return oDiagnostic;
   }

   @Override
   public String getClassName() {
      return sClassName;
   }

   @Override
   public void setClassName(String sClassName) {
      this.sClassName = sClassName;
   }

   @Override
   public String toString() {
      return sClassName + " :{ Message: " + sMessage + ", Result : " + boIsCorrect + " }";
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
