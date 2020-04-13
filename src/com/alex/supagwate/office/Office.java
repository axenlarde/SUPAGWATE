package com.alex.supagwate.office;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**********************************
 * Class used to store an Office
 * 
 * @author RATEL Alexandre
 **********************************/
public class Office
	{
	/********
	 * Variables
	 */
	private String name,
	templatename,
	fullname,
	internalprefix,
	receptionnumber;
	
	private Country country;
	private CUCM cucm;
	
	//Lists
	private ArrayList<IPRange> voiceIpRange;
	private ArrayList<IPRange> dataIpRange;
	private ArrayList<DidRange> didRanges;
	private ArrayList<CustomSettings> settings;
	
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public Office(String name, String templatename, String fullname, String internalprefix, String receptionnumber,
			Country country, CUCM cucm, ArrayList<IPRange> voiceIpRange, ArrayList<IPRange> dataIpRange,
			ArrayList<DidRange> didRanges, ArrayList<CustomSettings> settings) throws Exception
		{
		super();
		this.name = name;
		this.templatename = templatename;
		this.fullname = fullname;
		this.internalprefix = internalprefix;
		this.receptionnumber = receptionnumber;
		this.country = country;
		this.cucm = cucm;
		this.voiceIpRange = voiceIpRange;
		this.dataIpRange = dataIpRange;
		this.didRanges = didRanges;
		this.settings = settings;
		
		//We check that no value was null except customsettings
		for(Field f : this.getClass().getDeclaredFields())
			{
			if(f.getName().equals("settings"))
				{
				//we do nothing
				}
			else if(f.get(this) == null)
				{
				throw new Exception(f.getName()+" could not be empty");
				}
			}
		}

	/******
	 * Used to return a value based on the string provided
	 * @throws Exception 
	 */
	public String getString(String s) throws Exception
		{
		String tab[] = s.split("\\.");
		
		if(tab.length == 2)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().equals(tab[1]))
					{
					return (String) f.get(this);
					}
				}
			}
		else if(tab.length == 3)
			{
			//Here we treat the particular cases
			if(tab[1].equals("setting"))
				{
				for(CustomSettings os : settings)
					{
					if(os.getTargetname().equals(tab[2]))return os.getValue();
					}
				}
			else if(tab[1].equals("did"))
				{
				return didRanges.get(Integer.parseInt(tab[2])-1).getPattern();
				}
			}
		
		throw new Exception("ERROR : No value found");
		}

	public String getName()
		{
		return name;
		}

	public String getTemplatename()
		{
		return templatename;
		}

	public String getFullname()
		{
		return fullname;
		}

	public String getInternalprefix()
		{
		return internalprefix;
		}

	public String getReceptionnumber()
		{
		return receptionnumber;
		}

	public Country getCountry()
		{
		return country;
		}

	public CUCM getCucm()
		{
		return cucm;
		}

	public ArrayList<IPRange> getVoiceIpRange()
		{
		return voiceIpRange;
		}

	public ArrayList<IPRange> getDataIpRange()
		{
		return dataIpRange;
		}

	public ArrayList<DidRange> getDidRanges()
		{
		return didRanges;
		}

	public ArrayList<CustomSettings> getSettings()
		{
		return settings;
		}

	
	/*2019*//*RATEL Alexandre 8)*/
	}

