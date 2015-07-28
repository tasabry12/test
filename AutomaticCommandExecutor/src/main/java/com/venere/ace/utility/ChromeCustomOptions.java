/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venere.ace.utility;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author cquatrini
 */
public class ChromeCustomOptions extends ChromeOptions{

    public ChromeCustomOptions() {
       super();  
//     this.addArguments("--ignore-certificate-errors,--disable-popup-blocking,--disable-translate");
    }
    
    public void setEnv (String sEnvironmentValues)
    {
       this.addArguments(sEnvironmentValues);
    }
   
    
}
