package com.alex.supagwate.ftp;

import java.io.File;

import com.alex.supagwate.device.Device;
import com.alex.supagwate.utils.Variables;

/**
 * Used to store a transfer data
 *
 * @author Alexandre RATEL
 */
public class FTPTransfer
	{
	/**
	 * Variables
	 */
	private long transferredSize;
	private File transferredFile;
	private boolean finish;
	private float progress;//Transfer progress in percent
	private Device device;
	
	public FTPTransfer(Device device, File transferredFile)
		{
		super();
		this.device = device;
		this.transferredFile = transferredFile;
		this.finish = false;
		this.progress = 0;
		this.transferredSize = 0;
		}
	
	private void updateProgress()
		{
		if(progress == 0)Variables.getLogger().debug(device.getInfo()+" : Starting FTP transfer for file : "+transferredFile.getName());
		
		progress = ((float)transferredFile.length()/(float)transferredSize)*100F;
		if(transferredFile.length() >= transferredSize)
			{
			Variables.getLogger().debug(device.getInfo()+" : FTP transfer finished for file : "+transferredFile.getName());
			finish = true;
			}
		}

	public long getTransferredSize()
		{
		return transferredSize;
		}

	public void setTransferredSize(long transferredSize)
		{
		this.transferredSize = transferredSize;
		updateProgress();
		}

	public File getTransferredFile()
		{
		return transferredFile;
		}

	public boolean isFinish()
		{
		return finish;
		}

	public int getProgress()
		{
		return (int)progress;
		}

	public Device getDevice()
		{
		return device;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
