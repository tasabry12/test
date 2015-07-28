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
package com.venere.ace.bl.placeholders;

/**
 *
 * @author fdifabio
 */
public class InternalPlaceholderFactory {
   private static IInternalPlaceholder storedRandom;
   public static IInternalPlaceholder getInstance(String sWhat) {
      switch(sWhat) {
         case "internal.uuid":
            return new UUID();
         case "internal.random":
            storedRandom = new Random();
            return storedRandom;
         case "internal.random.repeat":
            return storedRandom;
         default:
            throw new UnsupportedOperationException("[" + sWhat + "] is not a recognized internal placeholder");
      }
   }
}
