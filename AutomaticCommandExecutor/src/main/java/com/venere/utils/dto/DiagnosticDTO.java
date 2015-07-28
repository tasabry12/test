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
package com.venere.utils.dto;

import com.venere.ace.interfaces.IRecover;

/**
 *
 * @author fcastaldi
 */
public class DiagnosticDTO {
    
    private String sDiagnosis;
    private int iId;
    private IRecover oRecover;
       
    public DiagnosticDTO(int id, String message, IRecover recover, int numberOfRetries){
        this.iId = id;
        this.sDiagnosis = message;
        this.oRecover = recover;       
    }

    public DiagnosticDTO() {        
    }

    public int getId() {
        return iId;
    }
    
    public void setId(int sId) {
        iId = sId;
    }
    
    public String getDiagnosis() {
        return sDiagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.sDiagnosis = diagnosis;
    }
    public IRecover getRecover() {
        return oRecover;
    }

    public void setRecover(IRecover recover) {
        this.oRecover = recover;
    }
}
