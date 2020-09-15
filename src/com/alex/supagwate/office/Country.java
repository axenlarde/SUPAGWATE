package com.alex.supagwate.office;

import java.util.ArrayList;

import com.alex.supagwate.utils.Variables.language;

/**
 * Country
 *
 * @author Alexandre RATEL
 */
public class Country
	{
	/**
	 * Variables
	 */
	private String name,
	e164;
	
	private language lang;
	private ArrayList<CustomSettings> settings;
	
	public Country(String name, String e164, language lang, ArrayList<CustomSettings> settings)
		{
		super();
		this.name = name;
		this.e164 = e164;
		this.lang = lang;
		this.settings = settings;
		}

	public String getName()
		{
		return name;
		}

	public String getE164()
		{
		return e164;
		}

	public language getLang()
		{
		return lang;
		}

	public ArrayList<CustomSettings> getSettings()
		{
		return settings;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
