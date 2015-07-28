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

package com.venere.ace.bl.placeholders;

/**
 *
 * @author fcastaldi
*/

public class Random implements IInternalPlaceholder{
 
   private String storedRandom;
   private int SUP = 100000000;
   private int INF = 1;
   
   public Random(){      
   }
   
   @Override
   public String resolve() {
      if(storedRandom==null){
         storedRandom = ""+((int)((SUP-INF+1)*Math.random())+INF);
         
         

      }
      return storedRandom;
   }

}
