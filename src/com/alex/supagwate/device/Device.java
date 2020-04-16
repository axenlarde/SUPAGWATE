package com.alex.supagwate.device;

import java.lang.reflect.Field;

import com.alex.supagwate.cli.CliInjector;
import com.alex.supagwate.cli.CliProfile;
import com.alex.supagwate.cli.CliProfile.cliProtocol;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.office.Office;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;
import com.alex.supagwate.utils.Variables.reachableStatus;
import com.alex.supagwate.utils.Variables.statusType;

/**
 * Represent a device
 *
 * @author Alexandre RATEL
 */
public class Device extends ItemToProcess
	{
	/**
	 * Variables
	 */
	protected String ip,
	user,
	password;
	
	protected Office office;
	protected reachableStatus reachable;
	protected CliInjector cliInjector;
	protected CliProfile cliProfile;
	protected cliProtocol connexionProtocol;
	
	public Device(DeviceType type, String name, actionType action, String ip, String user, String password,
			Office office, cliProtocol connexionProtocol, CliProfile cliProfile, int index)
		{
		super(type, name, name+ip, action, index);
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.office = office;
		this.connexionProtocol = connexionProtocol;
		this.cliInjector = new CliInjector(this,
				cliProfile);
		this.reachable = reachableStatus.unknown;
		}

	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		
		s.append(type.getName()+" ");
		s.append(ip+" : ");
		s.append(name);
		
		int maxchar = 60;
		
		try
			{
			maxchar = Integer.parseInt(UsefulMethod.getTargetOption("maxinfochar"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Unable to retrieve maxinfochar");
			}
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
		}
	
	//To init the item
	@Override
	public void doBuild() throws Exception
		{
		/**
		 * If the ping failed we disable the device
		 * No point to send command to an unreachable 
		 */
		if(reachable.equals(reachableStatus.unreachable))this.setStatus(statusType.disabled);
		
		/**
		 * Then we initialize the CLI list
		 */
		if(cliInjector != null)cliInjector.build();
		}

	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String doGetDetailedStatus()
		{
		StringBuffer s = new StringBuffer("");
		
		switch(reachable)
			{
			case reachable:
				{
				s.append("Reachable : true");
				break;
				}
			case unreachable:
				{
				s.append("Reachable : false");
				break;
				}
			default:
				{
				s.append("Reachable : unknown");
				break;
				}
			}
		
		/*
		if(cliInjector.getErrorList().size() > 0)
			{
			s.append("\r\n");
			s.append("Cli error list : \r\n");
			
			for(ErrorTemplate e : cliInjector.getErrorList())
				{
				s.append(e.getErrorDesc()+"\r\n");
				}
			}*/
		
		if((cliInjector != null) && (cliInjector.getErrorList().size() > 0))s.append(", Error found");
		else if((errorList != null) && (errorList.size() > 0))s.append(", Error found");
		
		return s.toString();
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
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			//We try also in the super class
			for(Field f : this.getClass().getSuperclass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			}
		
		return null;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public String getUser()
		{
		return user;
		}

	public void setUser(String user)
		{
		this.user = user;
		}

	public String getPassword()
		{
		return password;
		}

	public void setPassword(String password)
		{
		this.password = password;
		}

	public Office getOffice()
		{
		return office;
		}

	public void setOffice(Office office)
		{
		this.office = office;
		}

	public reachableStatus getReachable()
		{
		return reachable;
		}

	public void setReachable(reachableStatus reachable)
		{
		this.reachable = reachable;
		}

	public CliInjector getCliInjector()
		{
		return cliInjector;
		}

	public void setCliInjector(CliInjector cliInjector)
		{
		this.cliInjector = cliInjector;
		}

	public cliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(cliProtocol connexionProtocol)
		{
		this.connexionProtocol = connexionProtocol;
		}
		
	/*2020*//*RATEL Alexandre 8)*/
	}
