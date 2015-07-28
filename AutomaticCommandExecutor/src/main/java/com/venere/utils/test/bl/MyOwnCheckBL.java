/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.utils.test.bl;

import com.venere.ace.dtos.ATaskResultDTO;
import com.venere.ace.exception.EHandlerException;
import com.venere.ace.idtos.ITestRequestDTO;
import com.venere.ace.interfaces.ICommandResult;
import junit.framework.Assert;
import org.openqa.selenium.By;

/**
 *
 * @author cquatrini
 */
public class MyOwnCheckBL extends AClassTestBL{

   public MyOwnCheckBL() {
      super("MyOwnCheckBL");
   }
   
    private String sTitleElement        = "//title";

    public ICommandResult myCheck(ITestRequestDTO oTest) throws   EHandlerException{
      ATaskResultDTO oResult = new ATaskResultDTO("MyOwnTaskResult");
      oResult.setIsCorrect(false);
      String sElementPageToCheck = (String) oTest.getInputDataList().get(0);

      String sText = oBrowser.findElement(By.xpath(sTitleElement)).getText();
      Assert.assertEquals(sText, "pippo");

       oLogger.error("checkTitle : "+oResult.isCorrect());
      oResult.setMessage("SEO Check Done");
      return oResult;
    }
    
}