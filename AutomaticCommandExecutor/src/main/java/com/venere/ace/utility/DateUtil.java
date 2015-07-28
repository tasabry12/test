/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
package com.venere.ace.utility;

import com.venere.ace.exception.EHandlerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fcastaldi
 */
public class DateUtil {

   public static String getDataValue(String sValue, String sDateFormat) {
      SimpleDateFormat oFormat = new SimpleDateFormat(sDateFormat);
      try {
         if (sDateFormat.contains("/")) {
            //in this case i have e well format string date
            oFormat.parse(sValue);
         } else {
            //in this case i must use the value as a delay days
            sValue = formatDate(sValue, oFormat);
         }
      } catch (ParseException ex) {
         // an easy way to check if a date is well formatted
         sValue = formatDate(sValue, oFormat);
      }

      sDateFormat = "dd/MM/yyyy";
      return sValue;
   }

   public static String formatDate(String sValue, SimpleDateFormat oFormat) {

      Calendar oCal = new GregorianCalendar();
      oCal.add(Calendar.DATE, new Integer(sValue));
      return oFormat.format(oCal.getTime());

   }
   
   public static String addDay(String sCalendarDay, int iOffset, String sDateFormat) {

      if (sCalendarDay.contains("/")) {

         SimpleDateFormat oFormat = new SimpleDateFormat(sDateFormat);
         Date oDate = null;
         try {
            oDate = oFormat.parse(sCalendarDay);
         } catch (ParseException ex) {
            //// NON LO SO
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
         Calendar oCal = new GregorianCalendar();
         oCal.setTime(oDate);
         oCal.add(Calendar.DATE, iOffset);
         return oFormat.format(oCal.getTime());
      } else {
         
         int iStartDate = Integer.parseInt(sCalendarDay);
         return "" + (iStartDate + iOffset);

      }

   }

   public static String dateFormatter(Date oDate) throws EHandlerException {
      String sP = "";
      try {
         SimpleDateFormat oF = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
         sP = oF.format(oDate);
      } catch (RuntimeException ex) {
         String sMessage = " Date Formatter Failure   " + ex.getMessage();
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }

      return sP;
   }
}
