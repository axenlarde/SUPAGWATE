package com.alex.supagwate.misc;

import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;


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
	public static void checkForCliSending(actionType action) throws Exception
		{
		try
			{
			Variables.getLogger().info("Collection file check for begins for action : "+action);
			//If a value is empty, an exception is going to be thrown
			
			int lastIndex = CollectionTools.getTheLastIndexOfAColumn(UsefulMethod.getTargetOption("hostname"));
			if(lastIndex == 0)throw new Exception("There is no gateway to process !");
			
			switch(action)
				{
				case set:
					{
					for(int i=0; i<lastIndex; i++)
						{
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwtype"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("cliprofile"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwip"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("protocol"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("user"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("password"));
						}
					break;
					}
				case upgrade:
					{
					for(int i=0; i<lastIndex; i++)
						{
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwtype"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("cliprofile"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwip"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("protocol"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("user"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("password"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("ios"));
						}
					break;
					}
				default:
					{
					for(int i=0; i<lastIndex; i++)
						{
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwtype"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("cliprofile"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("gwip"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("protocol"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("user"));
						CollectionTools.isValueFromCollectionFileEmpty(i, UsefulMethod.getTargetOption("password"));
						}
					break;
					}
				}
			
			Variables.getLogger().info("Collection file check ends : The collection file seems to be correct to proceed");
			}
		catch (Exception e)
			{
			throw new Exception("The collection file is incorrect : Some data are missing : "+e.getMessage());
			}
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

