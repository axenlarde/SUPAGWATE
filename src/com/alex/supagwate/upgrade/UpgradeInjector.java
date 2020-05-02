package com.alex.supagwate.upgrade;

import java.io.File;
import java.util.ArrayList;

import com.alex.supagwate.action.Injector;
import com.alex.supagwate.cli.ConnectionException;
import com.alex.supagwate.cli.OneLine;
import com.alex.supagwate.cli.OneLine.cliType;
import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.CollectionTools;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;



/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public class UpgradeInjector extends Injector
	{
	/**
	 * Variables
	 */
	private File upgradeFile;
	private String upgradeFileName, destination, checkdiskspace, startupgrade;
	private ArrayList<OneLine> bootList;
	
	public UpgradeInjector(Device device)
		{
		super(device);
		}
	
	public void doBuild() throws Exception
		{
		/**
		 * We need to resolve all the needed value 
		 */
		String upgradeFileName = CollectionTools.getRawValue(device.getCliProfile().getDeviceType().getUpgradeData().getUpgradeFile(), device, true);
		upgradeFile = new File(Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("ftpdirectory")+"/"+upgradeFileName);
		destination = CollectionTools.getRawValue(device.getCliProfile().getDeviceType().getUpgradeData().getDestination(), device, true);
		checkdiskspace = CollectionTools.getRawValue(device.getCliProfile().getDeviceType().getUpgradeData().getCheckdiskspace(), device, true);
		startupgrade = CollectionTools.getRawValue(device.getCliProfile().getDeviceType().getUpgradeData().getStartupgrade(), device, true);
		
		for(OneLine ol : device.getCliProfile().getDeviceType().getUpgradeData().getBootList())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			bootList.add(ol);
			}
		}
	
	public void exec() throws Exception
		{
		try
			{
			/**
			 * We check the current version
			 */
			
			/**
			 * We check that the file is not already on the flash
			 */
			
			/**
			 * We check the disk space
			 */
			clil.execute(new OneLine(checkdiskspace, cliType.write));
			String reply = clil.getReceiver().getExchange().get(1);//We get line 1 because line 0 is what we have just sent
			
			/**
			 * If there is not enough disk space we check if it is possible to delete an old ios file
			 * We will figure out which IOS to delete trying to not delete the current one
			 * In addition we will also check that the deletion will free enough disk space for the new file
			 */
			
			
			
			/**
			 * Then transfer the file
			 */
			
			
			/**
			 * Then we check for file integrity
			 */
			
			
			/**
			 * Finally we configure the boot and save
			 */
			
			
			this.sleep(device.getCliProfile().getDefaultInterCommandTimer());
			}
		catch (ConnectionException ce)
			{
			throw new ConnectionException(ce);
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : ERROR While upgrading : "+e.getMessage(),e);
			device.addError(new ErrorTemplate(device.getInfo()+" : ERROR While upgrading : "+e.getMessage()));
			}
		}


	/*2020*//*RATEL Alexandre 8)*/
	}
