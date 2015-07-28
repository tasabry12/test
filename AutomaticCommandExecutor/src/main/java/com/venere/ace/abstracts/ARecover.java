/*                   Copyright (c) 2007 Venere Net S.p.A.
 *                             All Rights Reserved
 *
 * This software is the confidential and proprietary information of
 * Venere Net S.r.l. ("Confidential  Information"). You  shall not disclose
 * such  Confidential Information and shall use it only in accordance with
 * the terms  of the license agreement you entered into with Venere Net S.p.A.
 *
 * VENERE NET S.r.l. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR NON-INFRINGEMENT. VENERE NET S.P.A. SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS
 * SOFTWARE OR ITS DERIVATIVES.
 */
package com.venere.ace.abstracts;

import com.venere.ace.dtos.SeleniumCommadRequestDTO;
import com.venere.ace.exception.EExecutorException;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.IHandler;
import com.venere.ace.interfaces.IRecover;
import com.venere.ace.utility.EnumBrowserTask;
import com.venere.utils.dto.UtilityRestoreDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cquatrini
 */
public abstract class ARecover implements IRecover {

   protected Log oLogger = LogFactory.getLog(getClass());
   protected int iNumberRetries ;
   protected List<String> oBlackList = new ArrayList<String>();

   public int getNumberRetries() {
      return iNumberRetries;
   }

   public void setNumberRetries(int iNumberRetries) {
      this.iNumberRetries = iNumberRetries;
   }

   @Override
   public abstract ICommandResult restore(AHandler handler, UtilityRestoreDTO oUtilityRestore);

   protected ICommandResult executeRecover(List<ICommandExecutor> oRecoverList) {
      ICommandResult oResulTaskAction = null;
      boolean boIsCotinue = true;
      Iterator oIt = oRecoverList.iterator();
      while (boIsCotinue && oIt.hasNext()) {
         ICommandExecutor oCommandExecutor = (ICommandExecutor) oIt.next();

         try {
            oResulTaskAction = oCommandExecutor.doTask();
            if (!oResulTaskAction.isCorrect()) {
               boIsCotinue = false;
            }
         } catch (EExecutorException ex) {
            oLogger.error("Error during recover operation: " + ex.getMessage());
            oResulTaskAction.setIsCorrect(false);
         }
      }
      return oResulTaskAction;

   }

   protected ICommandExecutor createActionTask(IHandler oHandler, String sDescription, EnumBrowserTask oTaskID, String sObjectLocator, String sObjectValue, String sObjectLocation) {
      AExecutor oCurrentAction = new AExecutor("ActionTask");
      oCurrentAction.setClassName("XXXXXXXXXXXXXX");
      oCurrentAction.setHandler(oHandler);
      SeleniumCommadRequestDTO oRequestDTO = new SeleniumCommadRequestDTO();
      oRequestDTO.setDelayMillisecond(2000);
      oRequestDTO.setLocationElement(sObjectLocation);
      oRequestDTO.setLocatorType(sObjectLocator);
      oRequestDTO.setValue(sObjectValue);
      oRequestDTO.setTaskName(sDescription);
      oCurrentAction.setTaskDescription(sDescription);
      oCurrentAction.setRequest(oRequestDTO);
      oCurrentAction.setTaskId(oTaskID);

      return oCurrentAction;
   }
   
      protected String getLastHotelNameSearched(UtilityRestoreDTO oUtilityRestore) {
      List oExecutedCommand = oUtilityRestore.getExecutedActions();
      ListIterator iter = oUtilityRestore.getExecutedActions().listIterator(oExecutedCommand.size() - 3);
      boolean isFoundElement = false;
      String sLastHotelNameSearched = null;
      while (iter.hasNext() && !isFoundElement) {
         ICommandExecutor oCommandExecuted = (ICommandExecutor) iter.next();
         String sLocationElem = ((SeleniumCommadRequestDTO) oCommandExecuted.getRequest()).getLocationElement();
         if ("hotelname".equals(sLocationElem)) {
            isFoundElement         = true;
            sLastHotelNameSearched = ((SeleniumCommadRequestDTO) oCommandExecuted.getRequest()).getValue();
         }
      }
      return sLastHotelNameSearched;
   }
}
