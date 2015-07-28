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

import com.venere.ace.bl.loaders.TestClassLoader;
import com.venere.ace.dtos.ConfigurationDTO;
import com.venere.ace.exception.EExecutorException;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.IOperator;
import com.venere.ace.utility.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author quatrini
 */
public class TestManager {

   private TestClassLoader oTestLoader;
   protected Log oLogger = LogFactory.getLog("TestManager");
   private Map<String, Object> oMapResultContainer;
   private static Map<String, Object> oConditionsMap;

   public static Map<String, Object> getConditionsMap() {
      return oConditionsMap;
   }

   public void setConditionsMap(Map<String, Object> oConditionsMap) {
      TestManager.oConditionsMap = oConditionsMap;
   }

   public Map<String, Object> getMapResultContainer() {
      return oMapResultContainer;
   }

   public void setMapResultContainer(Map<String, Object> oMapResultContainer) {
      this.oMapResultContainer = oMapResultContainer;
   }

   public TestClassLoader getoTestLoader() {
      return oTestLoader;
   }

   public void setTestLoader(TestClassLoader oTestLoader) {
      this.oTestLoader = oTestLoader;
   }

   public void loadTestXml(String sFilePath, Properties oProperties) {
      ConfigurationDTO oConf = new ConfigurationDTO();
      oConf.setMainProps(oProperties);
      oConf.setPathFileToLoad(FolderManager.TestCaseFolder + sFilePath + ".xml");
      oTestLoader.load(oConf);
   }

   public List<ICommandResult> selectDoTask(IEnum oPos, String sListofTest) throws EExecutorException {

      List<ICommandResult> oResultList = new ArrayList<>();

      if (oTestLoader.getPointCutsMap() != null) {
         Map oMapPointPosition = (Map) oTestLoader.getPointCutsMap().get(oPos.getActionCode());
         List<ICommandExecutor> oList = (List<ICommandExecutor>) (oMapPointPosition.get(sListofTest));
         if (oList != null) {
            for (Iterator<ICommandExecutor> oIt = oList.iterator(); oIt.hasNext();) {

               ICommandExecutor oCommandExecutor = oIt.next();

               boolean boEvaluation = true;
               if(oCommandExecutor.getRequest()instanceof ITestRequestDTO){
                  boEvaluation = evaluateCondition((ITestRequestDTO) oCommandExecutor.getRequest());               
               }
               
               if (boEvaluation) {

                  ICommandResult oResult = oCommandExecutor.doTask();

                  if (oResult.getCondition() != null) {
                     oConditionsMap.put(oResult.getCondition().getConditionId(), oResult.getCondition().getConditionResult());
                  } else {
                     oMapResultContainer.put(oCommandExecutor.getTaskDescription(), oResult);
                     oResultList.add(oResult);
                  }
               }
            }
         }
      }

      return oResultList;
   }


   private boolean evaluateCondition(ITestRequestDTO request) {
      boolean boEvaluation = true;
      try {
         if (request.getDependOfCondition() != null) {
            List<String> splittedCond = StringUtil.pipeSplit(request.getDependOfCondition());
          
            String sClassName= EnumTestConditions.valueOf(splittedCond.get(0).toUpperCase()).getActionCode();
            IOperator oOperator = (IOperator) Class.forName("com.venere.ace.utility."+sClassName).newInstance();
            boEvaluation = oOperator.evaluate(request.getDependOfCondition(), oConditionsMap);
            oLogger.info("The check " + request.getCheckDescription() + " depends on the condition: " + request.getDependOfCondition() + ". The condition value is: " + boEvaluation);
         }
      } catch (Exception e) {
         oLogger.warn("Error during condition '"+request.getDependOfCondition()+"' evaluation. The check " + request.getCheckDescription() + " will be executed");

      }

      return boEvaluation;
   }
}
