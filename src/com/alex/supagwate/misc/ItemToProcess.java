package com.alex.supagwate.misc;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.supagwate.action.Injector;
import com.alex.supagwate.device.DeviceType;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;
import com.alex.supagwate.utils.Variables.statusType;

/**
 * Abstract Item to migrate class
 *
 * @author Alexandre RATEL
 */
public abstract class ItemToProcess implements ItemToProcessImpl
	{
	/**
	 * Variables
	 */	
	protected actionType action;
	protected statusType status;
	protected String id,name;
	protected DeviceType deviceType;
	protected Injector injector;
	protected int index;
	protected ArrayList<ErrorTemplate> errorList;
	protected ArrayList<Correction> correctionList;
	
	/**
	 * Constructor
	 */
	public ItemToProcess(DeviceType deviceType, String name, String patternID, actionType action, int index)
		{
		super();
		this.deviceType = deviceType;
		this.action = action;
		this.name = name;
		this.id = DigestUtils.md5Hex(patternID);
		errorList = new ArrayList<ErrorTemplate>();
		correctionList = new ArrayList<Correction>();
		this.index = index;
		status = statusType.init;
		}
	
	@Override
	public void build() throws Exception
		{
		Variables.getLogger().debug("Starting build for "+deviceType.getName()+" "+name);
		
		injector.build();
		
		doBuild();
		this.setStatus(statusType.waiting);
		}
	
	@Override
	public String getDetailedStatus()
		{
		Variables.getLogger().debug("Displaying informations for "+deviceType.getName()+" "+name);
		
		StringBuffer result = new StringBuffer("");
		
		result.append(doGetDetailedStatus());
		
		return result.toString();
		}
	
	/**
	 * Add an error to the error list and check for duplicate
	 */
	public void addError(ErrorTemplate error)
		{
		boolean duplicate = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)errorList.add(error);
		}
	
	/**
	 * Add a correction to the correction list and check for duplicate
	 */
	public void addCorrection(Correction correction)
		{
		boolean duplicate = false;
		for(Correction c : correctionList)
			{
			if(c.getDescription().equals(correction.getDescription()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)correctionList.add(correction);
		}

	public DeviceType getDeviceType()
		{
		return deviceType;
		}

	public void setDeviceType(DeviceType deviceType)
		{
		this.deviceType = deviceType;
		}

	public statusType getStatus()
		{
		return status;
		}

	public void setStatus(statusType status)
		{
		this.status = status;
		}

	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public int getIndex()
		{
		return index;
		}

	public void setIndex(int index)
		{
		this.index = index;
		}

	public actionType getAction()
		{
		return action;
		}

	public void setAction(actionType action)
		{
		this.action = action;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public ArrayList<Correction> getCorrectionList()
		{
		return correctionList;
		}

	public Injector getInjector()
		{
		return injector;
		}

	public void setInjector(Injector injector)
		{
		this.injector = injector;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}
