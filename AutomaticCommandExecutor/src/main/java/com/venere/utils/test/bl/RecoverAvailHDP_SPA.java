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

import com.venere.ace.abstracts.AHandler;
import com.venere.ace.abstracts.ARecover;
import com.venere.ace.dtos.DataAlternativeRequestDTO;
import com.venere.ace.dtos.DataAlternativeResponseDTO;
import com.venere.ace.dtos.SeleniumCommadRequestDTO;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.utility.Constants;
import com.venere.ace.utility.DataFileAlternativeRetrieverDAO;
import com.venere.ace.utility.DateUtil;
import com.venere.ace.utility.EnumBrowserTask;
import com.venere.utils.dto.UtilityRestoreDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author fcastaldi
 */
public class RecoverAvailHDP_SPA extends ARecover {

   protected int dateOffset;
   protected DataFileAlternativeRetrieverDAO oAlternativeDataRetriever;

   public DataFileAlternativeRetrieverDAO getAlternativeDataRetriever() {
      return oAlternativeDataRetriever;
   }

   public void setAlternativeDataRetriever(DataFileAlternativeRetrieverDAO alternativeDataRetriever) {
      this.oAlternativeDataRetriever = alternativeDataRetriever;
   }

   public int getDateOffset() {
      return dateOffset;
   }

   public void setDateOffset(int dateOffset) {
      this.dateOffset = dateOffset;
   }

   @Override
   /**
    * This Method try to restore navigation using alternative browsing path
    *
    */
   public ICommandResult restore(AHandler oHandler, UtilityRestoreDTO oUtilityRestore) {
      ICommandResult oResult = null;
      
      List<ICommandExecutor> oExecutedActions = oUtilityRestore.getExecutedActions();
      int iExecutedActionSize = oUtilityRestore.getExecutedActions().size();
      ICommandExecutor oLastAction = oExecutedActions.get(iExecutedActionSize - 1);
      String sLastObjectLocation =((SeleniumCommadRequestDTO)oLastAction.getRequest()).getLocationElement();
//      boolean boMakeRestore = !"guest_firstname_id".equals(sLastObjectLocation);
//      if(!boMakeRestore){
//         oLogger.info("No actions to restore navigation: the action failed was in BF");      
//      }
      
//      if (iNumberRetries > 0 && boMakeRestore) {
      if (iNumberRetries > 0) {
         oLogger.info("Starting Restore execution ");
         
         String sLastHotelNameSearched   = getLastHotelNameSearched(oUtilityRestore);         
         oBlackList = new ArrayList<>();
         if(sLastHotelNameSearched != null){
            oBlackList.add(sLastHotelNameSearched);
         }
         
         Properties oAllProperties = oUtilityRestore.getAllProperties();
         String sDateFormat = oAllProperties.getProperty("com.venere.ace.dateFormat");         

         String sTypology = oAllProperties.getProperty(Constants.TYPOLOGY_PROPERTY);
         if (sTypology == null) {
            oResult = executeDatesRecoverAction(oHandler, oUtilityRestore.getAllProperties().getProperty("com.venere.ace.checkin"), oUtilityRestore.getAllProperties().getProperty("com.venere.ace.checkout"), sDateFormat, oUtilityRestore);
         } else {
            oResult = executeTypologyBasedRecoverAction(sTypology, oHandler, oUtilityRestore.getAllProperties().getProperty("com.venere.ace.checkin"), oUtilityRestore.getAllProperties().getProperty("com.venere.ace.checkout"), oUtilityRestore);
         }

      }
      return oResult;
   }

