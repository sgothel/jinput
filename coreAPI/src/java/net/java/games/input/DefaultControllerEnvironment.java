/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.java.games.util.plugins.*;

/**
 * The default controller environment.
 *
 * @version %I% %G%
 * @author Michael Martak
 */
class DefaultControllerEnvironment extends ControllerEnvironment {
	static String libPath;
	
	private static Logger log = Logger.getLogger(DefaultControllerEnvironment.class.getName());
	
	/**
	 * Static utility method for loading native libraries.
	 * It will try to load from either the path given by
	 * the net.java.games.input.librarypath property
	 * or through System.loadLibrary().
	 * 
	 */
	static void loadLibrary(final String lib_name) {
		AccessController.doPrivileged(
				new PrivilegedAction() {
					public final Object run() {
						String lib_path = System.getProperty("net.java.games.input.librarypath");
						if (lib_path != null)
							System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
						else
							System.loadLibrary(lib_name);
						return null;
					}
				});
	}
    
	static String getPrivilegedProperty(final String property) {
	       return (String)AccessController.doPrivileged(new PrivilegedAction() {
	                public Object run() {
	                    return System.getProperty(property);
	                }
	            });
		}
		

	static String getPrivilegedProperty(final String property, final String default_value) {
       return (String)AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    return System.getProperty(property, default_value);
                }
            });
	}
		
    /**
     * List of all controllers in this environment
     */
    private ArrayList<Controller> controllers;
    
	private Collection loadedPlugins = new ArrayList();
    private ArrayList<ControllerEnvironment> environments = new ArrayList<ControllerEnvironment>();

    /**
     * Public no-arg constructor.
     */
    public DefaultControllerEnvironment() {
    }
    
    /**
     * Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     */
    public Controller[] getControllers() {
        if (controllers == null) {
            // Controller list has not been scanned.
            controllers = new ArrayList<Controller>();
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    scanControllers();
                    return null;
                }
            });
            //Check the properties for specified controller classes
            String pluginClasses = getPrivilegedProperty("jinput.plugins", "") + " " + getPrivilegedProperty("net.java.games.input.plugins", "");
			if(!getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
				String osName = getPrivilegedProperty("os.name", "").trim();
				if(osName.equals("Linux")) {
					pluginClasses = pluginClasses + " net.java.games.input.LinuxEnvironmentPlugin";
				} else if(osName.equals("Mac OS X")) {
					pluginClasses = pluginClasses + " net.java.games.input.OSXEnvironmentPlugin";
				} else  if(osName.equals("Windows XP") || osName.equals("Windows Vista") || osName.equals("Windows 7")) {
					pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
				} else if(osName.equals("Windows 98") || osName.equals("Windows 2000")) {
					pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
				} else if (osName.startsWith("Windows")) {
					log.warning("Found unknown Windows version: " + osName);
					log.warning("Attempting to use default windows plug-in.");
					pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
				} else {
					log.warning("Trying to use default plugin, OS name " + osName +" not recognised");
				}
			}

			StringTokenizer pluginClassTok = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
			while(pluginClassTok.hasMoreTokens()) {
				String className = pluginClassTok.nextToken();					
				try {
					if(!loadedPlugins.contains(className)) {
						log.fine("Loading: " + className);
						Class ceClass = Class.forName(className);						
						ControllerEnvironment ce = (ControllerEnvironment) ceClass.newInstance();
						if(ce.isSupported()) {
							environments.add(ce);
							addControllers(ce.getControllers());
							loadedPlugins.add(ce.getClass().getName());
						} else {
							logln(ceClass.getName() + " is not supported");
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
        }
	if(!environments.isEmpty()){
	    Controller[] newScanControllers = environments.get(0).getControllers();
            Controller[] ret = new Controller[newScanControllers.length];
	    for(int i = 0; i < newScanControllers.length; i++){
                ret[i] = newScanControllers[i];
            }
	    return ret;
	}

        Controller[] ret = new Controller[controllers.size()];
        Iterator it = controllers.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = (Controller)it.next();
            i++;
        }
        return ret;
    }

    /**
     * Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     */
    public Controller[] rescanControllers() {
	if(!environments.isEmpty()){
	    Controller[] newScanControllers = environments.get(0).rescanControllers();
	    // need to add controllers that were connected
	    for(int i = 0; i < newScanControllers.length; i++){
		boolean controllerExist = false;
		for(Controller controller:controllers){
		    if(newScanControllers[i] == controller){
			controllerExist = true;
			break;
		    }
		}
		if(!controllerExist){
		    controllers.add(newScanControllers[i]);
		}
	    }
	    ArrayList<Controller> removeControllers = new ArrayList<Controller>();
	    // need to remove controllers that have disconnected
	    for(Controller controller:controllers){
		boolean controllerExist = false;
		for(int i = 0; i < newScanControllers.length; i++){
		    if(controller == newScanControllers[i]){
			controllerExist = true;
			break;
		    }
		}
		if(!controllerExist){
		    //controllers.remove(controller);
		    removeControllers.add(controller);
		}
	    }
	    for(Controller controller: removeControllers){
		controllers.remove(controller);
	    }
	}

        Controller[] ret = new Controller[controllers.size()];
        Iterator it = controllers.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = (Controller)it.next();
            i++;
        }
        return ret;
    }
    
    /* This is jeff's new plugin code using Jeff's Plugin manager */
    private void scanControllers() {
        String pluginPathName = getPrivilegedProperty("jinput.controllerPluginPath");
        if(pluginPathName == null) {
            pluginPathName = "controller";
        }
        
        scanControllersAt(getPrivilegedProperty("java.home") +
            File.separator + "lib"+File.separator + pluginPathName);
        scanControllersAt(getPrivilegedProperty("user.dir")+
            File.separator + pluginPathName);
    }
    
    private void scanControllersAt(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            Plugins plugins = new Plugins(file);
            Class[] envClasses = plugins.getExtends(ControllerEnvironment.class);
            for(int i=0;i<envClasses.length;i++){
                try {
					ControllerEnvironment.logln("ControllerEnvironment "+
                            envClasses[i].getName()
                            +" loaded by "+envClasses[i].getClassLoader());
                    ControllerEnvironment ce = (ControllerEnvironment)
                    	envClasses[i].newInstance();
					if(ce.isSupported()) {
	                    addControllers(ce.getControllers());
						loadedPlugins.add(ce.getClass().getName());
					} else {
						logln(envClasses[i].getName() + " is not supported");
					}
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * Add the array of controllers to our list of controllers.
     */
    private void addControllers(Controller[] c) {
        for (int i = 0; i < c.length; i++) {
            controllers.add(c[i]);
        }
    }

	public boolean isSupported() {
		return true;
	}
}
