package com.alex.supagwate.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFileChooser;
import javax.xml.ws.BindingProvider;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Level;

import com.alex.supagwate.cli.CliGetOutput;
import com.alex.supagwate.cli.CliProfile;
import com.alex.supagwate.cli.CliProfile.cliProtocol;
import com.alex.supagwate.cli.OneLine.cliType;
import com.alex.supagwate.cli.OneLine;
import com.alex.supagwate.device.Device;
import com.alex.supagwate.device.DeviceType;
import com.alex.supagwate.ftp.FTPTransfer;
import com.alex.supagwate.misc.SimpleRequest;
import com.alex.supagwate.office.DidRange;
import com.alex.supagwate.office.IPRange;
import com.alex.supagwate.office.Office;
import com.alex.supagwate.office.CUCM;
import com.alex.supagwate.office.Country;
import com.alex.supagwate.office.CustomSettings;
import com.alex.supagwate.office.Range;
import com.alex.supagwate.risport.RisportTools;
import com.alex.supagwate.utils.Variables.cucmVersion;
import com.cisco.schemas.ast.soap.RISService70;
import com.cisco.schemas.ast.soap.RisPortType;


/**********************************
 * Class used to store the useful static methods
 * 
 * @author RATEL Alexandre
 **********************************/
public class UsefulMethod
	{
	
	/*****
	 * Method used to read the main config file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readMainConfigFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead("./"+fileName);
			
			listParams.add("config");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		
		}
	
	/***************************************
	 * Method used to get a specific value
	 * in the user preference XML File
	 ***************************************/
	public synchronized static String getTargetOption(String node) throws Exception
		{
		//We first seek in the configFile.xml
		for(String[] s : Variables.getTabConfig().get(0))
			{
			if(s[0].equals(node))return s[1];
			}
		
		/***********
		 * If this point is reached, the option looked for was not found
		 */
		throw new Exception("Option \""+node+"\" not found"); 
		}
	/*************************/
	
	
	
	/************************
	 * Check if java version
	 * is correct
	 ***********************/
	public static void checkJavaVersion()
		{
		try
			{
			String jVer = new String(System.getProperty("java.version"));
			Variables.getLogger().info("Detected JRE version : "+jVer);
			
			/*Need to use the config file value*/
			
			if(jVer.contains("1.6"))
				{
				/*
				if(Integer.parseInt(jVer.substring(6,8))<16)
					{
					Variables.getLogger().info("JRE version is not compatible. The application will now exit. system.exit(0)");
					System.exit(0);
					}*/
				}
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().info("ERROR : It has not been possible to detect JRE version",exc);
			}
		}
	/***********************/
	
	/*********************************************
	 * Used to get a file path
	 * @throws Exception 
	 *********************************************/
	public static String getFilePath(ArrayList<String> allowedExtensionList, String invitPhrase, String selectButton) throws Exception
		{
		JFileChooser fcSource = new JFileChooser();
		try
			{
			fcSource.setCurrentDirectory(new File(Variables.getMainDirectory()));
			
			fcSource.setDialogTitle(invitPhrase);
			
			EasyFileFilter myFilter = new EasyFileFilter(allowedExtensionList);
			fcSource.setFileFilter(myFilter);
			
			int resultat = fcSource.showDialog(fcSource, selectButton);
			if(resultat == fcSource.APPROVE_OPTION)
				{
				return fcSource.getSelectedFile().toString();
				}
			else
				{
				return null;
				}
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR : While fetching a file");
			}
		}
	
	/*********************************************
	 * Used to get a file path
	 * @throws Exception 
	 *********************************************/
	public static String getDirectoryPath(String baseDirectory, String invitPhrase, String selectButton) throws Exception
		{
		JFileChooser fcSource = new JFileChooser();
		try
			{
			fcSource.setCurrentDirectory(new File(baseDirectory));
			fcSource.setDialogTitle(LanguageManagement.getString("invitPhrase"));
			
			int resultat = fcSource.showDialog(fcSource, LanguageManagement.getString("selectButton"));
			if(resultat == fcSource.APPROVE_OPTION)
				{
				File mFile = new File(fcSource.getSelectedFile().toString());
				return mFile.getParent();
				}
			else
				{
				throw new Exception(LanguageManagement.getString("errordirectory"));
				}
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR : While fetching a file : "+exc.getMessage());
			}
		}
	
	
	/************
	 * Method used to read a simple configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String> readFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			
			return xMLGear.getResultList(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("ERROR : The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+fileName+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read a configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readFileTab(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the "+param+" file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+param+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read an advanced configuration file
	 * @throws Exception 
	 */
	public static ArrayList<ArrayList<String[][]>> readExtFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTabExt(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+exc.getMessage());
			}
		}
	
	/**
	 * Used to return the file content regarding the data source (xml file or database file)
	 * @throws Exception 
	 */
	public static String getFlatFileContent(String fileName) throws Exception, FileNotFoundException
		{
		return xMLReader.fileRead(Variables.getMainDirectory()+"/"+fileName);
		}
	
	/**
	 * Method used to initialize the database from
	 * a collection file
	 * @throws 
	 */
	public static void initDatabase() throws Exception
		{
		Variables.setDeviceTypeList(initDeviceTypeList());
		Variables.setCliProfileList(initCliProfileList());
		Variables.setCucmList(initCUCMList());
		Variables.setCountryList(initCountryList());
		Variables.setOfficeList(initOfficeList());
		}
	
	/************
	 * Method used to initialize the Device Type list from
	 * the xml file
	 */
	public static ArrayList<DeviceType> initDeviceTypeList() throws Exception
		{
		ArrayList<String> listParams = new ArrayList<String>();
		ArrayList<String[][]> result;
		ArrayList<ArrayList<String[][]>> extendedList;
		ArrayList<DeviceType> deviceTypeList = new ArrayList<DeviceType>();
		
		Variables.getLogger().info("Initializing the Device Type list from collection file");
		
		listParams.add("devices");
		listParams.add("device");
		result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getDeviceTypeListFileName()), listParams);
		extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getDeviceTypeListFileName()), listParams);
		
		for(int i=0; i<result.size(); i++)
			{
			try
				{
				String[][] tab = result.get(i);
				ArrayList<String[][]> tabE = extendedList.get(i);
				
				/**
				 * First we check for duplicates
				 */
				String DeviceTypeName = UsefulMethod.getItemByName("name", tab);
				boolean found = false;
				for(DeviceType dt : deviceTypeList)
					{
					if(dt.getName().equals(DeviceTypeName))
						{
						Variables.getLogger().debug("Duplicate found, do not adding the following device type : "+DeviceTypeName);
						found = true;
						break;
						}
					}
				if(found)continue;
				
				ArrayList<OneLine> howToConnect = new ArrayList<OneLine>();
				ArrayList<OneLine> howToSave = new ArrayList<OneLine>();
				ArrayList<OneLine> howToReboot = new ArrayList<OneLine>();
				
				for(int j=0; j<tab.length; j++)
					{
					if(tab[j][0].equals("howtoconnect"))
						{
						for(String[] s : tabE.get(j))
							{
							howToConnect.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tab[j][0].equals("howtosave"))
						{
						for(String[] s : tabE.get(j))
							{
							howToSave.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tab[j][0].equals("howtoreboot"))
						{
						for(String[] s : tabE.get(j))
							{
							howToReboot.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					}
				
				deviceTypeList.add(new DeviceType(UsefulMethod.getItemByName("name", tab),
						UsefulMethod.getItemByName("vendor", tab),
						howToConnect,
						howToSave,
						howToReboot));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("Failed to load a new Device Type : "+e.getMessage(), e);
				}
			}
		
		Variables.getLogger().debug(deviceTypeList.size()+" device type found");
		Variables.getLogger().debug("Device Type list initialization done");
		return deviceTypeList;
		}
	
	/************
	 * Method used to initialize the CUCM list from
	 * the xml file
	 */
	public static ArrayList<CUCM> initCUCMList() throws Exception
		{
		ArrayList<String> listParams = new ArrayList<String>();
		ArrayList<String[][]> result;
		ArrayList<CUCM> cucmList = new ArrayList<CUCM>();
		
		Variables.getLogger().info("Initializing the CUCM list from collection file");
		
		listParams.add("servers");
		listParams.add("cucm");
		result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getCucmListFileName()), listParams);
		
		for(String[][] tab : result)
			{
			try
				{
				/**
				 * First we check for duplicates
				 */
				String cucmName = UsefulMethod.getItemByName("name", tab);
				boolean found = false;
				for(CUCM c : cucmList)
					{
					if(c.getName().equals(cucmName))
						{
						Variables.getLogger().debug("Duplicate found, do not adding the following CUCM : "+cucmName);
						found = true;
						break;
						}
					}
				if(found)continue;
				
				cucmList.add(new CUCM(UsefulMethod.getItemByName("name", tab),
						convertStringToCucmVersion(UsefulMethod.getItemByName("version", tab)),
						UsefulMethod.getItemByName("ip", tab),
						UsefulMethod.getItemByName("axlport", tab),
						UsefulMethod.getItemByName("axlusername", tab),
						UsefulMethod.getItemByName("axlpassword", tab),
						UsefulMethod.getItemByName("risport", tab),
						UsefulMethod.getItemByName("risusername", tab),
						UsefulMethod.getItemByName("rispassword", tab),
						UsefulMethod.getItemByName("rismaxphonerequest", tab)));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("Failed to load a new CUCM : "+e.getMessage(), e);
				}
			}
		
		Variables.getLogger().debug(cucmList.size()+" cucm found");
		Variables.getLogger().debug("CUCM list initialization done");
		return cucmList;
		}
	
	/************
	 * Method used to initialize the country list from
	 * the xml file
	 */
	public static ArrayList<Country> initCountryList() throws Exception
		{
		ArrayList<String> listParams = new ArrayList<String>();
		ArrayList<String[][]> result;
		ArrayList<ArrayList<String[][]>> extendedList;
		ArrayList<Country> countryList = new ArrayList<Country>();
		
		Variables.getLogger().info("Initializing the Country list from collection file");
		
		listParams.add("countries");
		listParams.add("country");
		result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getCountryListFileName()), listParams);
		extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getCountryListFileName()), listParams);
		
		for(int i=0; i<result.size(); i++)
			{
			try
				{
				String[][] tab = result.get(i);
				ArrayList<String[][]> tabE = extendedList.get(i);
				
				/**
				 * First we check for duplicates
				 */
				String countryName = UsefulMethod.getItemByName("name", tab);
				boolean found = false;
				for(Country c : countryList)
					{
					if(c.getName().equals(countryName))
						{
						Variables.getLogger().debug("Duplicate found, do not adding the following country : "+countryName);
						found = true;
						break;
						}
					}
				if(found)continue;
				
				CUCM cucm = null;
				ArrayList<CustomSettings> settings = new ArrayList<CustomSettings>();
				
				for(CUCM c : Variables.getCucmList())
					{
					if(c.getName().equals(UsefulMethod.getItemByName("cucm", tab)))
						{
						cucm = c;
						break;
						}
					}
					
				for(int j=0; j<tab.length; j++)
					{
					if(tab[j][0].equals("customsettings"))
						{
						for(String[] s : tabE.get(j))
							{
							settings.add(new CustomSettings(s[0],s[1]));
							}
						}
					}
				
				countryList.add(new Country(UsefulMethod.getItemByName("name", tab),
						UsefulMethod.getItemByName("e164", tab),
						Variables.language.valueOf(UsefulMethod.getItemByName("language", tab)),
						cucm,
						settings));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("Failed to load a new Country : "+e.getMessage(), e);
				}
			}
		
		Variables.getLogger().debug(countryList.size()+" country found");
		Variables.getLogger().debug("Country list initialization done");
		return countryList;
		}
	
	
	/************
	 * Method used to initialize the office list from
	 * the xml file
	 * @throws Exception 
	 */
	public static ArrayList<Office> initOfficeList() throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		ArrayList<String[][]> result;
		ArrayList<ArrayList<String[][]>> extendedList;
		ArrayList<Office> myOfficeList = new ArrayList<Office>();
		
		try
			{
			Variables.getLogger().info("Initializing the office list from collection file");
			file = UsefulMethod.getFlatFileContent(Variables.getOfficeListFileName());
			
			listParams.add("offices");
			listParams.add("office");
			result = xMLGear.getResultListTab(file, listParams);
			extendedList = xMLGear.getResultListTabExt(file, listParams);
			
			for(int i=0; i<result.size(); i++)
				{
				try
					{
					String[][] tab = result.get(i);
					ArrayList<String[][]> tabE = extendedList.get(i);
					
					CUCM cucm = null;
					Country country = null;
					ArrayList<IPRange> voiceIpRange = new ArrayList<IPRange>();
					ArrayList<IPRange> dataIpRange = new ArrayList<IPRange>();
					ArrayList<DidRange> didRanges = new ArrayList<DidRange>();
					ArrayList<CustomSettings> settings = new ArrayList<CustomSettings>();
					
					/**
					 * We check for duplicates
					 */
					String officeName = UsefulMethod.getItemByName("name", tab);
					boolean found = false;
					for(Office o : myOfficeList)
						{
						if(o.getName().equals(officeName))
							{
							Variables.getLogger().debug("Duplicate found, do not adding the office : "+officeName);
							found = true;
							break;
							}
						}
					if(found)continue;
					
					for(int j=0; j<tab.length; j++)
						{
						if(tab[j][0].equals("cucm"))
							{
							cucm = getCUCM(tab[j][1]);
							}
						else if(tab[j][0].equals("country"))
							{
							country = getCountry(tab[j][1]);
							}
						else if(tab[j][0].equals("voiceipranges"))
							{
							for(String[] s : tabE.get(j))
								{
								voiceIpRange.add(new IPRange(s[1]));
								}
							}
						else if(tab[j][0].equals("dataipranges"))
							{
							for(String[] s : tabE.get(j))
								{
								dataIpRange.add(new IPRange(s[1]));
								}
							}
						else if(tab[j][0].equals("didranges"))
							{
							for(String[] s : tabE.get(j))
								{
								//Here we process the multiple did ranges and regex cases
								DidRange myRange = new DidRange(s[1]);
								ArrayList<DidRange> myLR = new ArrayList<DidRange>();
								if(myRange.getPattern() == null)
									{
									//So we have to process a regex based on a range
									for(String str : getRegexFromRange(myRange.getFirst(), myRange.getLast()))
										{
										Variables.getLogger().debug("Resulting regex for the range "+myRange.getFirst()+" > "+myRange.getLast()+" : "+str);
										myLR.add(new DidRange(str));
										}
									}
								else
									{
									myLR.add(myRange);
									}
									
								didRanges.addAll(myLR);
								}
							}
						else if(tab[j][0].equals("customsettings"))
							{
							for(String[] s : tabE.get(j))
								{
								settings.add(new CustomSettings(s[0],s[1]));
								}
							}
						}
					
					myOfficeList.add(new Office(officeName,
							UsefulMethod.getItemByName("templatename", tab),
							UsefulMethod.getItemByName("fullname", tab),
							UsefulMethod.getItemByName("internalprefix", tab),
							UsefulMethod.getItemByName("receptionnumber", tab),
							country,
							cucm,
							voiceIpRange,
							dataIpRange,
							didRanges,
							settings));
					}
				catch (Exception e)
					{
					Variables.getLogger().error("ERROR while initializing a new office : "+e.getMessage(), e);
					}
				}
			
			Variables.getLogger().debug(myOfficeList.size()+" office found");
			Variables.getLogger().debug("Office list initialization done");
			return myOfficeList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while loading the office file : "+exc.getMessage(), exc);
			}
		}
	
	
	/**
	 * Used to initialize CliProfile list
	 * @throws Exception 
	 */
	public static ArrayList<CliProfile> initCliProfileList() throws Exception
		{	
		try
			{
			Variables.getLogger().info("Initializing the CliProfile list from collection file");
			ArrayList<CliProfile> cliProfileList = new ArrayList<CliProfile>();
			ArrayList<String> listParams = new ArrayList<String>();
			listParams.add("profiles");
			listParams.add("profile");
			ArrayList<String[][]> result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName()), listParams);
			ArrayList<ArrayList<String[][]>> extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName()), listParams);
			
			for(int i=0; i<result.size(); i++)
				{
				try
					{
					String[][] tab = result.get(i);
					ArrayList<String[][]> tabE = extendedList.get(i);
					ArrayList<OneLine> cliList = new ArrayList<OneLine>();
					DeviceType deviceType = null;
					
					/**
					 * First we check for duplicates
					 */
					String cpName = UsefulMethod.getItemByName("name", tab);
					boolean found = false;
					for(CliProfile cp : cliProfileList)
						{
						if(cp.getName().equals(cpName))
							{
							Variables.getLogger().debug("Duplicate found, do not adding the CliProfile : "+cpName);
							found = true;
							break;
							}
						}
					if(found)continue;
					
					for(DeviceType dt : Variables.getDeviceTypeList())
						{
						if(dt.getName().equals(UsefulMethod.getItemByName("type", tab)))
							{
							deviceType = dt;
							break;
							}
						}
					
					for(int j=0; j<tab.length; j++)
						{
						if(tab[j][0].equals("config"))
							{
							for(String[] s : tabE.get(j))
								{
								cliList.add(new OneLine(s[1],cliType.valueOf(s[0])));
								}
							}
						}
					
					cliProfileList.add(new CliProfile(UsefulMethod.getItemByName("name", tab),
							deviceType,
							cliList,
							Integer.parseInt(UsefulMethod.getItemByName("defaultintercommandtimer", tab))));
					
					
					}
				catch (Exception e)
					{
					Variables.getLogger().error("ERROR while initializing a new cliProfile : "+e.getMessage(), e);
					}
				}
			
			Variables.getLogger().debug(cliProfileList.size()+ " cliProfiles found in the database");
			Variables.getLogger().debug("CliProfiles list initialization done");
			return cliProfileList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while initializing the CliProfile list : "+exc.getMessage(),exc);
			}
		}
	
	
	/******
	 * Method used to initialize the AXL Connection to the CUCM
	 */
	public static synchronized com.cisco.axlapiservice10.AXLPort initAXLConnectionToCUCM(CUCM host) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(host.getVersion().equals(cucmVersion.version105))
				{
				com.cisco.axlapiservice10.AXLAPIService axlService = new com.cisco.axlapiservice10.AXLAPIService();
				com.cisco.axlapiservice10.AXLPort axlPort = axlService.getAXLPort();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+host.getIp()+":"+host.getAxlport()+"/axl/";
				
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, host.getAxlusername());
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, host.getAxlpassword());
				
				Variables.getLogger().debug(host.getName()+" AXL WSDL Initialization done !");
				
				return axlPort;
				}
			else
				{
				throw new Exception("AXL unsupported version");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while initializing AXL CUCM connection : "+e.getMessage(),e);
			host.setReachable(false);
			throw e;
			}
		}
	
	/******
	 * Method used to initialize the AXL Connection to the CUCM
	 */
	public static synchronized RisPortType initRISConnectionToCUCM(CUCM host) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(host.getVersion().equals(cucmVersion.version105))
				{
				RISService70 ris = new RISService70();
				RisPortType risPort = ris.getRisPort70();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+host.getIp()+":"+host.getRisport()+"/realtimeservice2/services/RISService70";
				
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, host.getRisusername());
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, host.getRispassword());
				
				Variables.getLogger().debug(host.getName()+" RIS connection Initialization done !");
				
				return risPort;
				}
			else
				{
				throw new Exception("RIS unsupported version");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while initializing RIS CUCM connection : "+e.getMessage(),e);
			host.setReachable(false);
			throw e;
			}
		}
	
	/**
	 * Method used when the application failed to 
	 * initialize
	 */
	public static void failedToInit(Exception exc)
		{
		Variables.getLogger().error(exc.getMessage(),exc);
		Variables.getLogger().error("Application failed to init : System.exit(0)");
		System.exit(0);
		}
	
	/**
	 * Initialization of the internal variables from
	 * what we read in the configuration file
	 * @throws Exception 
	 */
	public static void initInternalVariables() throws Exception
		{
		/***********
		 * Logger
		 */
		String level = UsefulMethod.getTargetOption("log4j");
		if(level.compareTo("DEBUG")==0)
			{
			Variables.getLogger().setLevel(Level.DEBUG);
			}
		else if (level.compareTo("INFO")==0)
			{
			Variables.getLogger().setLevel(Level.INFO);
			}
		else if (level.compareTo("ERROR")==0)
			{
			Variables.getLogger().setLevel(Level.ERROR);
			}
		else
			{
			//Default level is INFO
			Variables.getLogger().setLevel(Level.INFO);
			}
		Variables.getLogger().info("Log level found in the configuration file : "+Variables.getLogger().getLevel().toString());
		/*************/
		
		/************
		 * Etc...
		 */
		//If needed, just write it here
		/*************/
		}
	
	/**
	 * Method which convert a string into cucmAXLVersion
	 */
	public static cucmVersion convertStringToCucmVersion(String version)
		{
		if(version.contains("80"))
			{
			return cucmVersion.version80;
			}
		else if(version.contains("85"))
			{
			return cucmVersion.version85;
			}
		else if(version.contains("105"))
			{
			return cucmVersion.version105;
			}
		else if(version.contains("110"))
			{
			return cucmVersion.version110;
			}
		else if(version.contains("115"))
			{
			return cucmVersion.version115;
			}
		else if(version.contains("120"))
			{
			return cucmVersion.version120;
			}
		else if(version.contains("125"))
			{
			return cucmVersion.version125;
			}
		else
			{
			//Default : 10.5
			return cucmVersion.version105;
			}
		}
	
	
	/**************
	 * Method aims to get a template item value by giving its name
	 * @throws Exception 
	 *************/
	public static String getItemByName(String name, String[][] itemDetails) throws Exception
		{
		for(int i=0; i<itemDetails.length; i++)
			{
			if(itemDetails[i][0].equals(name))
				{
				return itemDetails[i][1];
				}
			}
		//throw new Exception("Item not found : "+name);
		Variables.getLogger().debug("Item not found : "+name);
		return "";
		}
	
	/**********************************************************
	 * Method used to disable security in order to accept any
	 * certificate without trusting it
	 */
	public static void disableSecurity()
		{
		try
        	{
            X509TrustManager xtm = new HttpsTrustManager();
            TrustManager[] mytm = { xtm };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, mytm, null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            	{
                public boolean verify(String hostname, SSLSession session)
                	{
                    return true;
                	}
            	}
            );
        	}
        catch (Exception e)
        	{
            e.printStackTrace();
        	}
		}
	
	/**
	 * Method used to convert a long network mask into a short one
	 * 
	 * For instance :
	 * Convert 255.255.255.0 into 24
	 * Convert 255.255.240.0 into 20
	 */
	public static String convertlongMaskToShortOne(String mask)
		{
		//We check if mask is a long or a CIDR one
		if(!InetAddressValidator.getInstance().isValidInet4Address(mask))return mask;
		
		SubnetUtils subnet = new SubnetUtils("192.168.1.0", mask.trim());
		return subnet.getInfo().getCidrSignature().split("/")[1];
		}
	
	/**
	 * Method used to convert a short network mask into a long one
	 * 
	 * For instance :
	 * Convert 24 into 255.255.255.0
	 * Convert 20 into 255.255.240.0
	 */
	public static String convertShortMaskToLongOne(String mask)
		{
		//We check if mask is a long or a CIDR one
		if(mask.length()>2)return mask;
		
		SubnetUtils subnet = new SubnetUtils("192.168.1.0/"+mask);
		return subnet.getInfo().getNetmask();
		}
	
	/***************
	 * Method used to convert a profile name from the collection file
	 * into a profile name expected by the CSV
	 * @throws Exception 
	 */
	public static synchronized String findCodecBandwidth(String codec) throws Exception
		{
		if(codec.equals("G.711"))
			{
			return "64";
			}
		else if(codec.equals("G.729"))
			{
			return "10";
			}
		
		throw new Exception("Profile "+codec+" not found");
		}
	
	
	/********************************************
	 * Method used to init the class eMailsender
	 * @throws Exception 
	 ********************************************/
	public synchronized static void initEMailServer() throws Exception
		{
		Variables.seteMSender(new eMailSender(UsefulMethod.getTargetOption("smtpemailport"),
				 UsefulMethod.getTargetOption("smtpemailprotocol"),
				 UsefulMethod.getTargetOption("smtpemailserver"),
				 UsefulMethod.getTargetOption("smtpemail"),
				 UsefulMethod.getTargetOption("smtpemailpassword")));
		}
	
	/**
	 * Method used to send an email to the admin group
	 */
	public synchronized static void sendEmailToTheAdminList(String subject, String content)
		{
		try
			{
			Variables.getLogger().debug("Sending emails to the admin group");
			String adminEmails = UsefulMethod.getTargetOption("smtpemailreceiver");
			String eMailDesc = "Multiple email sending";
			
			String[] adminList = adminEmails.split(";");
			for(String s : adminList)
				{
				Variables.getLogger().debug("Sending to "+s);
				try
					{
					Variables.geteMSender().send(s,
							subject,
							content,
							eMailDesc);
					Variables.getLogger().debug("Email sent for "+s);
					}
				catch (Exception e)
					{
					Variables.getLogger().debug("Failed to send email to "+s+" : "+e.getMessage());
					}
				}
			
			Variables.getLogger().debug("Email sent to the admin group");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to send emails to the admin list : "+e.getMessage(),e);
			}
		}
	
	
	/**
	 * Method used to find the file name from a file path
	 * For intance :
	 * C:\JAVAELILEX\YUZA\Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 * gives :
	 * Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 */
	public static String extractFileName(String fullFilePath)
		{
		String[] tab =  fullFilePath.split("\\\\");
		return tab[tab.length-1];
		}
	
	/***
	 * Method used to get the AXL version from the CUCM
	 * We contact the CUCM using a very basic request and therefore get the version
	 * @throws Exception 
	 */
	public static cucmVersion getAXLVersionFromTheCUCM() throws Exception
		{
		/**
		 * In this method version we just read the version from the configuration file
		 * This has to be improved to match the method description
		 **/
		cucmVersion AXLVersion;
		
		AXLVersion = UsefulMethod.convertStringToCucmVersion("version"+getTargetOption("axlversion"));
		
		return AXLVersion;
		}
	
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(String s)
		{
		if((s == null) || (s.equals("")))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(ArrayList<String> as)
		{
		if((as == null) || (as.size() == 0))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	
	/******
	 * Method used to determine if the fault description means
	 * that the item was not found or something else
	 * If it is not found we return true
	 * For any other reason we return false
	 * @param faultDesc
	 * @return
	 */
	public static boolean itemNotFoundInTheCUCM(String faultDesc)
		{
		ArrayList<String> faultDescList = new ArrayList<String>();
		faultDescList.add("was not found");
		for(String s : faultDescList)
			{
			if(faultDesc.contains(s))return true;
			}
		
		return false;
		}
	
	/**
	 * Used to check if the IP is in the subnet
	 */
	public static boolean isIPIncludedInThisSubnet(IPRange range, String ip)
		{
		try
			{
			SubnetInfo subnet = new SubnetUtils(range.getCIDRFormat()).getInfo();
			return subnet.isInRange(ip);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			}
		return false;
		}
	
	/**
	 * To make a user authenticate by the CUCM 
	 */
	public static boolean doAuthenticate(CUCM cucm, String userID, String password)
		{
		try
			{
			com.cisco.axl.api._10.DoAuthenticateUserReq req = new com.cisco.axl.api._10.DoAuthenticateUserReq();
			
			req.setUserid(userID);
			req.setPassword(password);
			
			com.cisco.axl.api._10.DoAuthenticateUserRes resp = cucm.getAXLConnectionToCUCMV105().doAuthenticateUser(req);
			
			return Boolean.parseBoolean(resp.getReturn().getUserAuthenticated());
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while authenticating user "+userID+" : "+e.getMessage(),e);
			}
		
		return false;
		}
	
	public static cliProtocol getProtocolType(String protocol) throws Exception
		{
		for(cliProtocol p : cliProtocol.values())
			{
			if(protocol.toLowerCase().replaceAll(" ", "").equals(p.name()))return p;
			}
		
		throw new Exception("No cliProtocol found for protocol : "+protocol);
		}
	
	/**
	 * Method used to convert a range of DID number into
	 * An arraylist of regex
	 * @param first
	 * @param end
	 * @return
	 */
	public static ArrayList<String> getRegexFromRange(String firstNumber, String lastNumber)
		{
		int start = Integer.parseInt(firstNumber);
		int end = Integer.parseInt(lastNumber);
		
		final LinkedList<Range> left = leftBounds(start, end);
		final Range lastLeft = left.removeLast();
		final LinkedList<Range> right = rightBounds(lastLeft.getStart(), end);
		final Range firstRight = right.removeFirst();

		LinkedList<Range> merged = new LinkedList<>();
		merged.addAll(left);
		if (!lastLeft.overlaps(firstRight))
			{
			merged.add(lastLeft);
			merged.add(firstRight);
			}
		else
			{
			merged.add(Range.join(lastLeft, firstRight));
			}
		merged.addAll(right);

		//merged.stream().map(Range::toRegex).forEach(System.out::println);
		
		ArrayList<String> list = new ArrayList<String>();
		for(Range r : merged)
			{
			list.add(r.toRegex());
			}
		
		return list;
		}
	
	/**
	 * Used to by getRegexFromRange
	 * @param start
	 * @param end
	 * @return
	 */
	private static LinkedList<Range> leftBounds(int start, int end)
		{
	    final LinkedList<Range> result = new LinkedList<>();
	    while (start < end)
	    	{
	        final Range range = Range.fromStart(start);
	        result.add(range);
	        start = range.getEnd()+1;
	    	}
	    return result;
		}

	/**
	 * Used to by getRegexFromRange
	 * @param start
	 * @param end
	 * @return
	 */
	private static LinkedList<Range> rightBounds(int start, int end)
		{
	    final LinkedList<Range> result = new LinkedList<>();
	    while (start < end)
	    	{
	        final Range range = Range.fromEnd(end);
	        result.add(range);
	        end = range.getStart()-1;
	    	}
	    Collections.reverse(result);
	    return result;
		}
	
	/**
	 * Used to get an office from the list
	 * @throws Exception 
	 */
	public static Office getOffice(String officeName) throws Exception
		{
		for(Office o : Variables.getOfficeList())
			{
			if(o.getName().equals(officeName))return o;
			}
		
		throw new Exception("The given office was not found : "+officeName);
		}
	
	/**
	 * Used to get an office from the list
	 * @throws Exception 
	 */
	public static Office getOffice(String officeName, boolean throwExceptionIfNotFound) throws Exception
		{
		for(Office o : Variables.getOfficeList())
			{
			if(o.getName().equals(officeName))return o;
			}
		
		if(throwExceptionIfNotFound)
			{
			throw new Exception("The given office was not found : "+officeName);
			}
		else
			{
			return null;
			}
		}
	
	/**
	 * Used to get a deviceType from the list
	 */
	public static DeviceType getDeviceType(String deviceTypeName) throws Exception
		{
		for(DeviceType dt : Variables.getDeviceTypeList())
			{
			if(dt.getName().equals(deviceTypeName))return dt;
			}
		
		throw new Exception("The given deviceType was not found : "+deviceTypeName);
		}
	
	/**
	 * Used to get a CliProfile from the list
	 */
	public static CliProfile getCliprofile(String cliProfileName) throws Exception
		{
		for(CliProfile cp : Variables.getCliProfileList())
			{
			if(cp.getName().equals(cliProfileName))return cp;
			}
		
		throw new Exception("The given cliprofile was not found : "+cliProfileName);
		}
	
	/**
	 * Used to get a CUCM from the list
	 * @throws Exception 
	 */
	public static CUCM getCUCM(String CUCMName) throws Exception
		{
		for(CUCM cucm : Variables.getCucmList())
			{
			if(cucm.getName().equals(CUCMName))return cucm;
			}
		
		throw new Exception("The given CUCM was not found : "+CUCMName);
		}
	
	/**
	 * Used to get a Country from the list
	 * @throws Exception 
	 */
	public static Country getCountry(String CountryName) throws Exception
		{
		for(Country c : Variables.getCountryList())
			{
			if(c.getName().equals(CountryName))return c;
			}
		
		throw new Exception("The given Country was not found : "+CountryName);
		}
	
	/**
	 * To get a cligetOutput from the associated device name
	 */
	public static CliGetOutput getCliGetOutput(Device d)
		{
		for(CliGetOutput cgo : Variables.getCliGetOutputList())
			{
			if(cgo.getDevice().getInfo().equals(d.getInfo()))return cgo;
			}
		
		return null;
		}
	
	/**
	 * To get a FTPTransfer searching by IP address
	 * @throws Exception 
	 */
	public static FTPTransfer getFTPTransfer(String deviceIP) throws Exception
		{
		for(FTPTransfer t : Variables.getFtpTransferList())
			{
			if(t.getDevice().getIp().equals(deviceIP))return t;
			}
		
		throw new Exception("The given FTPTransfer was not found : "+deviceIP);
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

