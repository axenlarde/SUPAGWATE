package com.alex.supagwate.upgrade;

import java.util.ArrayList;

import com.alex.supagwate.cli.OneLine;

/**
 * To store upgrade data
 *
 * @author Alexandre RATEL
 */
public class UpgradeData
	{
	/**
	 * Variables
	 */
	private String upgradeFile, destination, checkdiskspace, startupgrade;
	private ArrayList<OneLine> bootList;
	
	public UpgradeData(String upgradeFile, String destination, String checkdiskspace, String startupgrade, ArrayList<OneLine> bootList)
		{
		super();
		this.upgradeFile = upgradeFile;
		this.destination = destination;
		this.checkdiskspace = checkdiskspace;
		this.startupgrade = startupgrade;
		this.bootList = bootList;
		}

	public String getUpgradeFile()
		{
		return upgradeFile;
		}

	public String getDestination()
		{
		return destination;
		}

	public String getStartupgrade()
		{
		return startupgrade;
		}

	public ArrayList<OneLine> getBootList()
		{
		return bootList;
		}

	public String getCheckdiskspace()
		{
		return checkdiskspace;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}
