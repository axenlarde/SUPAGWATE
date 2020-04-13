package com.alex.supagwate.device;

import java.util.ArrayList;

import com.alex.supagwate.misc.ItemToProcess;

/**********************************
 * Class used to represent an item with its associated itemToInject
 * 
 * It can be a user or a callpickupgroup or a line group, etc..
 * 
 * @author RATEL Alexandre
 **********************************/
public abstract class MainItem
	{
	/**
	 * Variables
	 */
	protected String description;
	ArrayList<ItemToProcess> associatedItems;
	
	/***************
	 * Constructor
	 ***************/
	public MainItem(String description)
		{
		super();
		this.description = description;
		this.description = this.description.trim();
		
		this.associatedItems = new ArrayList<ItemToProcess>();
		}

	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

	public ArrayList<ItemToProcess> getAssociatedItems()
		{
		return associatedItems;
		}

	public void setAssociatedItems(ArrayList<ItemToProcess> associatedItems)
		{
		this.associatedItems = associatedItems;
		}
	
	
	/*2016*//*RATEL Alexandre 8)*/
	}

