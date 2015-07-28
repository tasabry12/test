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

package com.venere.ace.dtos;

import com.venere.ace.interfaces.ICommandResult;
import com.venere.utils.dto.DiagnosticDTO;
import java.util.Map;

/**
 *
 * @author quatrini
 */
public class ATaskResultDTO implements ICommandResult{

   private String sClassName;
   private String sValue;
   private boolean boIsCorrect;
   private Map<String,Object> oResultContent;
   private String sFilePrintScreen;
   private boolean boIsStopped;
   private DiagnosticDTO oDiagnostic;
   private CheckConditionDTO oCondition;

   @Override
   public CheckConditionDTO getCondition() {
      return oCondition;
   }

   @Override
   public void setCondition(CheckConditionDTO oCondition) {
      this.oCondition = oCondition;
   }
    
   public ATaskResultDTO(){
      this.sClassName = "Generic Task Result";
   }
   
   public ATaskResultDTO(String sClassName){
      this.sClassName = sClassName;
   }
   
   public void setFilePrintScreen(String sFilePrintScreen) {
      this.sFilePrintScreen = sFilePrintScreen;
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
   public boolean isCorrect() {
      return boIsCorrect;
   }

   @Override
   public Map<String, Object> getResultContent() {
      return oResultContent;
   }

   public void setResultContent(Map<String, Object> oResultContent) {
      this.oResultContent = oResultContent;
   }

   @Override
   public String getMessage() {
      return sValue;
   }

   @Override
   public void setMessage(String sValue) {
      this.sValue = sValue;
   }
   
   @Override
   public String toString(){
      return sClassName+" :{ Message: "+sValue+", Result : "+boIsCorrect+" }";
   }

   @Override
   public Object getResultTask() {
      throw new UnsupportedOperationException("Not supported yet.");
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
        this.boIsStopped = boIsStopped ;
    }

    @Override
    public void setIsCorrect(boolean boIsCorrect) {
        this.boIsCorrect = boIsCorrect;
    }

    @Override
    public void setDiagnostic(DiagnosticDTO oDiagnosticDTO) {
        this.oDiagnostic=oDiagnosticDTO;
    }

    @Override
    public DiagnosticDTO getDiagnostic() {
        return oDiagnostic;
    }

}
