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

package com.venere.ace.utility;

/**
 *
 * @author quatrini
 */
public enum EnumBrowserTask implements IEnum {

    CLICK       ("click"),
    SUBMIT      ("submit"),
    OPEN        ("open"),
    TYPE        ("type"),
    CLOSE       ("close"),
    SELECT      ("select"),
    SELECTVALUE ("selectValue"),
    GETTEXT     ("getText"),
    HOVER       ("hover"),
    TOGGLE      ("toggle"),
    CLOSEALL    ("closeAll"),
    PRINTSCREEN ("makeAPrintScreen"),
    GETHTML     ("getHtmlPage"),
    SETCOOKIE   ("setCookie"),    
    SWITCH_TO_FRAME     ("switchToFrame"),
    SWITCH_TO_DEFAULT   ("switchToDefaultContent"),
    ALERT_OK            ("confirmAlert"),
    SET_RESERV_NUM_PROP ("setReservationNumberProperty"),
    SET_PROP_FROM_TEXT  ("setPropertyFromText"),
    SET_PROP_BY_ATTR    ("setPropertyFromAttributeValue"),
    SCROLL_TO_ELEM      ("scrollToElem"),
    CLICK_CHECKBOX      ("clickCheckBox"),
    CLICK_BF_BOOK      ("clickBfBook"),
    SET_DATES_JS       ("setDatesJs"),
    SELECTVALUE_JS     ("selectValueJs"),
    SELECT_JS          ("selectJs"),
    SELECT_BY_POSITION ("selecByPositiontJs"),
    ROTATE             ("rotate"),
    
    //added for app android
    OPEN_MENU     ("openAndroidMenu"),
    BACK          ("androidBack");    
    
    
    
    private final String sAction;

    EnumBrowserTask(String  sAction){
      this.sAction = sAction;
    }
   @Override
    public String getActionCode() { return sAction; }

   @Override
   public IEnum getTerminateCode() {
      return CLOSEALL;
   }
}
