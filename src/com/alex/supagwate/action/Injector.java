package com.alex.supagwate.action;

import com.alex.supagwate.cli.CliLinker;
import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.statusType;



/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public abstract class Injector extends Thread implements InjectorImpl
	{
	/**
	 * Variables
	 */
	protected Device device;
	protected CliLinker clil;
	
	public Injector(Device device)
		{
		super();
		this.device = device;
		}
	
	public void build() throws Exception
		{
		doBuild();
		}
	
	public void run()
		{
		try
			{
			/**
			 * Here we send the cli command
			 */
			clil = new CliLinker(this);
			clil.connect();//First we initialize the connection
			Variables.getLogger().debug(device.getInfo()+" : Connected successfully, ready for cli injection");
			
			exec();
			
			clil.disconnect();//Last we disconnect
			Variables.getLogger().debug(device.getInfo()+" : Disconnected successfully");
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

	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
