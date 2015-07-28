/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.utils.dto;

import com.venere.ace.dtos.CheckConditionDTO;
import com.venere.ace.interfaces.ICommandResult;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author cquatrini
 */
public class SnapInfoDTO implements ICommandResult {
    
    private String sPathSnap;
    private String sSnapName;
    private Date   oDatePrint;
    private String sPrintScreenPath;
    private boolean     boIsCorrect;
    private boolean boIsStopped;
    private String sValue;


       @Override
   public String getMessage() {
      return sValue;
   }

   @Override
   public void setMessage(String sValue) {
      this.sValue = sValue;
   }
    
    public void setPrintScreenPath(String sPrintScreenPath) {
        this.sPrintScreenPath = sPrintScreenPath;
    }

    public Date getDatePrint() {
        return oDatePrint;
    }

    public void setDatePrint(Date oDatePrint) {
        this.oDatePrint = oDatePrint;
    }

    public String getPathSnap() {
        return sPathSnap;
    }

    public void setPathSnap(String sPathSnap) {
        this.sPathSnap = sPathSnap;
    }

    public String getSnapName() {
        return sSnapName;
    }

    public void setSnapName(String sSnapName) {
        this.sSnapName = sSnapName;
    }

    @Override
    public Object getResultTask() {
        return this;
    }

    @Override
    public boolean isCorrect() {
         return boIsCorrect;
    }

    @Override
    public Map<String, Object> getResultContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPrintScreenPath() {
        return sPrintScreenPath;
    }

    @Override
    public boolean isStopped() {
        return boIsStopped;
    }

    @Override
    public void setIsStopped(boolean isStopped) {
        boIsStopped= isStopped;
    }

    @Override
    public void setIsCorrect(boolean boIsCorrect) {
        this.boIsCorrect = boIsCorrect;
    }

    @Override
    public void setDiagnostic(DiagnosticDTO oDiagnosticDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DiagnosticDTO getDiagnostic() {
        return null;
    }

   @Override
   public String getClassName() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setClassName(String sClassName) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public CheckConditionDTO getCondition() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setCondition(CheckConditionDTO oCondition) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
