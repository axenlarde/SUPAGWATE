package com.alex.supagwate.ftp;


import java.util.ArrayList;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import com.alex.supagwate.utils.UsefulMethod;

public class FtpTools
	{
	
	
	public static DefaultFtpServer startFTPServer() throws Exception
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
		    
		    ArrayList<Authority> authorities = new ArrayList<Authority>();
		    authorities.add(new WritePermission());
		    user.setAuthorities(authorities);
		   
		    userManager.save(user);
			serverFactory.setUserManager(userManager);
		    
			//start the server
			DefaultFtpServer server = (DefaultFtpServer) serverFactory.createServer();
			server.start();
			return server;
			}
		catch (Exception e)
			{
			throw new Exception("ERROR while starting the FTP server : "+e.getMessage(),e);
			}
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
