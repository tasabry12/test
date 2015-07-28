/*                   Copyright (c) 2013 Venere Net S.p.A.
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

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author fcastaldi
 */
public class WebDriverUtil {

   private Log oLogger = LogFactory.getLog(getClass());
   protected WebDriver oBrowser;
   protected String sRemoteNodeUrlSession;

//   public WebDriverUtil(WebDriver oBrowser) {
//      this.oBrowser = oBrowser;
//   }

   public void setDriver(WebDriver oBrowser) {
      this.oBrowser = oBrowser;
      printHostNode();
   }
   
   public void setRemoteNodeUrlSession(String sRemoteNodeUrlSession) {
      this.sRemoteNodeUrlSession = sRemoteNodeUrlSession;      
   }
   
   

   public void waitFor(int iMillisecond) {
      if (iMillisecond > 0) {
         oLogger.debug("Wait for " + iMillisecond + " milliseconds");
         try {
            Thread.sleep(iMillisecond);
         } catch (InterruptedException ex) {
            oLogger.error("WAIT ERROR " + ex);
         }
      }
   }

   public void printHostNode(){
      oLogger.info("NODE IP: "+getHostNode());
   }
   
   public String getHostNode() {
//      try {
//
////         URL url = new URL("http://quickbuild.dev.venere.it:4444/grid/api/testsession?session=" + ((RemoteWebDriver) oBrowser).getSessionId().toString());
////         BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", url.toExternalForm());
////         DefaultHttpClient client = new DefaultHttpClient();
////         HttpHost host = new HttpHost(url.getHost(), url.getPort());
////         org.apache.http.HttpResponse response = client.execute(host, request);
////         JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
////         URL nodeUrl = new URL(object.getString("proxyId"));
////         return nodeUrl.getHost()+":"+nodeUrl.getPort();
////         
//      } catch (Exception e) {
//         return "Unable to obtain hostname of webdriver node";
//      }
       return null;
   }
}