   /**
    * This method repeat the hotel search changing the dates. Any try a offset
    * is added to original dates. This offset is defined into
    * external-config-recoverSystem.properties , The tries number correspond to
    * the number of retries defined into
    * external-config-recoverSystem.properties file
    *
    * @param oHandler handler needed to recover operation
    * @param sCheckin checkin date setted into properties
    * @param sCheckout checkout date setted into properties
    * @param sDateFormat date format
    * @param oUtilityRestore information needed for restoring operation
    * @return the result of recover operation
    */
   private ICommandResult executeDatesRecoverAction(AHandler oHandler, String sCheckin, String sCheckout, String sDateFormat, UtilityRestoreDTO oUtilityRestore) {
      ICommandResult oR = null;
      int iExecutedActionSize = oUtilityRestore.getExecutedActions().size();
      for (int i = 0; i < iNumberRetries; i++) {

         List<ICommandExecutor> oRecoverActions = new ArrayList<>();
         String sNewCheckIn = DateUtil.addDay(sCheckin, (i + 1) * dateOffset, sDateFormat);
         String sNewCheckOut = DateUtil.addDay(sCheckout, (i + 1) * dateOffset, sDateFormat);
         
         //con la form di non disponibilità, la prima azione da fare è il click per aprire il cercatore
         oRecoverActions.add(createActionTask(oHandler, "click.searchSRP", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//div[@id='w1']/div[1]"));
         
         //cambio checkin
         oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//*[@class='vui-input input-checkin hasDatepicker']"));
         oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "cssSelector", "", ".ui-icon.ui-icon-circle-triangle-e"));
         oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//*[@id='ui-datepicker-div']/table/tbody/tr[3]/td[4]"));
         
         //click Search button
         oRecoverActions.add(createActionTask(oHandler, "click.searchSRP", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//div[@class='input_find_container']/input"));
         
         List<ICommandExecutor> oExecutedActions = oUtilityRestore.getExecutedActions();
         oRecoverActions.add(oExecutedActions.get(iExecutedActionSize - 1));
         oR = executeRecover(oRecoverActions);
         if (oR.isCorrect()) {
            break;
         }
      }
      return oR;
   }

   /**
    * This method repeat the hotel search using a same typology hotel list
    * present in external-config-recoverSystem.properties file.The tries number
    * correspond to the number of retries defined into
    * external-config-recoverSystem.properties file
    *
    * @param sTypology typology hotel (eem, omsDual, ean)
    * @param oHandler handler needed to recover operation
    * @param sCheckin checkin date setted into properties
    * @param sCheckout checkout date setted into properties
    * @param oUtilityRestore information needed for restoring operation
    * @return the result of recover operation
    */
   private ICommandResult executeTypologyBasedRecoverAction(String sTypology, AHandler oHandler, String sCheckin, String sCheckout, UtilityRestoreDTO oUtilityRestore) {
      ICommandResult oResult = null;
      int iAlternativeNumber = 0;
      DataAlternativeRequestDTO oDataAlternativeRequest = new DataAlternativeRequestDTO();
      oDataAlternativeRequest.setBlackListHotel(oBlackList);
      oDataAlternativeRequest.setTypology(sTypology);
      
      List<ICommandExecutor> oExecutedActions = oUtilityRestore.getExecutedActions();
      int iExecutedActionSize = oUtilityRestore.getExecutedActions().size();
      ICommandExecutor oLastAction = oExecutedActions.get(iExecutedActionSize - 1);
      
      for (int i = 0; i < iNumberRetries; i++) {

         DataAlternativeResponseDTO oAlternativeHotel = oAlternativeDataRetriever.getSameHotelTypology(oDataAlternativeRequest);
         
         iAlternativeNumber++;
         String sDestination = oAlternativeHotel.getAlternativeHotel().getDestination();
         String sHotelName = oAlternativeHotel.getAlternativeHotel().getHotelName();
         if (oAlternativeHotel != null && sDestination != null && sHotelName != null) {
            oBlackList.add(sHotelName);
            oResult = executeRecover(createListRecoverAction(oHandler, sCheckin, sCheckout, sDestination, sHotelName, oLastAction));
            if (oResult.isCorrect()) {
               break;
            }
         }
      }
      return oResult;
   }

   /**
    * This method create a list of actions to execute for recovering
    * operations.It makes A complete search in srp search form
    *
    * @param oHandler
    * @param sNewCheckIn
    * @param sNewCheckOut
    * @param sDestination
    * @param sHotelName
    * @param oUtilityRestore
    * @return
    */
   private List createListRecoverAction(AHandler oHandler, String sNewCheckIn, String sNewCheckOut, String sDestination, String sHotelName, ICommandExecutor oLastExecutedAction) {
      ArrayList oRecoverActions = new ArrayList<>();
        
      oRecoverActions.add(createActionTask(oHandler, "click.searchSRP", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//div[@id='w1']/div[1]"));
         
      oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//*[@class='vui-input input-checkin hasDatepicker']"));
      oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "cssSelector", "", ".ui-icon.ui-icon-circle-triangle-e"));
      oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//*[@id='ui-datepicker-div']/table/tbody/tr[3]/td[4]"));
         
      oRecoverActions.add(createActionTask(oHandler, "click.searchSRP", EnumBrowserTask.valueOf("CLICK"), "xpath", "", "//div[@class=\"input_find_container\"]/input"));
         
      oRecoverActions.add(oLastExecutedAction);
      return oRecoverActions;
   }
}
