package com.alex.supagwate.device;

import java.util.ArrayList;

import com.alex.supagwate.cli.OneLine;
import com.alex.supagwate.upgrade.UpgradeData;

/**
 * Device Type
 *
 * @author Alexandre RATEL
 */
public class DeviceType
	{
	/**
	 * Variables
	 */
	private String name, vendor;
	private UpgradeData upgradeData;
	private ArrayList<OneLine> howToConnect;
	private ArrayList<OneLine> howToSave;
	private ArrayList<OneLine> howToReboot;
	
	public DeviceType(String name, String vendor, UpgradeData upgradeData, ArrayList<OneLine> howToConnect, ArrayList<OneLine> howToSave,
			ArrayList<OneLine> howToReboot)
		{
		super();
		this.name = name;
		this.vendor = vendor;
		this.upgradeData = upgradeData;
		this.howToConnect = howToConnect;
		this.howToSave = howToSave;
		this.howToReboot = howToReboot;
		}

	public String getName()
		{
		return name;
		}

	public String getVendor()
		{
		return vendor;
		}

	public ArrayList<OneLine> getHowToConnect()
		{
		return howToConnect;
		}

	public ArrayList<OneLine> getHowToSave()
		{
		return howToSave;
		}

	public ArrayList<OneLine> getHowToReboot()
		{
		return howToReboot;
		}

	public UpgradeData getUpgradeData()
		{
		return upgradeData;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
