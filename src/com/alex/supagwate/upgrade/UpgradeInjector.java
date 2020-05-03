package com.alex.supagwate.upgrade;

import java.io.File;
import java.lang.reflect.Field;
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
	private String upgradeFileName;
	private ArrayList<OneLine> checkcurrentversion, checkdiskspace, checkexistingfile, filedelete, startupgrade, checkfile, boot;
	
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
		
		for(Field f : device.getDeviceType().getUpgradeData().getClass().getDeclaredFields())
			{
			if(f.getType().getName().equals(checkdiskspace.getClass().getTypeName()))
				{
				ArrayList<OneLine> olList = (ArrayList<OneLine>) f.get(device.getDeviceType().getUpgradeData());
				ArrayList<OneLine> thisOlList = null;
				for(Field ff : this.getClass().getDeclaredFields())
					{
					if(f.getName().equals(ff.getName()))thisOlList = ((ArrayList<OneLine>) ff.get(this));
					}
				
				for(OneLine ol : olList)
					{
					OneLine l = new OneLine(ol.getCommand(), ol.getType());
					l.resolve(device);
					thisOlList.add(l);
					}
				}
			}
		}
	
	public void exec() throws Exception
		{
		try
			{
			/*****************************************
			 * The complete upgrade process !
			 * 
			 * here we try to manage every single aspect of the upgrade process to tackle
			 * every possible error case
			 * 
			 * We choose to not add "inter command" timer because we already wait for the gateway to reply
			 *******************************************/
			
			Variables.getLogger().debug(device.getInfo()+" : ########## Starting the upgrade process");
			/**
			 * We check the current version
			 * 
			 * In case the gateway would already been up to date
			 */
			Variables.getLogger().debug(device.getInfo()+" : Checking current version");
			for(OneLine ol : checkcurrentversion)clil.execute(ol);
			String reply = clil.getReceiver().getExchange().get(1);//We get line 1 because line 0 is what we have just sent
			String versionFound = reply.split(":")[1].replaceAll("\"", "");
			Variables.getLogger().debug(device.getInfo()+" : Version found : "+versionFound);
			if(versionFound.equals(upgradeFileName))
				{
				Variables.getLogger().debug(device.getInfo()+" : is already up to date, skipping upgrade");
				return;
				}
			
			/**
			 * We check that the file is not already on the flash
			 * 
			 * If yes we will skip the transfer step
			 */
			boolean skipTransfer = false;
			Variables.getLogger().debug(device.getInfo()+" : Checking if the file is not already on the flash");
			for(OneLine ol : checkexistingfile)clil.execute(ol);
			sleep(100);//Just to be sure we got a full response
			for(String s : clil.getReceiver().getExchange())
				{
				if(s.toLowerCase().contains(upgradeFileName.toLowerCase()))
					{
					Variables.getLogger().debug(device.getInfo()+" : The file is already on the flash, skipping the transfer step");
					skipTransfer = true;
					break;
					}
				}
			if(!skipTransfer)Variables.getLogger().debug(device.getInfo()+" : The file was not found on the flash so we continue");
			
			if(!skipTransfer)
				{
				/**
				 * We check the disk space
				 */
				Variables.getLogger().debug(device.getInfo()+" : Checking the available disk space");
				for(OneLine ol : checkdiskspace)clil.execute(ol);
				long availableSpace = Long.parseLong(clil.getReceiver().getExchange().get(1).split(" ")[0]);
				
				Variables.getLogger().debug(device.getInfo()+" : Available disk space : "+availableSpace);
				
				if(availableSpace < upgradeFile.length())
					{
					Variables.getLogger().debug(device.getInfo()+" : There is not enough space to copy the file");
					
					/**
					 * If there is not enough disk space we check if it is possible to delete an old ios file
					 * We will figure out which IOS to delete trying to not delete the current one
					 * In addition we will also check that the deletion will free enough disk space for the new file
					 */
					
					}
				else
					{
					Variables.getLogger().debug(device.getInfo()+" : Enough disk space, good to go !");
					}
				
				/**
				 * Then transfer the file
				 */
				
				}
			
			
			/**
			 * Then we check for file integrity
			 * 
			 * If the file is corrupted, we will end the process and advise to delete it first then retry
			 * the whole procedure. We will not retry the transfer because it might be necessary to figure out
			 * why the first attempt failed
			 */
			
			
			/**
			 * Finally we configure the boot and save
			 */
			
			
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
