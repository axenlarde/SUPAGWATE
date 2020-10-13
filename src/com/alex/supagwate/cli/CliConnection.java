package com.alex.supagwate.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;

import com.alex.supagwate.cli.CliProfile.cliProtocol;
import com.alex.supagwate.utils.Variables;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Used to establish the connection to a device
 *
 * @author Alexandre RATEL
 */
public class CliConnection implements TelnetNotificationHandler
	{
	/**
	 * Variables
	 */
	public enum connectedTech
		{
		sshd,
		jsch,
		telnet
		};
	
	private String user,password,ip,info;
	private BufferedWriter out;
	private BufferedReader in;
	private AnswerReceiver receiver;
	private cliProtocol protocol;
	private TelnetClient telnetConnection;
	private Channel SSHConnection;
	private int timeout;
	private SshClient SSHDClient;
	private Session SSHSession;
	private ClientChannel SSHDConnection;
	private connectedTech cTech;
	
	public CliConnection(String user, String password, String ip, String info, cliProtocol protocol, int timeout)
		{
		super();
		this.user = user;
		this.password = password;
		this.ip = ip;
		this.info = info;
		this.protocol = protocol;
		this.timeout = timeout;
		}

	/**
	 * To initialize the connection
	 */
	public void connect() throws Exception, ConnectionException
		{
		if(protocol.equals(cliProtocol.ssh))
			{
			trySSH();
			}
		else if(protocol.equals(cliProtocol.telnet))
			{
			initTelnet();
			cTech = connectedTech.telnet;
			}
		else
			{
			/**
			 * Only if "auto" is the choosen cli protocol
			 */
			Variables.getLogger().debug("CLI : auto connection protocol selected");
			
			try
				{
				trySSH();
				}
			catch(Exception e)
				{
				Variables.getLogger().error("CLI : Failed to connect using SSH : "+e.getMessage());
				try
					{
					Variables.getLogger().debug("CLI : Trying with telnet");
					initTelnet();
					cTech = connectedTech.telnet;
					}
				catch (Exception ex)
					{
					Variables.getLogger().error("CLI : Failed to connect using telnet as well : "+ex.getMessage());
					throw new ConnectionException(ex.getMessage());
					}
				}
			}
		
		receiver = new AnswerReceiver(info, in);
		receiver.start();
		}
	
	/**
	 * To initialize a SSH connection using both method
	 */
	private void trySSH() throws Exception, ConnectionException
		{
		try
			{
			initSSH();
			cTech = connectedTech.jsch;
			}
		catch(Exception e)
			{
			Variables.getLogger().error("CLI : Failed to connect using jsch : "+e.getMessage());
			try
				{
				Variables.getLogger().debug("CLI : Trying with apache sshd");
				initSSHD();
				cTech = connectedTech.sshd;
				}
			catch (Exception ex)
				{
				Variables.getLogger().error("CLI : Failed to connect using apache sshd : "+ex.getMessage());
				throw new ConnectionException("Failed to connect : "+ex.getMessage());
				}
			}
		}
	
	/**
	 * Initialize SSH connection using apache sshd
	 * @throws Exception 
	 */
	public void initSSHD() throws Exception
		{
		Variables.getLogger().debug(info+" CLI : init SSH connection using SSHD");
		try
			{
			SSHDClient = SshClient.setUpDefaultClient();
			SSHDClient.start();
			ClientSession session = SSHDClient.connect(user, ip, 22).verify(timeout).getSession();
			session.addPasswordIdentity(password);
	        session.auth().verify(timeout);
	        
	        SSHDConnection = session.createChannel(ClientChannel.CHANNEL_SHELL);
	        SSHDConnection.open().verify(timeout);
	        out = new BufferedWriter(new OutputStreamWriter(SSHDConnection.getInvertedIn()));
	        in = new BufferedReader(new InputStreamReader(SSHDConnection.getInvertedOut()));
	        
	        
	        Variables.getLogger().debug(info+" : CLI : SSH connection initiated successfully");
			}
		catch (Exception e)
			{
			throw new ConnectionException(info+" CLI : Unable to connect using SSH : "+e.getMessage());
			}
		}
	
	
	/**
	 * Initialize SSH connection using jsch
	 * @throws Exception 
	 */
	public void initSSH() throws Exception
		{
		Variables.getLogger().debug(info+" CLI : init SSH connection using JSCH");
		
		try
			{
			JSch jsch = new JSch();
			SSHSession = jsch.getSession(user, ip, 22);
			SSHSession.setPassword(password);
			SSHSession.setConfig("StrictHostKeyChecking", "no");
			
			//Connection to the ssh server with timeout
			SSHSession.connect(timeout);
			
			//Start the shell session
			SSHConnection = SSHSession.openChannel("shell");
			
			//Assign input and output Stream
			out = new BufferedWriter(new OutputStreamWriter(SSHConnection.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(SSHConnection.getInputStream()));
			
			((ChannelShell)SSHConnection).setPtyType("vt100");
			//((ChannelShell)SSHConnection).setPtyType("vt102");
			SSHConnection.connect();
			
			Variables.getLogger().debug(info+" : CLI : SSH connection initiated successfully");
			}
		catch (Exception e)
			{
			throw new ConnectionException(info+" CLI : Unable to connect using SSH : "+e.getMessage());
			}
		}
	
	/***********************************
	 * Method used to open and prepare 
	 * the connection with the gateway
	 * using telnet protocol
	 ***********************************/
	public void initTelnet() throws Exception
		{
		Variables.getLogger().debug(info+" CLI : init Telnet connection");
		
		telnetConnection = new TelnetClient();
        
        //Options
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", true, false, true, true);
        EchoOptionHandler echoopt = new EchoOptionHandler(false, false, true, true);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(false, false, true, true);
        
        telnetConnection.addOptionHandler(ttopt);
        telnetConnection.addOptionHandler(echoopt);
        telnetConnection.addOptionHandler(gaopt);
        telnetConnection.setConnectTimeout(timeout);
        telnetConnection.connect(ip,23);
        telnetConnection.registerNotifHandler(this);
        out = new BufferedWriter(new OutputStreamWriter(telnetConnection.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(telnetConnection.getInputStream()));
		
		Variables.getLogger().debug(info+" : CLI : First step of telnet connection initiated successfully");
		}
	
	
	public void receivedNegotiation(int negotiation_code, int option_code)
		{
		String command = null;
        if(negotiation_code == TelnetNotificationHandler.RECEIVED_DO)
	        {
	        command = "DO";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_DONT)
	        {
	        command = "DONT";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_WILL)
	        {
	        command = "WILL";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_WONT)
	        {
	        command = "WONT";
	        }
        Variables.getLogger().debug(info+" CLI : Telnet negociation command received : "+command);
		}
	
	public boolean isConnected()
		{
		switch(cTech)
			{
			case sshd:return SSHDConnection.isOpen();
			case jsch:return SSHConnection.isConnected();
			case telnet:return telnetConnection.isConnected();
			default:return false;
			}
		}
	
	/**
	 * To close the connection with the device
	 */
	public void close()
		{
		try
			{
			receiver.setStop(true);
			
			switch(cTech)
				{
				case sshd:
					{
					SSHDConnection.close();
					SSHDClient.stop();
					break;
					}
				case jsch:
					{
					SSHConnection.disconnect();
					SSHSession.disconnect();
					break;
					}
				case telnet:
					{
					telnetConnection.disconnect();
					}
				}
			
			Variables.getLogger().debug(info+" : CLI : Device disconnected successfully");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(info+" : CLI : ERROR while closing connection : "+e.getMessage());
			}
		}

	public BufferedWriter getOut()
		{
		return out;
		}

	public BufferedReader getIn()
		{
		return in;
		}

	public AnswerReceiver getReceiver()
		{
		return receiver;
		}

	public connectedTech getcTech()
		{
		return cTech;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
