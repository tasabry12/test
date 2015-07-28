/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.ace.utility;

import java.util.Arrays;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author cquatrini
 */
public class DesireCustomCapabilities extends DesiredCapabilities{

    public DesireCustomCapabilities() {
         super();        
    }
    
    public DesireCustomCapabilities(String broserType,String version, Platform platform) {
       super(broserType, version, platform);
    }
    
    public void setEnv(String sValue)
    {
        super.setCapability("chrome.switches", Arrays.asList(sValue.split(",")));
    }
    
    public void setSingleCapability(String sKeyValue){  
 
       String[] oSplit = sKeyValue.split("=");
       String sKey = oSplit[0];
       String sValue = oSplit[1];
       boolean isBoolean = false;
       if(sValue.equals("true")||sValue.equals("false")){
          isBoolean = true;       
       }
       super.setCapability(sKey,isBoolean?Boolean.valueOf(sValue).booleanValue():sValue);
    }
    
}
