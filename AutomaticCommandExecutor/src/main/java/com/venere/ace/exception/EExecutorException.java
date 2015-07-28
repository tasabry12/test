package com.venere.ace.exception;

import com.venere.ace.abstracts.AException;

/**
 *
 * @author quatrini
 */
public class EExecutorException extends AException {

    /**
     * Creates a new instance of <code>ICommandException</code> without detail message.
     */
    public EExecutorException() {
    }


    /**
     * Constructs an instance of <code>ICommandException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EExecutorException(String msg) {
        super(msg);
    }


      /** enumeration of all persistency layer exceptions error codes */
   public static enum ExecutorErrorCode
   {
      EXECUTION_FAIL,
      EXECUTION_TEST
      
   };


   /** persistency layer exception error code */
   private ExecutorErrorCode oErrorCode;

   /**
    * constructor
    * @param oErrorCode		persistency layer error code
    * @param sMessage		persistency layer error message
    * @param eCause		persistency layer exception
    */
   public EExecutorException(ExecutorErrorCode oErrorCode, String sMessage, Throwable eCause)
   {
      super(sMessage, eCause);
      this.oErrorCode = oErrorCode;
   }

   /**
    * get persistency layer error code
    * @return	BLErrorCode	get persistency layer error code
    */
   public ExecutorErrorCode getErrorCode()
   {
      return oErrorCode;
   }

   /**
    * set persistency layer error code
    * @param oErrorCode		persistency layer error code
    */
   public void setErrorCode(ExecutorErrorCode oErrorCode)
   {
      this.oErrorCode = oErrorCode;
   }

   public EExecutorException(String sMessage, Throwable eCause) {
      super(sMessage, eCause);
   }












}
