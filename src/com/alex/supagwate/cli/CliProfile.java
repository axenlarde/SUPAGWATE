package com.alex.supagwate.cli;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.supagwate.device.DeviceType;


/**
 * Class used to describe a cli injection profile
 *
 * @author Alexandre RATEL
 */
public class CliProfile
	{
	/**
	 * Variables
	 */
	public enum cliProtocol
		{
		ssh,
		telnet,
		auto
		};
	
	private String name;
	private int defaultInterCommandTimer;
	private DeviceType deviceType;
	private ArrayList<OneLine> cliList;
	
	public CliProfile(String name, DeviceType deviceType, ArrayList<OneLine> cliList, int defaultInterCommandTimer) throws Exception
		{
		super();
		this.name = name;
		this.deviceType = deviceType;
		this.cliList = cliList;
		this.defaultInterCommandTimer = defaultInterCommandTimer;
		
		//We check that no value was null except customsettings
		for(Field f : this.getClass().getDeclaredFields())
			{
			if(f.get(this) == null)
				{
				throw new Exception(f.getName()+" could not be empty");
				}
			}
		
		if(this.cliList.size() == 0)throw new Exception("Cli list could not be empty");
		}

	public ArrayList<OneLine> getCliList()
		{
		return cliList;
		}

	public void setCliList(ArrayList<OneLine> cliList)
		{
		this.cliList = cliList;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public int getDefaultInterCommandTimer()
		{
		return defaultInterCommandTimer;
		}

	public void setDefaultInterCommandTimer(int defaultInterCommandTimer)
		{
		this.defaultInterCommandTimer = defaultInterCommandTimer;
		}

	public DeviceType getDeviceType()
		{
		return deviceType;
		}

	public void setDeviceType(DeviceType deviceType)
		{
		this.deviceType = deviceType;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
