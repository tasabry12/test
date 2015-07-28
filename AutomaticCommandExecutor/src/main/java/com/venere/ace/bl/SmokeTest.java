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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venere.ace.bl;

import com.venere.ace.abstracts.AHandler;
import com.venere.ace.abstracts.ATest;
import com.venere.ace.exception.EExecutorException;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.ICommandResult;
import com.venere.ace.interfaces.IDiagnostic;
import com.venere.ace.utility.IEnum;

/**
 *
 * @author quatrini
 */
public class SmokeTest extends ATest{

   @Override
   public ICommandResult doTask() throws EExecutorException {
      oLogger.error("IL PRIMO TEST FOO BAR");
      return null;
   }

   @Override
   public void validate() throws EExecutorException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public ICommandResult checkPreCondition() throws EExecutorException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public ICommandResult checkPostCondition() throws EExecutorException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setPostTask(ICommandExecutor oPostAction) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setPreTask(ICommandExecutor oPostAction) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setHandler(Object oBrowser) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setRequest(Object oRequest) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setTaskId(IEnum oTaskName) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setClassName(String sClassName) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String getClassName() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String getTaskDescription() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setTaskDescription(String sTaskDescription) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public ICommandResult terminate() throws EExecutorException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public AHandler getHandler() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public IDiagnostic getDiagnostic() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void setDiagnostic(IDiagnostic oDiagnostic) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Object getRequest() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public boolean evaluateCondition() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
