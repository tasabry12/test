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
package com.venere.ace.dao;

import com.venere.ace.abstracts.AHandler;
import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.dtos.ConfigurationDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.exception.EManagerException;
import com.venere.ace.idtos.IMetaPlanRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.utility.Constants;
import com.venere.ace.utility.FolderManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author quatrini
 */
public class ScenarioHandler extends AHandler {

   public ScenarioHandler(String sClassName) {
      super(sClassName);
   }
   
   public ScenarioHandler() {
      super("ScenarioHandler");
   }

   public ICommandResult importMetaplan(IMetaPlanRequestDTO oPlanrerquest) throws EHandlerException {
      ConfigurationDTO oConfigurationDTO = new ConfigurationDTO();
      ATaskResultDTO scenarioResult = null;

      try {
         oConfigurationDTO.setPathFileToLoad(FolderManager.MetaplansFolder + oPlanrerquest.getMetaplanname() + ".xml");
         oConfigurationDTO.setMainProps(oPlanrerquest.getPropertiesFile());
         Map oConfAdd = oConfigurationDTO.getConfigurationAdds()==null?new HashMap():oConfigurationDTO.getConfigurationAdds();
         oConfAdd.put("optional", oPlanrerquest.isOptional());
         oConfigurationDTO.setConfigurationAdds(oConfAdd);
         List<ICommandResult> oResultList = oManager.manager(oConfigurationDTO);
         scenarioResult = (ATaskResultDTO) aggregateResult(oResultList,new ATaskResultDTO("ScenarioTaskResult"),"Scenario "+oPlanrerquest.getMetaplanname()+" Executed");
      } catch (EManagerException ex) {
         String sMessage = "Scenario Execution Failed \"" + oPlanrerquest.getMetaplanname() + "\"";
         oLogger.error(sMessage + " - " + ex.getCause());

         scenarioResult = new ATaskResultDTO("ScenarioTaskResult");
         scenarioResult.setIsCorrect(false);
         scenarioResult.setMessage(sMessage);
         
      }catch(Exception ex){
         String sMessage = " Unexpected error during  Metaplan Import   " + ex.getMessage();
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.SCENARIO_TASK,sMessage, ex);      
      }

      return scenarioResult;

   }

   public ICommandResult closeAll(IMetaPlanRequestDTO obj) throws EHandlerException {

      ATaskResultDTO scenarioResult = null;
      ConfigurationDTO oConfigurationDTO = new ConfigurationDTO();
      try {
         Map confAdds = new HashMap();

         confAdds.put(Constants.CONDITION_TAG, Constants.CONDITION_STOP);
         oConfigurationDTO.setConfigurationAdds(confAdds);

         oConfigurationDTO.setPathFileToLoad(FolderManager.MetaplansFolder + "m_closeSession.xml");
         oConfigurationDTO.setMainProps(obj.getPropertiesFile());

         List<ICommandResult> oResultList = oManager.manager(oConfigurationDTO);
         scenarioResult = (ATaskResultDTO) aggregateResult(oResultList,new ATaskResultDTO("ScenarioTaskResult"),"Scenario CloseAll Executed");
      } catch (Exception ex) {
         String sMessage = "Scenario Execution Failed ";
         oLogger.error(sMessage);        
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK,sMessage, ex);
      }
      return scenarioResult;
   }
}
