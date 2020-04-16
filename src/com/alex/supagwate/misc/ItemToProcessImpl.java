package com.alex.supagwate.misc;

/**
 * Interface used to define a basic item to migrate
 *
 * @author Alexandre RATEL
 */
public interface ItemToProcessImpl
	{
	public void build() throws Exception;//To build the item
	public void doBuild() throws Exception;//Is called in addition of the main method
	public String getInfo();//To display item info
	public String getDetailedStatus();//Return the detailed status of the item
	public String doGetDetailedStatus();//Return the detailed status of the item
	
	/*2019*//*RATEL Alexandre 8)*/
	}
