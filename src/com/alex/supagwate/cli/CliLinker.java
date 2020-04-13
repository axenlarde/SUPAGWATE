package com.alex.supagwate.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import com.alex.supagwate.cli.CliConnection.connectedTech;
import com.alex.supagwate.cli.CliProfile.cliProtocol;
import com.alex.supagwate.device.Device;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;


/**
 * This class will connect to the given device and send the given command
 *
 * @author Alexandre RATEL
 */
public class CliLinker
	{
	/**
	 * Variables
	 */
	private Device device;
	private cliProtocol protocol;
	private CliConnection connection;
	private CliInjector clii;
	private BufferedWriter out;
	private AnswerReceiver receiver;
	private int timeout;
	private String carrierReturn;
	
	public CliLinker(CliInjector clii)
		{
		super();
		this.clii = clii;
		this.device = clii.getDevice();
		this.protocol = device.getConnexionProtocol();
		
		if(protocol.equals(cliProtocol.ssh))
			{
			this.carrierReturn = "\n";
			}
		else
			{
			this.carrierReturn = "\r\n";
			}
		
		try
			{
			this.timeout = Integer.parseInt(UsefulMethod.getTargetOption("cliconnectiontimeout"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : CLI : Unable to find timeout so we apply the default value");
			this.timeout = 10000;
			}
		}
	
	/**
	 * here we connect using the given ip
	 * @throws ConnectionException 
	 */
	public void connect(String ip) throws ConnectionException, Exception
		{
		try
			{
			connection = new CliConnection(device.getUser(),
					device.getPassword(),
					ip,
					device.getInfo(),
					device.getConnexionProtocol(),
					timeout);
			
			connection.connect();
			out = connection.getOut();
			receiver = connection.getReceiver();
			
			/**
			 * Using telnet credentials cannot be sent during the connection process
			 * Instead, we have to send them once connected when prompted
			 * So we add an extra step
			 */
			if(connection.getcTech().equals(connectedTech.telnet))
				{
				telnetAuth();
				}
			}
		catch (Exception e)
			{
			throw new ConnectionException("Failed to connect : "+e.getMessage());
			}
		}
	
	/**
	 * Here we connect using the default device ip
	 * @throws ConnectionException 
	 */
	public void connect() throws ConnectionException, Exception
		{
		connect(device.getIp());
		}
	
	public void disconnect()
		{
		connection.close();
		}
	
	public String waitFor(String s) throws ConnectionException, Exception
		{
		int timer = 0;
		
		Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for the word :"+s);
		
		boolean onlyOnce = true;
		
		while(true)
			{
			for(int i=0; i<receiver.getExchange().size(); i++)
				{
				//(?i) : Case insensitive
				if(Pattern.matches("(?i).*"+s+".*",receiver.getExchange().get(i)))
					{
					String SToReturn = receiver.getExchange().get(i);
					receiver.getExchange().clear();
					return SToReturn;
					}
				else if(onlyOnce)
					{
					/**
					 * We send a carriage return just to activate the connection
					 * and only once
					 */
					out.write(carrierReturn);
					out.flush();
					onlyOnce = false;
					}
				}
			
			clii.sleep(100);
			if(timer>100)
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : We have been waiting too longfor '"+s+"' so we keep going");
				break;
				}
			timer++;
			}
		return null;
		}
	
	/**
	 * Simply wait for a return from the gateway
	 * @throws ConnectionException, Exception 
	 */
	private String waitForAReturn() throws ConnectionException, Exception
		{
		int timer = 0;
		
		//Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for a reply");
		
		while(true)
			{
			if(receiver.getExchange().size() > 0)return receiver.getExchange().get(0);
			
			clii.sleep(100);
			if(timer>100)
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : We have been waiting too long so we keep going");
				break;
				}
			timer++;
			}
		return null;
		}
	
	
	public void write(String s) throws ConnectionException, Exception
		{
		if(!connection.isConnected())connect();
		
		receiver.getExchange().clear();
		out.write(s+carrierReturn);
		out.flush();
		waitForAReturn();
		}
	
	/**
	 * Will write a command and then analyze the result
	 * if the result contains the given string we will write the given command
	 * otherwise will write the other command instead
	 * 
	 * The command patternis the following
	 * command to send:::string to compare:::write if the result contains the string to compare:::write if not
	 * The last parameters is optional. You can write only a:::b:::c
	 */
	public void writeIf(String s) throws ConnectionException, Exception
		{
		String[] cmdTab = s.split(":::");
		if(!connection.isConnected())connect();
		
		receiver.getExchange().clear();
		out.write(cmdTab[0]+carrierReturn);
		out.flush();
		
		if(waitForAReturn().toLowerCase().equals(cmdTab[1].toLowerCase()))
			{
			write(cmdTab[2]);
			}
		else
			{
			if(cmdTab.length == 4)write(cmdTab[3]);
			}
		}
	
	
	/**
	 * Aims to send telnet credentials once prompted
	 */
	public void telnetAuth() throws Exception
		{
		try
			{
			/*******
			 * Write instruction used to authenticate to gateway using telnet protocol 
			 */
			Variables.getLogger().debug(device.getInfo()+" : CLI : Authentication process begin");
			for(OneLine l : device.getType().getHowToConnect())
				{
				execute(l);
				}
			Variables.getLogger().debug(device.getInfo()+" : CLI : Telnet connection initiated successfully");
			}
		catch(Exception exc)
			{
			throw new ConnectionException(device.getInfo()+" : CLI : Unable to connect using telnet : "+exc.getMessage());
			}
		}
	
	public void execute(OneLine l) throws ConnectionException, Exception
		{
		//Variables.getLogger().debug(device.getInfo()+" : CLI : Sending : "+l.getCommand());
		switch(l.getType())
			{
			case connect:
				{
				connect(l.getCommand());
				break;
				}
			case disconnect:
				{
				disconnect();
				break;
				}
			case wait:
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for "+l.getCommand()+" ms");
				clii.sleep(Long.parseLong(l.getCommand()));
				break;
				}
			case waitfor:
				{
				waitFor(l.getCommand());
				break;
				}
			case write:
				{
				write(l.getCommand());
				break;
				}
			case writeif:
				{
				writeIf(l.getCommand());
				break;
				}
			case get:
				{
				//To be written
				break;
				}
			case save:
				{
				for(OneLine ol : device.getType().getHowToSave())
					{
					execute(ol);
					}
				break;
				}
			case reboot:
				{
				for(OneLine ol : device.getType().getHowToReboot())
					{
					execute(ol);
					}
				break;
				}
			default:
				{
				write(l.getCommand());
				break;
				}
			}
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
