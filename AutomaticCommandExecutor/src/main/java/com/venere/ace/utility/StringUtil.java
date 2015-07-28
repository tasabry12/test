/*                   Copyright (c) 2013 Venere Net S.p.A.
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
package com.venere.ace.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fdifabio
 */
public class StringUtil {
   public static List<String> pipeSplit(String sSource) {
      List<String> oTokens = new ArrayList<>();      
      for (String element : sSource.split("\\|")){
          oTokens.add(element);
      }
      return oTokens;
   }
   
   public static List<String> firstPipeSplit(String sSource) {
      List<String> oTokens = new ArrayList<>();      
      oTokens.addAll(Arrays.asList(sSource.split("\\|",2)));
      return oTokens;
   }
}
