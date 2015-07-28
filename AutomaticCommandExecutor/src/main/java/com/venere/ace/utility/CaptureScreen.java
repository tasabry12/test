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
package com.venere.ace.utility;

/**
 *
 * @author quatrini
 */

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

public class CaptureScreen  {


   protected Log oLogger        = LogFactory.getLog(getClass());

   Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
   Robot myRobot;
   BufferedImage screenImage;
   protected WebDriver    oBrowser;
   
   public CaptureScreen() {
     
   }
   
   public static void main(String[] args) {
      CaptureScreen cs = new CaptureScreen();

   }

   private BufferedImage getPrintScreenImage(){

      try {
         myRobot = new Robot();
      } catch (Exception exception) {
      }
      screenImage = myRobot.createScreenCapture(screenRectangle);

      return screenImage;
   }

   /**
    * Get PrintScreen Image and save in sPathToSave
    * 
    * @param sPathToSave where image is saved
    * 
    * @return boolean result of operation
    */
   public boolean saveImagePrintScreen(String sPathToSave){
      boolean boResult = false;
      File outputfile = new File(sPathToSave);
      try {
         boResult = ImageIO.write(getPrintScreenImage(), "png", outputfile);
      } catch (IOException ex) {
         oLogger.error("Error While Saving PrintScreen  ", ex);
      }catch (RuntimeException ex) {
         oLogger.error(" While Saving PrintScreen  ", ex);
      }

      return boResult;
   }
   
    /**
     * Make a Shoot on WebElement Element 
     * 
     * @param element that will shooted
     * @param oBrowser active browser that hold the alement
     * 
     * @return File Image with element 
     * 
     * @throws IOException 
     */ 
    public File shootWebElement(WebElement element, WebDriver oBrowser) throws IOException  {

        File screen = ((TakesScreenshot) oBrowser).getScreenshotAs(OutputType.FILE);

        Point p = element.getLocation();

        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        Rectangle rect = new Rectangle(width, height);

        BufferedImage img = ImageIO.read(screen);

        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width,   
                                 rect.height);

        ImageIO.write(dest, "png", screen);
        return screen ;
    }
    
    /**
     * Get a Partial Image from active page Browser, cutted with the Screen Size
     * 
     * @param oBrowser active browser
     * 
     * @return  File IMage with Full Page shooted
     */
    public File shootWindowView(WebDriver oBrowser){
        
        File oScreenShotFile        = shootFullScreen(oBrowser);
        Rectangle oScreenSizeWindow = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Point oPositionWindows      = oBrowser.manage().window().getPosition();
        int width                   = oScreenSizeWindow.width;
        int height                  = oScreenSizeWindow.height;
       
        BufferedImage oFullScreenImage;
        try {

            oFullScreenImage = ImageIO.read(oScreenShotFile);
            int iMinWidth    = (width  < oFullScreenImage.getWidth() ) ? width : oFullScreenImage.getWidth() ;        
            int iMinHeight   = (height < oFullScreenImage.getHeight()) ? width : oFullScreenImage.getHeight();
              
            Rectangle rect = new Rectangle(iMinWidth, iMinHeight);
            BufferedImage oPartialScreenImage = oFullScreenImage.getSubimage(oPositionWindows.getX(), oPositionWindows.getY(), rect.width,   
                                 rect.height);
            
            ImageIO.write(oPartialScreenImage, "png", oScreenShotFile);
        } catch (IOException ex) {
            Logger.getLogger(CaptureScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return oScreenShotFile;
    }
    
   /**
    * Get Full Page Shoot from active page browser
    *
    * @param oBrowser
    *
    * @return File that hold image
    */
   public File shootFullScreen(WebDriver oBrowser) {
      TakesScreenshot oScreenshotBrowser;
      if (oBrowser instanceof TakesScreenshot) {
         oScreenshotBrowser = (TakesScreenshot) oBrowser;
      } else {
         //should be a RemoteWebDriver then try to augment the browser         
         oScreenshotBrowser = (TakesScreenshot) new Augmenter().augment(oBrowser);
      }
      return (oScreenshotBrowser).getScreenshotAs(OutputType.FILE);
   }
}
