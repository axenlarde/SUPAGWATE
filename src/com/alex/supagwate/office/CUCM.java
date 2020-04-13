package com.alex.supagwate.office;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.supagwate.misc.SimpleRequest;
import com.alex.supagwate.risport.RisportTools;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.cucmVersion;
import com.cisco.schemas.ast.soap.RisPortType;

/**
 * CUCM
 *
 * @author Alexandre RATEL
 */
public class CUCM
	{
	/**
	 * Variables
	 */
	private String name,
	ip,
	axlport,
	axlusername,
	axlpassword,
	risport,
	risusername,
	rispassword,
	rismaxphonerequest;
	
	private cucmVersion version;
	private boolean reachable;
	//AXL
    private com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCMV105;//Connection to CUCM version 105
    //RISPORT
    private RisPortType risConnection;

	public CUCM(String name, cucmVersion version, String ip, String axlport, String axlusername, String axlpassword,
			String risport, String risusername, String rispassword, String rismaxphonerequest) throws Exception
		{
		super();
		this.name = name;
		this.version = version;
		this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
		this.axlport = axlport;
		this.axlusername = axlusername;
		this.axlpassword = axlpassword;
		this.risport = risport;
		this.risusername = risusername;
		this.rispassword = rispassword;
		this.rismaxphonerequest = rismaxphonerequest;
		reachable = false;
		
		//We check that no String value was null
		for(Field f : this.getClass().getDeclaredFields())
			{
			if(f.get(this) instanceof String)
				{
				if(f.get(this) == null)throw new Exception(f.getName()+" could not be empty");
				}
			}
		}

	public String getName()
		{
		return name;
		}

	public cucmVersion getVersion()
		{
		return version;
		}

	public String getIp()
		{
		return ip;
		}

	public String getAxlport()
		{
		return axlport;
		}

	public String getAxlusername()
		{
		return axlusername;
		}

	public String getAxlpassword()
		{
		return axlpassword;
		}

	public String getRisport()
		{
		return risport;
		}

	public String getRisusername()
		{
		return risusername;
		}

	public String getRispassword()
		{
		return rispassword;
		}

	public String getRismaxphonerequest()
		{
		return rismaxphonerequest;
		}

	public boolean isReachable()
		{
		return reachable;
		}

	public com.cisco.axlapiservice10.AXLPort getAXLConnectionToCUCMV105()
		{
		if(AXLConnectionToCUCMV105 == null)
			{
			try
				{
				this.AXLConnectionToCUCMV105 = UsefulMethod.initAXLConnectionToCUCM(this);
				
				/**
				 * We now try to get the cucm version to test the connection
				 */
				SimpleRequest.getCUCMVersion(version, AXLConnectionToCUCMV105);
				this.reachable = true;
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR while initializing the CUCM AXL connection : "+e.getMessage(), e);
				}
			}
		return AXLConnectionToCUCMV105;
		}

	public RisPortType getRisConnection()
		{
		if(risConnection == null)
			{
			try
				{
				this.risConnection = UsefulMethod.initRISConnectionToCUCM(this);
				this.reachable = true;
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR while initializing CUCM RIS connection : "+e.getMessage(), e);
				}
			}
		return risConnection;
		}

	public void setReachable(boolean reachable)
		{
		this.reachable = reachable;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
