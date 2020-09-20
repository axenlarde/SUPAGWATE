package com.alex.supagwate.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
import com.alex.supagwate.office.DidRange;
import com.alex.supagwate.office.IPRange;
import com.alex.supagwate.office.Office;
import com.alex.supagwate.office.Country;
import com.alex.supagwate.office.CustomSettings;
import com.alex.supagwate.office.Range;
import com.alex.supagwate.upgrade.UpgradeData;


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
		String flatFileContent = UsefulMethod.getFlatFileContent(Variables.getDeviceTypeListFileName());
		result = xMLGear.getResultListTab(flatFileContent, listParams);
		extendedList = xMLGear.getResultListTabExt(flatFileContent, listParams);
		
		for(int i=0; i<result.size(); i++)
			{
			try
				{
				String[][] tab = result.get(i);
				
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
				
				ArrayList<String[][]> tabE = extendedList.get(i);
				
				/**
				 * We manage the upgrade data
				 */
				listParams.add("upgrade");
				ArrayList<String[][]> resultUpgrade = xMLGear.getResultListTab(flatFileContent, listParams);
				ArrayList<ArrayList<String[][]>> extendedListUpgrade = xMLGear.getResultListTabExt(flatFileContent, listParams);
				
				ArrayList<OneLine> checkcurrentversion = new ArrayList<OneLine>();
				ArrayList<OneLine> checkdiskspace = new ArrayList<OneLine>();
				ArrayList<OneLine> checkexistingfile = new ArrayList<OneLine>();
				ArrayList<OneLine> filedelete = new ArrayList<OneLine>();
				ArrayList<OneLine> startupgrade = new ArrayList<OneLine>();
				ArrayList<OneLine> checkfile = new ArrayList<OneLine>();
				ArrayList<OneLine> boot = new ArrayList<OneLine>();
				
				String[][] tabUpgrade = resultUpgrade.get(0);
				ArrayList<String[][]> tabEUpgrade = extendedListUpgrade.get(0);
				
				for(int j=0; j<tabUpgrade.length; j++)
					{
					if(tabUpgrade[j][0].equals("checkcurrentversion"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							checkcurrentversion.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("checkdiskspace"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							checkdiskspace.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("checkexistingfile"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							checkexistingfile.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("filedelete"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							filedelete.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("startupgrade"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							startupgrade.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("checkfile"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							checkfile.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tabUpgrade[j][0].equals("boot"))
						{
						for(String[] s : tabEUpgrade.get(j))
							{
							boot.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					}
				
				UpgradeData ud = new UpgradeData(UsefulMethod.getItemByName("upgradefile", tabUpgrade),
						checkcurrentversion,
						checkdiskspace,
						checkexistingfile,
						filedelete,
						startupgrade,
						checkfile,
						boot);
				
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
						ud,
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
		String flatFileContent = UsefulMethod.getFlatFileContent(Variables.getCountryListFileName());
		result = xMLGear.getResultListTab(flatFileContent, listParams);
		extendedList = xMLGear.getResultListTabExt(flatFileContent, listParams);
		
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
				
				ArrayList<CustomSettings> settings = new ArrayList<CustomSettings>();
				
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
						if(tab[j][0].equals("country"))
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
			String flatFileContent = UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName());
			ArrayList<String[][]> result = xMLGear.getResultListTab(flatFileContent, listParams);
			ArrayList<ArrayList<String[][]>> extendedList = xMLGear.getResultListTabExt(flatFileContent, listParams);
			
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
					
					boolean devicTypeNotFound = true;
					String deviceTypeS = UsefulMethod.getItemByName("type", tab);
					for(DeviceType dt : Variables.getDeviceTypeList())
						{
						if(dt.getName().equals(deviceTypeS))
							{
							deviceType = dt;
							devicTypeNotFound = false;
							break;
							}
						}
					
					if(devicTypeNotFound)
						{
						Variables.getLogger().debug("The following device type was not found, so we do not add the corresponding CliProfile : "+deviceTypeS);
						continue;
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
	public static FTPTransfer getFTPTransfer(String deviceIP)
		{
		for(FTPTransfer t : Variables.getFtpTransferList())
			{
			if(t.getDevice().getIp().equals(deviceIP))return t;
			}
		
		return null;
		}
	
	public static String getMyIP(int interfaceID)
		{
		try
			{
			return Collections.list(Collections.list(NetworkInterface.getNetworkInterfaces()).get(interfaceID).getInetAddresses()).get(0).getHostAddress();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while looking for my IP : "+e.getMessage(),e);
			}
		return "";//Not found
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

