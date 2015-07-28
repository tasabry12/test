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
public class EHandlerException extends AException {

    /**
     * Creates a new instance of <code>ICommandException</code> without detail message.
     */
    public EHandlerException() {

    }

    /**
     * Constructs an instance of <code>ICommandException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EHandlerException(String msg) {
        super(msg);
        oResultTask.setIsCorrect(false);
        oResultTask.setMessage(msg);
    }


      /** enumeration of all persistency layer exceptions error codes */
   public static enum HandlerErrorCode
   {
      ACTION_TASK,
      SCENARIO_TASK,
      TESTPLAN_TASK,
      BAD_RESULT,
      WRONG_LOCATOR

      
   };


   /** persistency layer exception error code */
   private HandlerErrorCode oErrorCode;

   /**
    * constructor
    * @param oErrorCode		persistency layer error code
    * @param sMessage		persistency layer error message
    * @param eCause		persistency layer exception
    */
   public EHandlerException(HandlerErrorCode oErrorCode, String sMessage, Throwable eCause)
   {
      super(sMessage,eCause);
      this.oErrorCode = oErrorCode;
      oResultTask.setIsCorrect(false);
      oResultTask.setMessage(sMessage);
   }

   /**
    * constructor
    * @param oErrorCode		persistency layer error code
    * @param sMessage		persistency layer error message
    */
   public EHandlerException(HandlerErrorCode oErrorCode, String sMessage)
   {
      this(oErrorCode, sMessage, null);
   }
   
   /**
    * get persistency layer error code
    * @return	BLErrorCode	get persistency layer error code
    */
   public HandlerErrorCode getErrorCode()
   {
      return oErrorCode;
   }

   /**
    * set persistency layer error code
    * @param oErrorCode		persistency layer error code
    */
   public void setErrorCode(HandlerErrorCode oErrorCode)
   {
      this.oErrorCode = oErrorCode;
   }

}
