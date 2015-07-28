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
package com.venere.ace.abstracts;

import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.IHandler;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author quatrini
 */
public class AHandler implements IHandler {

   protected Log oLogger;
   protected AManager oManager;
   protected String sClassName;

   public AHandler(String sClassName) {
      this.sClassName = sClassName;  
      oLogger = LogFactory.getLog(sClassName);

   }
   public AHandler() {
      oLogger = LogFactory.getLog(sClassName);
  }

   @Override
   public AManager getManager() {
      return oManager;
   }

   @Override
   public void setManager(AManager oTaskManager) {
      this.oManager = oTaskManager;
   }

   @Override
   public boolean isHanlderStopped() {
      return false;
   }
   
     protected ICommandResult aggregateResult(List<ICommandResult> oResultList, ICommandResult oCurrentDTO, String sMessage) {
      boolean isCorrectResult = true;
      boolean isStopped = false;
      oCurrentDTO.setIsCorrect(true);
      oCurrentDTO.setMessage(sMessage);

      for (Iterator<ICommandResult> oIt = oResultList.iterator(); oIt.hasNext();) {
         ICommandResult oCurrResult = (ICommandResult) oIt.next();
         if (oCurrResult != null) {
            isCorrectResult &= oCurrResult.isCorrect();
            isStopped |= oCurrResult.isStopped();

            oCurrentDTO.setIsCorrect(isCorrectResult);
            oCurrentDTO.setIsStopped(isStopped);

            if (isStopped) {
               oCurrentDTO.setMessage(oCurrResult.getMessage());
               break;
            }
         }
      }

      return oCurrentDTO;
   }

   
   
}
