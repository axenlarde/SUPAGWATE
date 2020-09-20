package com.alex.supagwate.cli;

import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.CollectionTools;

/**
 * Represent one cli command
 *
 * @author Alexandre RATEL
 */
public class OneLine
	{
	/**
	 * variables
	 */
	public enum cliType
		{
		connect,
		disconnect,
		write,
		wait,
		waitfor,
		waitforever,
		get,
		writeif,
		writethenregex,
		save,
		reboot
		};
	
	private String command;
	private cliType type;
	
	public OneLine(String command, cliType type)
		{
		super();
		this.command = command;
		this.type = type;
		}
	
	/**
	 * To resolve command content with device values
	 * @throws Exception 
	 */
	public void resolve(Device d) throws Exception
		{
		command = CollectionTools.getValueFromCollectionFile(d.getIndex(), command, d, false);
		}
	
	public String getInfo()
		{
		return type.name()+" "+command;
		}

	public String getCommand()
		{
		return command;
		}

	public void setCommand(String command)
		{
		this.command = command;
		}

	public cliType getType()
		{
		return type;
		}

	public void setType(cliType type)
		{
		this.type = type;
		}
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
