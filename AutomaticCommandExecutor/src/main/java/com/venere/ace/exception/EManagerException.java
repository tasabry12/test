/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venere.ace.exception;

import com.venere.ace.abstracts.AException;

/**
 *
 * @author quatrini
 */
public class EManagerException extends AException {

    /**
     * Creates a new instance of <code>ICommandException</code> without detail message.
     */
    public EManagerException() {
    }


    /**
     * Constructs an instance of <code>ICommandException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EManagerException(String msg) {
        super(msg);
    }


      /** enumeration of all persistency layer exceptions error codes */
   public static enum ManagerErrorCode
   {
      COMMAND_EXECUTION_ERROR,
      LOADER_EXECUTION_ERROR,
   };


   /** persistency layer exception error code */
   private ManagerErrorCode oErrorCode;

   /**
    * constructor
    * @param oErrorCode		persistency layer error code
    * @param sMessage		persistency layer error message
    * @param eCause		persistency layer exception
    */
   public EManagerException(ManagerErrorCode oErrorCode, String sMessage, Throwable eCause)
   {
      super(sMessage, eCause);
      this.oErrorCode = oErrorCode;
   }

   /**
    * get persistency layer error code
    * @return	BLErrorCode	get persistency layer error code
    */
   public ManagerErrorCode getErrorCode()
   {
      return oErrorCode;
   }

   /**
    * set persistency layer error code
    * @param oErrorCode		persistency layer error code
    */
   public void setErrorCode(ManagerErrorCode oErrorCode)
   {
      this.oErrorCode = oErrorCode;
   }

   public EManagerException(String sMessage, Throwable eCause) {
      super(sMessage, eCause);
   }

}
