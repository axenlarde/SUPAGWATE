package com.alex.supagwate.cli;

import java.util.ArrayList;

import com.alex.supagwate.action.Injector;
import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.utils.Variables;



/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public class CliInjector extends Injector
	{
	/**
	 * Variables
	 */
	private ArrayList<OneLine> todo;
	
	public CliInjector(Device device)
		{
		super(device);
		todo = new ArrayList<OneLine>();
		}
	
	public void doBuild() throws Exception
		{
		/**
		 * First we get our own version of the cliprofile commands and resolve them to match device values
		 */
		for(OneLine ol : device.getCliProfile().getCliList())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			todo.add(l);
			}
		}
	
	public void exec() throws Exception
		{
		for(OneLine l : todo)
			{
			try
				{
				clil.execute(l);
				this.sleep(device.getCliProfile().getDefaultInterCommandTimer());
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
		}

	public ArrayList<OneLine> getTodo()
		{
		return todo;
		}

	/*2020*//*RATEL Alexandre 8)*/
	}
