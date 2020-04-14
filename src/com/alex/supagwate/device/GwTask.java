package com.alex.supagwate.device;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.alex.supagwate.action.Task;
import com.alex.supagwate.gui.ProgressUpdater;
import com.alex.supagwate.gui.StatusWindow;
import com.alex.supagwate.gui.WaitingWindow;
import com.alex.supagwate.misc.CollectionFileChecker;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;

/**********************************
 * Class used to create the gateway cli process
 * 
 * @author RATEL Alexandre
 **********************************/
public class GwTask extends Thread
	{
	/**
	 * Variables
	 */
	private ArrayList<MainItem> itemToProcessList;
	private actionType action;
	
	/****
	 * Constructor
	 */
	public GwTask(actionType action)
		{
		this.action = action;
		itemToProcessList = new ArrayList<MainItem>();
		
		start();
		}
	
	/******
	 * Global Method used to inject the items
	 */
	public void run()
		{
		/*******
		 * Splash window
		 * Used to make the user wait
		 */
		WaitingWindow myWW = new WaitingWindow(LanguageManagement.getString("pleasewait"));
		/**************/
		
		try
			{
			/***************
			 * Init
			 */
			/**
			 * Collection file checking
			 */
			CollectionFileChecker.checkForCliSending();
			
			myWW.getAvancement().setText(" "+LanguageManagement.getString("itemlistbuilding"));
			
			/**
			 * We build the list of gateway which is a list of main items
			 */
			itemToProcessList = GwTools.setGwList(action, myWW);
			
			/**
			 * Pinging the devices
			 */
			Variables.getLogger().info("Pinging devices");
			PingManager myPM = new PingManager();
			
			for(MainItem mi : itemToProcessList)
				{
				for(ItemToProcess itp : mi.getAssociatedItems())
					{
					myPM.getPingList().add(new PingProcess((Device)itp));
					}
				}
			
			myWW.getAvancement().setText(" "+LanguageManagement.getString("pingingdevices"));
			myPM.start();
			
			/**
			 * We need to wait for the ping manager to end before continue
			 */
			Variables.getLogger().debug("We wait for the ping manager to end");
			while(myPM.isAlive())
				{
				this.sleep(500);
				}
			Variables.getLogger().debug("Ping manager ends");
			/**
			 * End Init 
			 ***************/
			
			/********************
			 * Injection
			 */
			myWW.getAvancement().setText(" "+LanguageManagement.getString("taskbuilding"));
			Task myTask = GwTools.prepareGWProcess(itemToProcessList, action);		
			myTask.startBuildProcess();
			myTask.start();
			
			Variables.getLogger().info("User task of type "+action.name()+" starts");
			
			//We launch the user interface panel
			StatusWindow sw = new StatusWindow(itemToProcessList, myTask);
			Variables.getMyWindow().getContentPane().removeAll();
			Variables.getMyWindow().getContentPane().add(sw);
			Variables.getMyWindow().repaint();
			Variables.getMyWindow().validate();
			
			//We launch the class in charge of monitoring and updating the gui
			new ProgressUpdater(sw, myTask);
			Variables.getLogger().debug("monitoring thread launched");
			/*********************/
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			JOptionPane.showMessageDialog(null,LanguageManagement.getString("usertaskerror")+" : "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		myWW.close();
		}
	
	/*2017*//*RATEL Alexandre 8)*/
	}

