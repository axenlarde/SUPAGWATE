package com.alex.supagwate.misc;

import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;


/**********************************
 * Class aims to contain static method to check
 * Colleciton file content is correct
 * 
 * @author RATEL Alexandre
 **********************************/
public class CollectionFileChecker
	{
	/**
	 * Variables
	 */
	
	
	/****************
	 * Method used to validate if the collection file is correct
	 * 
	 * To do that we simply check if some values are empty
	 */
	public static void checkForCliSending() throws Exception
		{
		try
			{
			Variables.getLogger().info("Collection file check for a cli sending begin");
			//If a value is empty, an exception is going to be thrown
			
			int lastIndex = CollectionTools.getTheLastIndexOfAColumn(UsefulMethod.getTargetOption("hostname"));
			if(lastIndex == 0)throw new Exception("There is no gateway to process !");
			
			for(int i=0; i<lastIndex; i++)
				{
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwtype"));
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("cliprofile"));
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwip"));
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("protocol"));
				}
			
			//Add some more check
			
			Variables.getLogger().info("Collection file check end : The collection file seems to be correct for a user creation");
			}
		catch (Exception e)
			{
			throw new Exception("The collection file is incorrect : Some data are missing : "+e.getMessage());
			}
		}
	
	/****************
	 * Method used to validate if the collection file is correct
	 * 
	 * To do that we simply check if some values are empty
	 */
	public static void checkForQuickCliSending() throws Exception
		{
		try
			{
			Variables.getLogger().info("Collection file check for a cli sending begin");
			//If a value is empty, an exception is going to be thrown
			
			int lastIndex = CollectionTools.getTheLastIndexOfAColumn(UsefulMethod.getTargetOption("hostname"));
			if(lastIndex == 0)throw new Exception("There is no gateway to process !");
			
			for(int i=0; i<lastIndex; i++)
				{
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwip"));
				CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("protocol"));
				}
			
			//Add some more check
			
			Variables.getLogger().info("Collection file check end : The collection file seems to be correct for a user creation");
			}
		catch (Exception e)
			{
			throw new Exception("The collection file is incorrect : Some data are missing : "+e.getMessage());
			}
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

