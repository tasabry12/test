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
package com.venere.utils.test.bl;

import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author fcastaldi
 */
public class ComparatorChecker extends AClassTestBL {

   String sFirstString = "";
   String sSecondString = "";
   String comparator = "";

   public ComparatorChecker() {
      super("ComparatorChecker");
   }
   
   /*
    * the attribute data into check file is like 
    * data=string1|equals|string2"
    *             or
    * data=string1|equals|string2|negative" (correspond to !equals)
    */
   public ICommandResult compareString(ITestRequestDTO oTest) throws EHandlerException {
      checkInput(oTest);
      boolean verified = false;
      sMessageResult = initializeMessageResult(oTest);
      try {
         sFirstString = (String) oListelement.get(0);
         comparator = (String) oListelement.get(1);
         sSecondString = (String) oListelement.get(2);
         String sTypologyTest = "positive";
         if (oListelement.size() == 4) {
            sTypologyTest = (String) oListelement.get(3);
         }
         verified = stringComparator(comparator);

         sMessageResult +="'"+ sFirstString + "' " + comparator + " '" + sSecondString + "' = " + verified;
         boResultTest = sTypologyTest.equals("positive") ? verified : !verified;

      } catch (Exception ex) {
         String sMessage = ex.getMessage();
         setExceptionMessageResult(oTest);
         oLogger.error(sMessageResult + "failure");
         oLogger.error(sMessage);
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, ex);
      }
      ATaskResultDTO oResult = populateResult(oTest);
      return oResult;
   }

   private boolean stringComparator(String methodName) throws EHandlerException, InvocationTargetException {
      boolean verified = false;

      try {
         Method[] methods = String.class.getMethods();
         for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equalsIgnoreCase(methodName)) {
               verified = (boolean) methods[i].invoke(sFirstString, sSecondString);
            }
         }
      } catch (IllegalAccessException | InvocationTargetException e) {
         String sMessage = "Failure during comparator method " + methodName;
         throw new EHandlerException(EHandlerException.HandlerErrorCode.ACTION_TASK, sMessage, e);
      }
      return verified;

   }
}
