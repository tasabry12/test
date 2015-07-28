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

package com.venere.ace.dtos;

import java.util.List;

/**
 *
 * @author cquatrini
 */
public class DataAlternativeRequestDTO {
  
   private int iCurrentAlternative;
   private String sTypology;
   private List oBlackListHotel;

   public List getBlackListHotel() {
      return oBlackListHotel;
   }

   public void setBlackListHotel(List oBlackListHotel) {
      this.oBlackListHotel = oBlackListHotel;
   }

   public String getTypology() {
      return sTypology;
   }

   public void setTypology(String sTypology) {
      this.sTypology = sTypology;
   }

   public int getCurrentAlternative() {
      return iCurrentAlternative;
   }

   public void setCurrentAlternative(int iCurrentAlternative) {
      this.iCurrentAlternative = iCurrentAlternative;
   }

}
