package com.alex.supagwate.misc;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

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
	protected DeviceType type;
	protected int index;
	protected ArrayList<ErrorTemplate> errorList;
	protected ArrayList<Correction> correctionList;
	
	/**
	 * Constructor
	 */
	public ItemToProcess(DeviceType type, String name, String patternID, actionType action)
		{
		super();
		this.type = type;
		this.action = action;
		this.name = name;
		this.id = DigestUtils.md5Hex(patternID);
		errorList = new ArrayList<ErrorTemplate>();
		status = statusType.init;
		}
	
	@Override
	public void init() throws Exception
		{
		//Write something if needed
		
		doInit();
		}
	
	@Override
	public void build() throws Exception
		{
		Variables.getLogger().debug("Starting build for "+type+" "+name);
		
		doBuild();
		}
	
	@Override
	public String getDetailedStatus()
		{
		Variables.getLogger().debug("Displaying informations for "+type+" "+name);
		
		StringBuffer result = new StringBuffer("");
		/*
		if(errorList.size() > 0)
			{
			result.append("Item error list : \r\n");
			for(ErrorTemplate e : errorList)
				{
				result.append("- "+e.getErrorDesc()+"\r\n");
				}
			result.append("\r\n");
			}
		
		if(axlList.size() > 0)
			{
			result.append("CUCM items : \r\n");
			for(ItemToInject iti : axlList)
				{
				result.append("- "+iti.getName()+" : "+iti.getType().name()+" : "+iti.getStatus().name()+"\r\n");
				for(ErrorTemplate e : iti.getErrorList())
					{
					result.append("	+ "+e+"\r\n");
					}
				}
			result.append("\r\n");
			}*/
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

	public DeviceType getType()
		{
		return type;
		}

	public void setType(DeviceType type)
		{
		this.type = type;
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

	
	/*2020*//*RATEL Alexandre 8)*/
	}
