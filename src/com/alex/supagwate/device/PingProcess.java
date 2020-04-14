package com.alex.supagwate.device;

import java.net.InetAddress;

import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.reachableStatus;

/**
 * used to ping a device
 *
 * @author Alexandre RATEL
 */
public class PingProcess extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private int timeout;

	public PingProcess(Device device)
		{
		super();
		this.device = device;
		this.timeout = 5000;
		}
	
	public void run()
		{
		device.setReachable(ping(device.getIp())?reachableStatus.reachable:reachableStatus.unreachable);
		}
	
	private boolean ping(String ip)
		{
		try
			{
			InetAddress inet = InetAddress.getByName(ip);
			boolean reachable = inet.isReachable(timeout);
			Variables.getLogger().debug(device.getInfo()+" reachable status : "+reachable);
			return reachable;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while pinging "+device.getInfo());
			}
		return false;
		}

	public void setTimeout(int timeout)
		{
		this.timeout = timeout;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
