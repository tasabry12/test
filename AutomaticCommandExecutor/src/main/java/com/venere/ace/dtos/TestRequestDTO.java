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

import com.venere.ace.idtos.ITestRequestDTO;
import java.util.List;

/**
 *
 * @author quatrini
 */
public class TestRequestDTO implements ITestRequestDTO{
   private String sTestClassName;
   private String sMethodName;
   private String sCheckDescription;
   private List   oInputDataList;
   private String sDependOfCondition;
   private CheckConditionDTO oConditionDTO;
   private String sLocatorType;
   
   
   @Override
   public String getDependOfCondition() {
      return sDependOfCondition;
   }

   public void setDependOfCondition(String sDependOfCondition) {
      this.sDependOfCondition = sDependOfCondition;
   }

   @Override
   public CheckConditionDTO getConditionDTO() {
      return oConditionDTO;
   }

   public void setConditionDTO(CheckConditionDTO oConditionDTO) {
      this.oConditionDTO = oConditionDTO;
   }
   
   @Override
   public List getInputDataList() {
      return oInputDataList;
   }

   @Override
   public void setInputDataList(List oInptDataList) {
      this.oInputDataList = oInptDataList;
   }

   @Override
   public String getMethodName() {
      return sMethodName;
   }

   @Override
   public void setMethodName(String sMethodName) {
      this.sMethodName = sMethodName;
   }

   @Override
   public String getTestClassName() {
      return sTestClassName;
   }

   @Override
   public void setTestClassName(String sTestClassName) {
      this.sTestClassName = sTestClassName;
   }

   @Override
   public void setCheckDescription(String sCheckDescription) {
      this.sCheckDescription = sCheckDescription;
   }

   @Override
   public String getCheckDescription() {
      return sCheckDescription;
   }
   
   @Override
   public String toString() {
      return sTestClassName+"{ ActionRequired: "+ sMethodName+"  }";// super.toString();
   }

   public void setLocatorType(String sLocatorType) {
      this.sLocatorType=sLocatorType;
   }
   
   public String getLocatorType() {
      return sLocatorType;
   }
}
