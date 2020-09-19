package com.alex.supagwate.main;


import java.io.File;

import org.apache.log4j.Level;

import com.alex.supagwate.device.Device;
import com.alex.supagwate.device.DeviceType;
import com.alex.supagwate.ftp.FTPTransfer;
import com.alex.supagwate.gui.MainWindow;
import com.alex.supagwate.utils.InitLogging;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;

/**
 * Perceler main class
 *
 * @author Alexandre RATEL
 */
public class Main
	{
	
	public Main()
		{
		//Set the software name
		Variables.setSoftwareName("SUPAGWATE");
		//Set the software version
		Variables.setSoftwareVersion("1.0");
		
		/****************
		 * Initialization of the logging
		 */
		Variables.setLogger(InitLogging.init(Variables.getSoftwareName()+"_LogFile.txt"));
		Variables.getLogger().info("\r\n");
		Variables.getLogger().info("#### Entering application");
		Variables.getLogger().info("## Welcome to : "+Variables.getSoftwareName()+" version "+Variables.getSoftwareVersion());
		Variables.getLogger().info("## Author : RATEL Alexandre : 2020");
		/*******/
		
		/******
		 * Initialization of the variables
		 */
		new Variables();
		/************/
		
		/**********
		 * We check if the java version is compatible
		 */
		UsefulMethod.checkJavaVersion();
		/***************/
		
		/**********************
		 * Reading of the configuration files
		 */
		try
			{
			/**
			 * Config file reading
			 */
			Variables.setTabConfig(UsefulMethod.readMainConfigFile(Variables.getConfigFileName()));
			
			/**
			 * Database file reading
			 */
			UsefulMethod.initDatabase();
			}
		catch(Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/********************/
		
		/*****************
		 * Setting of the inside variables from what we read in the configuration file
		 */
		try
			{
			UsefulMethod.initInternalVariables();
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(exc.getMessage());
			Variables.getLogger().setLevel(Level.INFO);
			}
		/*********************/
		
		/****************
		 * Init email server
		 */
		try
			{
			UsefulMethod.initEMailServer();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			Variables.getLogger().error("Failed to init the eMail server : "+e.getMessage());
			}
		/*************/
		
		/*******************
		 * Start main user interface
		 */
		try
			{
			Variables.getLogger().info("Launching Main Window");
			Variables.setMainWindow(new MainWindow());
			}
		catch (Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/******************/
		
		//End of the main class
		}
	
	
	

	public static void main(String[] args)
		{
		new Main();
		}
	/*2020*//*RATEL Alexandre 8)*/
	}
