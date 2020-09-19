package com.alex.supagwate.ftp;

import java.io.File;

import org.apache.ftpserver.impl.DefaultFtpServer;

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
	private DefaultFtpServer server;
	
	public FTPTransfer(Device device, File transferredFile, DefaultFtpServer server)
		{
		super();
		this.device = device;
		this.transferredFile = transferredFile;
		this.finish = false;
		this.progress = 0;
		this.transferredSize = 0;
		this.server = server;
		}
	
	private void updateProgress()
		{
		if(progress == 0)Variables.getLogger().debug(device.getInfo()+" : Starting FTP transfer for file : "+transferredFile.getName());
		
		progress = (((float)transferredSize)/((float)transferredFile.length()))*100F;
		if(transferredSize >= transferredFile.length())
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
