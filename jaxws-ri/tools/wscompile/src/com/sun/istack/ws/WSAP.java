/**
 * $Id: WSAP.java,v 1.6 2005-08-24 15:52:21 kohlert Exp $
 *
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.istack.ws;

import com.sun.tools.ws.processor.Processor;
import com.sun.tools.ws.processor.ProcessorConstants;
import com.sun.tools.ws.processor.ProcessorOptions;
import com.sun.tools.ws.processor.config.Configuration;
import com.sun.tools.ws.processor.model.Model;
import com.sun.tools.ws.processor.modeler.annotation.*;
import com.sun.tools.ws.processor.util.ProcessorEnvironment;
import com.sun.xml.ws.util.localization.Localizable;
import com.sun.xml.ws.util.VersionUtil;

import java.util.*;

import javax.jws.*; 



/**
 * This is the entry point for the WebServiceAP when APT is invoked on a SEI
 * annotated with the javax.jws.WebService annotation.
 *
 * @author WS Development Team
 */
public class WSAP extends WebServiceAP {
             
    public WSAP(AnnotationProcessorContext context) {         
        super(null, (ProcessorEnvironment)null, null, context);    
    }

    protected WebServiceVisitor createWrapperGenerator() {
        return new WebServiceWrapperGenerator(this, context);    
    }


    protected boolean shouldProcessWebService(WebService webService) {
        return webService != null;
    }    
    
    protected void runProcessorActions(Model model) throws Exception {
        Map<String, String> options = apEnv.getOptions();
        String classDir = options.get("-d");
        if (classDir == null)
            classDir = ".";
        String srcDir = options.get("-s");    
        Properties properties = new Properties();
        if (srcDir == null)
            srcDir = classDir;
        properties.setProperty(ProcessorOptions.SOURCE_DIRECTORY_PROPERTY, srcDir);
        properties.setProperty(ProcessorConstants.JAXWS_VERSION,
                                getVersionString());
        properties.setProperty(ProcessorOptions.JAXWS_SOURCE_VERSION,
            VersionUtil.JAXWS_VERSION_DEFAULT);
        properties.setProperty(ProcessorOptions.DESTINATION_DIRECTORY_PROPERTY, classDir);
        String ndDir = classDir;
        
        properties.setProperty(
            ProcessorOptions.NONCLASS_DESTINATION_DIRECTORY_PROPERTY,
            ndDir);               
        Configuration config = new Configuration(env);
        
        Processor processor = new Processor(config, properties, model);
        registerGenerators(processor);
        
        processor.runActions(); 
    }
    
    private void registerGenerators(Processor processor) {
    }    
    
    public void onError(Localizable msg) {
        String message;
        if (messager != null) {
            message = localizer.localize(msg);
            messager.printError(localizer.localize(msg));     
            throw new RuntimeException(message);
        } else {
            message = localizer.localize(getMessage("webserviceap.error", localizer.localize(msg)));            
            report(message);
            throw new RuntimeException(message);        
        }
    }         
}
