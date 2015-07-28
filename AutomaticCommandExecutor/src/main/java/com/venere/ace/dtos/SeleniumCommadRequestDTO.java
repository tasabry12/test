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
package com.venere.ace.dtos;

import com.venere.ace.idtos.ICommandRequestDTO;


/**
 *
 * @author quatrini
 */
public class SeleniumCommadRequestDTO implements ICommandRequestDTO {

   private String sValue;
   private String sTaskName;
   private String sLocationElement;
   private String sLocator;
   private int sDelayMillisecond;
//   private String sDependOfCondition;
//
//   public void setDependOfCondition(String sDependOfCondition) {
//      this.sDependOfCondition = sDependOfCondition;
//   }
//
//   @Override
//   public String getDependOfCondition() {
//      return sDependOfCondition;
//   }

   @Override
   public int getDelayMillisecond() {
      return sDelayMillisecond;
   }

   @Override
   public void setDelayMillisecond(int sDelayMillisecond) {
      this.sDelayMillisecond = sDelayMillisecond;
   }

   @Override
   public void setValue(String sValue) {
      this.sValue = sValue;
   }

   @Override
   public void setLocationElement(String sLocationElement) {
      this.sLocationElement = sLocationElement;
   }

   @Override
   public void setLocatorType(String sLocator) {
      this.sLocator = sLocator;
   }

   @Override
   public String getValue() {
      return sValue;
   }

   @Override
   public String getLocationElement() {
      return sLocationElement;
   }

   @Override
   public String getLocatorType() {
      return sLocator;
   }

   @Override
   public void setTaskName(String sValue) {
      sTaskName = sValue;

   }

   @Override
   public String getTaskName() {
      return sTaskName;
   }

   @Override
   public String toString() {
      String sReturn = "Selenium request :{TaskName :\"" + sTaskName + "\" ,Value:\"" + sValue + "\" ,Method:\"" + sLocator + "\" , Locator element:\"" + sLocationElement + "\" }";
      return sReturn;
   }

}
