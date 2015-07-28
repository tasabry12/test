/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.ace.interfaces;

import com.venere.ace.exception.EHandlerException;
import com.venere.utils.dto.DiagnosticDTO;

/**
 *
 * @author fcastaldi
 */
public interface IDiagnostic {
    
      public DiagnosticDTO performDiagnosis(ICommandExecutor oExecutor) throws EHandlerException ;
    
}
