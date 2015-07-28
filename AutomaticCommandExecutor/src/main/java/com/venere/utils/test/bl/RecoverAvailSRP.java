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
public class RecoverAvailSRP extends ARecover {

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
   public ICommandResult restore(AHandler oHandler, UtilityRestoreDTO oUtilityRestore) {


      ICommandResult oR = null;
      if (iNumberRetries > 0) {
         oLogger.info("Starting Restore execution ");

         List oExecutedCommand = oUtilityRestore.getExecutedActions();
         ICommandExecutor oLastCommandExecuted = (ICommandExecutor) oExecutedCommand.get(oExecutedCommand.size() - 1);
         String sLastHotelNameSearched   = getLastHotelNameSearched(oUtilityRestore);
         String sLastDestinationSearched = null;
         oBlackList = new ArrayList<>();
         if(sLastHotelNameSearched != null){
            oBlackList.add(sLastHotelNameSearched);
         }
         
         Properties oAllProperties = oUtilityRestore.getAllProperties();
         String sDateFormat        = oAllProperties.getProperty("com.venere.ace.dateFormat");
         String sNewCheckIn        = oAllProperties.getProperty("com.venere.ace.checkin");
         String sNewCheckOut       = oAllProperties.getProperty("com.venere.ace.checkout");
         
         DataAlternativeRequestDTO oAlternativeRequestHotel   = new DataAlternativeRequestDTO();
         oAlternativeRequestHotel.setBlackListHotel(oBlackList);
         String sTypology = oAllProperties.getProperty(Constants.TYPOLOGY_PROPERTY);
         oAlternativeRequestHotel.setTypology(oAllProperties.getProperty(Constants.TYPOLOGY_PROPERTY));

         int i = 0;

         do {
            // If i don't have a typology i can't ask another Same Typology hotel, in this case retry the last configuration using new Date
            if (i % 2 == 0 || sTypology == null ) {
            
               sNewCheckIn  = DateUtil.addDay(sNewCheckIn, dateOffset, sDateFormat);
               sNewCheckOut = DateUtil.addDay(sNewCheckOut, dateOffset, sDateFormat);
               oAllProperties.setProperty("com.venere.ace.checkin", sNewCheckIn);
               oAllProperties.setProperty("com.venere.ace.checkout", sNewCheckOut);
         
            } else {
  
               DataAlternativeResponseDTO oAlternativeResponseHotel = oAlternativeDataRetriever.getSameHotelTypology(oAlternativeRequestHotel);

               sLastHotelNameSearched   = oAlternativeResponseHotel.getAlternativeHotel().getHotelName();
               sLastDestinationSearched = oAlternativeResponseHotel.getAlternativeHotel().getDestination();
               oAllProperties.setProperty("com.venere.ace.destination", sLastDestinationSearched);
               oAllProperties.setProperty("com.venere.ace.hotelname", sLastHotelNameSearched);
               oBlackList.add(sLastHotelNameSearched);
            }

            oR = executeRecover(createListRecoverAction(oHandler, sNewCheckIn, sNewCheckOut, sLastDestinationSearched, sLastHotelNameSearched, oLastCommandExecuted));
            
            i++;
         } while (!oR.isCorrect() && i < iNumberRetries);
         
         oUtilityRestore.setAllProperties(oAllProperties);
      }
      return oR;


   }

   private List createListRecoverAction(AHandler oHandler, String sNewCheckIn, String sNewCheckOut, String sDestination, String sHotelName, ICommandExecutor oLastCommandExecuted) {

      ArrayList oRecoverActions = new ArrayList<>();

      oRecoverActions.add(createActionTask(oHandler, "type.checkin", EnumBrowserTask.valueOf("SET_DATES_JS"), "id", sNewCheckIn, "checkin"));            
      oRecoverActions.add(createActionTask(oHandler, "type.checkout", EnumBrowserTask.valueOf("SET_DATES_JS"), "id", sNewCheckOut, "checkout"));
            
      if (sDestination != null && !sDestination.equals("")) {
         oRecoverActions.add(createActionTask(oHandler, "type.city", EnumBrowserTask.valueOf("TYPE"), "id", sDestination, "city"));
      }

      oRecoverActions.add(createActionTask(oHandler, "click.landingonDestinationWD", EnumBrowserTask.valueOf("CLICK"), "id", "", "find"));

      if (sHotelName != null && !sHotelName.equals("")) {
         oRecoverActions.add(createActionTask(oHandler, "type.hotelname", EnumBrowserTask.valueOf("TYPE"), "id", sHotelName, "hotelname"));
         oRecoverActions.add(createActionTask(oHandler, "click.hotelnameSearch", EnumBrowserTask.valueOf("CLICK"), "id", "", "name_filter"));
      }
      oRecoverActions.add(oLastCommandExecuted);
      return oRecoverActions;

   }


}
