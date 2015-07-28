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

package com.venere.ace.interfaces;

import com.venere.ace.dtos.CheckConditionDTO;
import com.venere.utils.dto.DiagnosticDTO;
import java.util.Map;

/**
 *
 * @author quatrini
 */

public interface ICommandResult {
    
   public Object getResultTask();

   public boolean isCorrect() ;

   public Map<String, Object> getResultContent();

   public String getMessage() ;
   
   public void   setMessage(String sMessage) ;
   
   public String getPrintScreenPath();
   
   public boolean isStopped();
   
   public void setIsStopped(boolean isStopped);
   
   public void setIsCorrect(boolean isCorrect);

   public void setDiagnostic(DiagnosticDTO oDiagnosticDTO);
   
   public DiagnosticDTO getDiagnostic();
   
   public CheckConditionDTO getCondition();

   public void setCondition(CheckConditionDTO oCondition);
   
   public String getClassName() ;

   public void setClassName(String sClassName) ;
   
}
