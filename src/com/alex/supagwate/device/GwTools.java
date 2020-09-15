package com.alex.supagwate.device;

import java.util.ArrayList;
import java.util.Scanner;

import com.alex.supagwate.action.Injector;
import com.alex.supagwate.action.Task;
import com.alex.supagwate.cli.CliInjector;
import com.alex.supagwate.cli.CliProfile.cliProtocol;
import com.alex.supagwate.gui.WaitingWindow;
import com.alex.supagwate.misc.CollectionTools;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.office.Office;
import com.alex.supagwate.upgrade.UpgradeInjector;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;


/**********************************
 * Class used to store static method
 * used for the site injection
 * 
 * @author RATEL Alexandre
 **********************************/
public class GwTools
	{
	/**
	 * Method used to build the gateway list
	 * 
	 * The goal here is just to gather gateways by office
	 */
	public static ArrayList<MainItem> setGwList(actionType action, WaitingWindow myWW) throws Exception
		{
		ArrayList<MainItem> gwList = new ArrayList<MainItem>();
		
		String deviceTypeTemplate = UsefulMethod.getTargetOption("gwtype");
		String cliProfileTemplate = UsefulMethod.getTargetOption("cliprofile");
		String hostNameTemplate = UsefulMethod.getTargetOption("hostname");
		String gwIPTemplate = UsefulMethod.getTargetOption("gwip");
		String officeNameTemplate = UsefulMethod.getTargetOption("officename");
		String protocolTemplate = UsefulMethod.getTargetOption("protocol");
		String userTemplate = UsefulMethod.getTargetOption("user");
		String passwordTemplate = UsefulMethod.getTargetOption("password");
		
		int lastIndex = CollectionTools.getTheLastIndexOfAColumn(hostNameTemplate);
		
		boolean allowDuplicateIP = Boolean.parseBoolean(UsefulMethod.getTargetOption("allowduplicateip"));
		
		/**
		 * We process the gateway list
		 */
		Variables.getLogger().debug("Gateway preparation process starts");
		for(int i=0; i<lastIndex; i++)
			{
			try
				{
				DeviceType dt = UsefulMethod.getDeviceType((CollectionTools.getValueFromCollectionFile(i, deviceTypeTemplate, null, true)));
				
				/**
				 * We process only selected items
				 */
				if(!Variables.getAllowedItemsToProcess().contains(dt.getName()))continue;
				/***************/
				
				/**
				 * We check for duplicates
				 */
				String deviceName = CollectionTools.getValueFromCollectionFile(i, hostNameTemplate, null, true);
				String IP = CollectionTools.getValueFromCollectionFile(i, gwIPTemplate, null, true);
				
				if(!allowDuplicateIP)
					{	
					boolean found = false;
					for(MainItem mi : gwList)
						{
						for(ItemToProcess itp : mi.getAssociatedItems())
							{
							Device d = (Device)itp;
							if((d.getName()+d.getIp()).equals(deviceName+IP))
								{
								Variables.getLogger().debug("line : "+(i+1)+" : Duplicate found, do not adding the device : "+d.getInfo());
								found = true;
								break;
								}
							
							}
						}
					if(found)continue;
					}
				/****************/
				
				String officeName = CollectionTools.getValueFromCollectionFile(i, officeNameTemplate, null, false);
				Office myO = UsefulMethod.getOffice(officeName);
				
				MItemOffice myIO = new MItemOffice(myO.getName(), myO.getFullname());
					
				//We update the waiting window
				myWW.getAvancement().setText(" "+LanguageManagement.getString("itemlistbuilding")+" : "+(i+1)+"/"+lastIndex+" : "+myIO.getDescription());
					
				/**
				 * Here we check if the MainItem has been used already in the collection file
				 * If yes we do not create a new MainItem but instead, use the existing one
				 */
				boolean foundMI = false;
				for(MainItem mi : gwList)
					{
					if(mi.getDescription().equals(myIO.getDescription()))
						{
						myIO = (MItemOffice) mi;
						foundMI = true;
						break;
						}
					}
				
				Variables.getLogger().debug("Processing device '"+deviceName+"' office '"+myO.getFullname()+"'");
				
				myIO.getAssociatedItems().add(new Device(dt,
						deviceName,
						action,
						IP,
						CollectionTools.getValueFromCollectionFile(i, userTemplate, null, true),
						CollectionTools.getValueFromCollectionFile(i, passwordTemplate, null, true),
						myO,
						cliProtocol.valueOf(CollectionTools.getValueFromCollectionFile(i, protocolTemplate, null, true).toLowerCase()),
						UsefulMethod.getCliprofile(CollectionTools.getValueFromCollectionFile(i, cliProfileTemplate, null, true)),
						i));
				
				if(!foundMI)gwList.add(myIO);
				}
			catch(Exception e)
				{
				Variables.getLogger().error("ERROR while processing gateway line "+i+1+" : "+e.getMessage(), e);
				}
			}
				
		Variables.getLogger().debug("Gateway preparation process ends");
		
		int count = 0;
		for(MainItem mi : gwList)
			{
			count += mi.getAssociatedItems().size();
			}
		Variables.getLogger().debug("Main item list size : "+gwList.size());
		Variables.getLogger().debug("Total gateway found : "+count);
		return gwList;
		}
		
	
	
	/************
	 * Method used to prepare a user injection
	 */
	public static Task prepareGWProcess(ArrayList<MainItem> itemToInjectList, actionType action) throws Exception
		{
		Variables.getLogger().info("Office "+action.name()+" preparation process begins");
		
		ArrayList<ItemToProcess> myList = new ArrayList<ItemToProcess>();
		
		for(MainItem mi : itemToInjectList)
			{
			for(ItemToProcess item : mi.getAssociatedItems())
				{
				Variables.getLogger().info("Adding the "+item.getDeviceType().getName()+" "+item.getName()+" to the list as a "+item.getAction().name()+" todo");
				
				if(action.equals(actionType.upgrade))item.setInjector(new UpgradeInjector((Device)item));
				else item.setInjector(new CliInjector((Device)item));
				
				myList.add(item);
				}
			}
		
		Variables.getLogger().info("Office "+action.name()+" preparation process ends");
		
		//The injection task is ready
		return new Task(myList, action);
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

