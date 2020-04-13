package com.alex.supagwate.misc;

import java.util.List;

import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.cucmVersion;

/**********************************
 * Class used to contain static method for
 * simple common AXL request to the CUCM
 * 
 * @author RATEL Alexandre
 **********************************/
public class SimpleRequest
	{
	
	
	
	/**************
	 * Method aims to return the Version of the CUCM of the asked item
	 *************/
	public static String getCUCMVersion(cucmVersion version, com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCM) throws Exception
		{
		if(version.equals(cucmVersion.version105))
			{
			com.cisco.axl.api._10.GetCCMVersionReq req = new com.cisco.axl.api._10.GetCCMVersionReq();
			com.cisco.axl.api._10.GetCCMVersionRes resp = AXLConnectionToCUCM.getCCMVersion(req);//We send the request to the CUCM
			
			Variables.getLogger().info("CUCM Version : "+resp.getReturn().getComponentVersion().getVersion());
			return resp.getReturn().getComponentVersion().getVersion();
			}
		else
			{
			throw new Exception("Unsupported AXL version");
			}
		}
	
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static List<Object> doSQLQuery(cucmVersion version, String request, com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCM) throws Exception
		{
		if(version.equals(cucmVersion.version105))
			{
			return doSQLQueryV105(request, AXLConnectionToCUCM);
			}
		
		throw new Exception("Unsupported AXL Version");
		}
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static void doSQLUpdate(cucmVersion version, String request, com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCM) throws Exception
		{
		if(version.equals(cucmVersion.version105))
			{
			doSQLUpdateV105(request, AXLConnectionToCUCM);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static List<Object> doSQLQueryV105(String request, com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCM) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLQueryReq req = new com.cisco.axl.api._10.ExecuteSQLQueryReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLQueryRes resp = AXLConnectionToCUCM.executeSQLQuery(req);//We send the request to the CUCM
		
		List<Object> myList = resp.getReturn().getRow();
		
		return myList;
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static void doSQLUpdateV105(String request, com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCM) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLUpdateReq req = new com.cisco.axl.api._10.ExecuteSQLUpdateReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLUpdateRes resp = AXLConnectionToCUCM.executeSQLUpdate(req);//We send the request to the CUCM
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}

