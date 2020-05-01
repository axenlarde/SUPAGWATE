package com.alex.supagwate.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;

import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;

public class FtpTools
	{
	
	
	public static void startFTPServer()
		{
		try
			{
			FtpServerFactory serverFactory = new FtpServerFactory();
			
			//set the port of the listener
			ListenerFactory lf = new ListenerFactory();
			lf.setPort(Integer.parseInt(UsefulMethod.getTargetOption("ftpport")));
			Listener listener = lf.createListener();
			serverFactory.addListener("default", listener);
			
			//Set the user
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		    UserManager userManager = userManagerFactory.createUserManager();
		    BaseUser user = new BaseUser();
		    user.setName(UsefulMethod.getTargetOption("ftpuser"));
		    user.setPassword(UsefulMethod.getTargetOption("ftppassword"));
		    user.setHomeDirectory(UsefulMethod.getTargetOption("ftpdirectory"));
		    userManager.save(user);
			serverFactory.setUserManager(userManager);
		    
			//start the server
			FtpServer server = serverFactory.createServer();
			server.start();
			Variables.setFtpServer(server);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while starting the FTP server : "+e.getMessage(),e);
			}
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
