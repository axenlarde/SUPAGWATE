package com.alex.supagwate.cli;

import java.util.ArrayList;

import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.statusType;



/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public class CliInjector extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private CliProfile cliProfile;
	private ArrayList<String> responses;
	private ArrayList<OneLine> todo;
	private ArrayList<ErrorTemplate> errorList;
	
	public CliInjector(Device device, CliProfile cliProfile)
		{
		super();
		this.device = device;
		this.cliProfile = cliProfile;
		responses = new ArrayList<String>();
		todo = new ArrayList<OneLine>();
		errorList = new ArrayList<ErrorTemplate>();
		}
	
	public void build() throws Exception
		{
		/**
		 * First we get our own version of the cliprofile commands and resolve them to match device values
		 */
		for(OneLine ol : cliProfile.getCliList())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			todo.add(l);
			}
		}
	
	public void run()
		{
		try
			{
			/**
			 * Here we send the cli command
			 */
			CliLinker clil = new CliLinker(this);
			
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection starts");
			
			clil.connect();//First we initialize the connection
			for(OneLine l : todo)
				{
				try
					{
					clil.execute(l);
					this.sleep(cliProfile.getDefaultInterCommandTimer());
					}
				catch (ConnectionException ce)
					{
					throw new ConnectionException(ce);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(device.getInfo()+" : CLI : ERROR whith command : "+l.getInfo());
					device.addError(new ErrorTemplate(device.getInfo()+" : CLI : ERROR whith command : "+l.getInfo()));
					}
				}
			clil.disconnect();//Last we disconnect
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection ends");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : CLI : Critical ERROR : "+e.getMessage());
			device.addError(new ErrorTemplate(device.getInfo()+" : CLI : Critical ERROR : "+e.getMessage()));
			}
		if(device.getErrorList().size() == 0)device.setStatus(statusType.done);
		else device.setStatus(statusType.error);
		}

	
	public Device getDevice()
		{
		return device;
		}

	public CliProfile getCliProfile()
		{
		return cliProfile;
		}

	public ArrayList<String> getResponses()
		{
		return responses;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}

	public ArrayList<OneLine> getTodo()
		{
		return todo;
		}

	/*2020*//*RATEL Alexandre 8)*/
	}
