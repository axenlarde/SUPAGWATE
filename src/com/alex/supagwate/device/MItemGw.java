package com.alex.supagwate.device;

/**********************************
 * Class used to gather everything about a User
 * 
 * @author RATEL Alexandre
 **********************************/
public class MItemGw extends MainItem
	{
	/**
	 * Variables
	 */
	private String hostname,
	ip;

	public MItemGw(String hostname, String ip)
		{
		super(hostname+" : "+ip);
		this.hostname = hostname;
		this.ip = ip;
		}

	public String getHostname()
		{
		return hostname;
		}

	public void setHostname(String hostname)
		{
		this.hostname = hostname;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

