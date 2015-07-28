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
 
package com.venere.ace.abstracts;

import com.venere.ace.bl.placeholders.IInternalPlaceholder;
import com.venere.ace.bl.placeholders.InternalPlaceholderFactory;
import com.venere.ace.bl.placeholders.InternalPlaceholderIdentifier;
import com.venere.ace.exception.ELoaderIO;
import com.venere.ace.idtos.IConfigurationDTO;
import com.venere.ace.interfaces.ICommandExecutor;
import com.venere.ace.interfaces.ICommandLoader;
import com.venere.ace.interfaces.IPointCutExecutor;
import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author quatrini
 */
public abstract class ALoader 
                  extends DefaultHandler
                  implements ICommandLoader{
   
   final static String DEFAULT_VALUE_SEPARATOR = "|=";
   
   protected List<ICommandExecutor> oCommandExecutor;
   protected CharArrayWriter        contents = new CharArrayWriter();
   protected Properties             oCurrentProperties = new Properties();
   protected Properties             oDefaultProperties = new Properties();
   protected Log oLogger            = LogFactory.getLog(getClass());
   
   protected Map<String,List<IPointCutExecutor>> oMapPointCut;

   @Override
   public List<ICommandExecutor> load(IConfigurationDTO oConfigurationDTO) throws ELoaderIO {


      oCommandExecutor  = new ArrayList<>();

      try {

         XMLReader xr = XMLReaderFactory.createXMLReader();

         xr.setContentHandler(this);

         InputSource oInputSource = new InputSource(oConfigurationDTO.getPathFileToLoad());

         try {
            xr.parse(oInputSource);
         } catch (IOException | SAXException ex) {
            oLogger.error(ex);
            throw new ELoaderIO("Error while parsing the XML File", ex);
         }
      } catch (SAXException ex) {
         oLogger.error(ex);
         throw new ELoaderIO(ELoaderIO.LoaderErrorCode.SAX_ERROR, "Error while creating the context for file", ex);

      } 
      return oCommandExecutor;
   }
   
   @Override
   public Map<String,List<IPointCutExecutor>> getoPointCutMap() throws ELoaderIO
   {
      return oMapPointCut;
      
   }
   
   
   
   @Override
   public void characters(char[] ch, int start, int len) {
      try {
         contents.write(new String(ch, start, len));
      } catch (IOException ex) {
         oLogger.error("Error while writing contents into parser");
         throw new RuntimeException("Error while parsing the XML File", ex);
      }
   }

   protected Properties addPropertiesFiles(String sPropertiesFilePath) {
      
      Properties oEnvironmentProperties = new Properties();
      String sValuePropertiesFilePath = getValue(sPropertiesFilePath);
      if(sValuePropertiesFilePath!=null && !sValuePropertiesFilePath.isEmpty()){
         InputStream in = null ;
         try {
            in = (InputStream) this.getClass().getResourceAsStream(sValuePropertiesFilePath);
            if(in == null ){

               in = (InputStream) new FileInputStream (sValuePropertiesFilePath);
            }
         } catch (Exception ex) {
            oLogger.error("Error While Getting properties from file properties "+sValuePropertiesFilePath);
            throw new ELoaderIO("Error While Getting properties from file properties ", ex);

         }
         try {
            oEnvironmentProperties.load(in);
         } catch (Exception ex) {
              oLogger.error("Error While Loading properties from file properties "+sValuePropertiesFilePath);
              throw new ELoaderIO("Error While Getting propoerties from file properties ", ex);

         }
         try {
            in.close();
         } catch (IOException ex) {
            oLogger.error("Error While Closing properties from file properties "+sValuePropertiesFilePath);
            throw new ELoaderIO("Error While Getting propoerties from file properties ", ex);
         }
      }

      return oEnvironmentProperties;
   }


   protected String getValue(String sValue) {

     while (sValue !=null  && sValue.contains("${")) {
         String[] sSplittedword = sValue.split("\\$\\{");
         sSplittedword = sSplittedword[1].split("}");
         String sKeyAndDefault = sSplittedword[0];
         
         String[] asKeyAndDefault = extractKeyAndDefault(sKeyAndDefault);
         
         String sKey = asKeyAndDefault[0];
         String sDefault = asKeyAndDefault[1];
         
         String sValueProperties;
         if(InternalPlaceholderIdentifier.isInternalPlaceholder(sKey)) {
            IInternalPlaceholder oInternalPlaceholder = InternalPlaceholderFactory.getInstance(sKey);
            sValueProperties = oInternalPlaceholder.resolve();
         } else {
            sValueProperties = oCurrentProperties.getProperty(sKey);
            if(sValueProperties == null || sValueProperties.isEmpty()){
               sValueProperties = oDefaultProperties.getProperty(sKey);
               if(sValueProperties == null || sValueProperties.isEmpty()){
                  sValueProperties = System.getProperty(sKey);
               }
            }
         }
         
         if(sValueProperties == null || sValueProperties.isEmpty()){
            if(sDefault != null) {
               sValueProperties = sDefault;
               oLogger.info("No value specified for key: ${" + sKey + "}, using default value: [" + sDefault + "]");
            } else {
               sValueProperties = "";
               oLogger.warn("No value found for key: ${" + sKey + "}, will set value to \"\"");
            }
         }
         
         sValue = sValue.replace("${" + sKeyAndDefault + "}", sValueProperties.trim());
      }
     
      return sValue;
   }

   private String[] extractKeyAndDefault(String sKey) {
      String[] asKeyAndDefault = {sKey, null};
      
      int iDefaultIdx = sKey.indexOf(DEFAULT_VALUE_SEPARATOR);
      if(iDefaultIdx > -1) {
         asKeyAndDefault[0] = sKey.substring(0, iDefaultIdx);
         asKeyAndDefault[1] = sKey.substring(iDefaultIdx + DEFAULT_VALUE_SEPARATOR.length());         
      }
      
      return asKeyAndDefault;
   }
   
   protected Properties mergeWithCurrentProps(Properties oTempProps){
      Properties oCurrentPropsCopy = (Properties) oCurrentProperties.clone();
      Set oEntryProps = oTempProps.entrySet();
      for (Iterator oIt = oEntryProps.iterator(); oIt.hasNext();) {
         Entry oEntry = (Entry) oIt.next();
         oCurrentPropsCopy.setProperty((String) oEntry.getKey(), (String) oEntry.getValue());
      }
      return oCurrentPropsCopy;
   }
}
