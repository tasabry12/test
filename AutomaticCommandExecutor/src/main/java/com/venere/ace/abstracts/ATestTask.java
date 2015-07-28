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

import com.venere.ace.dtos.ConfigurationDTO;
import com.venere.ace.interfaces.ITestHandler;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author quatrini
 */
public abstract class ATestTask extends AExecutor{

   protected Map               oPropertiesMap;
   protected Map               oTestMap;
   protected String            sTestID;
   protected String            sScenarioID;
   protected Properties        oMainProperties;
   protected ITestHandler      oTestHandler;

   protected ConfigurationDTO  oConfigurations;

   public ATestTask(String sClassName) {
      super(sClassName);
   }

//   public ConfigurationDTO getAddsConfiguration() {
//      return oConfigurations;
//   }

//   public void setAddConfiguration(ConfigurationDTO oConfigurations) {
//      this.oConfigurations = oConfigurations;
//   }

//   public EnumTestTask getTestTask() {
//      return oTaskId;
//   }
//
//   public void setTestTask(EnumTestTask oTestTask) {
//      this.oTaskId = oTestTask;
//   }

//   public ITestHandler getTestHandler() {
//      return oTestHandler;
//   }
//
//   public void setTestHandler(ITestHandler oTestHandler) {
//      this.oTestHandler = oTestHandler;
//   }


   public Map getPropertiesMap() {
      return oPropertiesMap;
   }

   public void setPropertiesMap(Map oPropertiesMap) {
      this.oPropertiesMap = oPropertiesMap;
   }

   public Map getTestMap() {
      return oTestMap;
   }

   public void setTestMap(Map oTestMap) {
      this.oTestMap = oTestMap;
   }

//
//   public Log getoLogger() {
//      return oLogger;
//   }
//
//   public void setLogger(Log oLogger) {
//      this.oLogger = oLogger;
//   }

   public Properties getMainProperties() {
      return oMainProperties;
   }

   public void setMainProperties(Properties oMainProperties) {
      this.oMainProperties = oMainProperties;
   }

   public String getScenarioID() {
      return sScenarioID;
   }

   public void setScenarioID(String sScenarioID) {
      this.sScenarioID = sScenarioID;
   }

   public String getTestID() {
      return sTestID;
   }

   public void setTestID(String sTestID) {
      this.sTestID = sTestID;
   }
//
//   @Override
//   public String toString() {
//      String sMessage ="{Test Id: \""+sTestID+"\",Scenario Id: \""+sScenarioID+"\", Configuration :\""+oConfigurations.toString()+"\"}";
//      return sMessage;
//   }


   

}
