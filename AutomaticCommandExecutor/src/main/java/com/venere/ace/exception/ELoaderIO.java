/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venere.ace.exception;

/**
 *
 * @author quatrini
 */
public class ELoaderIO extends RuntimeException {

    /**
     * Creates a new instance of <code>ELoaderIO</code> without detail message.
     */
    public ELoaderIO() {
    }
 public static enum LoaderErrorCode
   {
      SAX_ERROR,
      IO_ERROR,
      TASK_UNKNOWN
   };


   /** persistency layer exception error code */
   private LoaderErrorCode oErrorCode;


    /**
     * Constructs an instance of <code>ELoaderIO</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ELoaderIO(String msg) {
        super(msg);
    }

   public ELoaderIO(String sMessage, Throwable eCause)
   {

       super(sMessage, eCause);
       this.oErrorCode = LoaderErrorCode.TASK_UNKNOWN;

   }
   
   public ELoaderIO(LoaderErrorCode oErrorLoaderCode, String sMessage, Throwable eCause)
   {

       super( sMessage, eCause);
       this.oErrorCode = oErrorLoaderCode;

   }
   
     /**
    * get persistency layer error code
    * @return	BLErrorCode	get persistency layer error code
    */
   public LoaderErrorCode getErrorCode()
   {
      return oErrorCode;
   }

   /**
    * set persistency layer error code
    * @param oErrorCode		persistency layer error code
    */
   public void setErrorCode(LoaderErrorCode oErrorCode)
   {
      this.oErrorCode = oErrorCode;
   }



}
