package com.alex.supagwate.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.alex.supagwate.cli.CliProfile;
import com.alex.supagwate.device.DeviceType;
import com.alex.supagwate.gui.MainWindow;
import com.alex.supagwate.office.CUCM;
import com.alex.supagwate.office.Country;
import com.alex.supagwate.office.Office;

/**********************************
 * Used to store static variables
 * 
 * @author RATEL Alexandre
 **********************************/
public class Variables
	{
	/**
	 * Variables
	 */
	//Enum
	/********************************************
	 * cucmAXLVersion :
	 * Is used to set the cucm AXL version used for the injection
	 ***************************************/
	public enum cucmVersion
		{
		version80,
		version85,
		version90,
		version91,
		version100,
		version105,
		version110,
		version115,
		version120,
		version125
		};
	
	/********************************************
	 * actionType :
	 * Is used to set the type of action is going to do a 
	 ***************************************/
	public enum actionType
		{
		set,
		get,
		reset
		};
		
	public enum reachableStatus
		{
		reachable,
		unreachable,
		unknown
		};
		
	/********************************************
	 * statusType :
	 * Is used to set the status of a request as followed :
	 * - init : the request has to be built
	 * - waiting : The request is ready to be injected. We gonna reach this status after the request has been built or has been deleted
	 * - processing : The injection or the deletion of the request is currently under progress
	 * - disabled : The request has not to be injected
	 * - injected : The request has been injected with success
	 * - error : Something went wrong and an exception has been thrown
	 ***************************************/
	public enum statusType
		{
		init,
		waiting,
		processing,
		done,
		disabled,
		error
		};
	
	//Misc
	private static String softwareName;
	private static String softwareVersion;
	private static Logger logger;
	private static ArrayList<String[][]> tabConfig;
	private static ArrayList<Office> officeList;
	private static ArrayList<CUCM> cucmList;
	private static ArrayList<Country> countryList;
	private static ArrayList<DeviceType> deviceTypeList;
	private static eMailSender eMSender;
	private static String mainDirectory;
	private static String configFileName;
	private static String matcherFileName;
	private static String officeListFileName;
	private static String cucmListFileName;
	private static String countryListFileName;
	private static String deviceTypeListFileName;
	private static String cliProfileListFileName;
	private static ArrayList<String> matcherList;
	private static String collectionFileName;
	private static Workbook myWorkbook;
	private static MainWindow mainWindow;
	private static JFrame myWindow;
	private static ArrayList<String> allowedItemsToProcess;
	
	//Langage management
	public enum language{english,french};
	private static String languageFileName;
	private static ArrayList<ArrayList<String[][]>> languageContentList;
    
    //CLI
    private static ArrayList<CliProfile> cliProfileList;
    
    /**************
     * Constructor
     **************/
	public Variables()
		{
		mainDirectory = ".";
		configFileName = "configFile.xml";
		matcherFileName = "matchers.xml";
		officeListFileName = "officeList.xml";
		cucmListFileName = "cucmList.xml";
		countryListFileName = "countryList.xml";
		deviceTypeListFileName = "deviceTypeList.xml";
		cliProfileListFileName = "cliProfileList.xml";
		languageFileName = "languages.xml";
		}

	/**
	 * Used to close the workbook properly
	 * @throws IOException 
	 */
	public static void closeWorkbook() throws IOException
		{
		if(myWorkbook != null)
			{
			myWorkbook.close();
			setMyWorkbook(null);//We reset the workbook
			Variables.getLogger().info("Workbook closed");
			}
		}
	
	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static ArrayList<String[][]> getTabConfig()
		{
		return tabConfig;
		}

	public static void setTabConfig(ArrayList<String[][]> tabConfig)
		{
		Variables.tabConfig = tabConfig;
		}

	public static ArrayList<Office> getOfficeList()
		{
		return officeList;
		}

	public static void setOfficeList(ArrayList<Office> officeList)
		{
		Variables.officeList = officeList;
		}

	public static ArrayList<CUCM> getCucmList()
		{
		return cucmList;
		}

	public static void setCucmList(ArrayList<CUCM> cucmList)
		{
		Variables.cucmList = cucmList;
		}

	public static ArrayList<Country> getCountryList()
		{
		return countryList;
		}

	public static void setCountryList(ArrayList<Country> countryList)
		{
		Variables.countryList = countryList;
		}

	public static ArrayList<DeviceType> getDeviceTypeList()
		{
		return deviceTypeList;
		}

	public static void setDeviceTypeList(ArrayList<DeviceType> deviceTypeList)
		{
		Variables.deviceTypeList = deviceTypeList;
		}

	public static eMailSender geteMSender()
		{
		return eMSender;
		}

	public static void seteMSender(eMailSender eMSender)
		{
		Variables.eMSender = eMSender;
		}

	public static String getMainDirectory()
		{
		return mainDirectory;
		}

	public static void setMainDirectory(String mainDirectory)
		{
		Variables.mainDirectory = mainDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static String getMatcherFileName()
		{
		return matcherFileName;
		}

	public static void setMatcherFileName(String matcherFileName)
		{
		Variables.matcherFileName = matcherFileName;
		}

	public static String getOfficeListFileName()
		{
		return officeListFileName;
		}

	public static void setOfficeListFileName(String officeListFileName)
		{
		Variables.officeListFileName = officeListFileName;
		}

	public static String getCucmListFileName()
		{
		return cucmListFileName;
		}

	public static void setCucmListFileName(String cucmListFileName)
		{
		Variables.cucmListFileName = cucmListFileName;
		}

	public static String getCountryListFileName()
		{
		return countryListFileName;
		}

	public static void setCountryListFileName(String countryListFileName)
		{
		Variables.countryListFileName = countryListFileName;
		}

	public static String getDeviceTypeListFileName()
		{
		return deviceTypeListFileName;
		}

	public static void setDeviceTypeListFileName(String deviceTypeListFileName)
		{
		Variables.deviceTypeListFileName = deviceTypeListFileName;
		}

	public static String getCliProfileListFileName()
		{
		return cliProfileListFileName;
		}

	public static void setCliProfileListFileName(String cliProfileListFileName)
		{
		Variables.cliProfileListFileName = cliProfileListFileName;
		}

	public static ArrayList<String> getMatcherList() throws Exception
		{
		if(matcherList == null)
			{
			Variables.getLogger().debug("Initialisation of matcherList");
			Variables.setMatcherList(UsefulMethod.readFile("matchers", Variables.getMatcherFileName()));
			}
		return matcherList;
		}

	public static void setMatcherList(ArrayList<String> matcherList)
		{
		Variables.matcherList = matcherList;
		}

	public static String getCollectionFileName() throws Exception
		{
		if(collectionFileName == null)
			{
			Variables.getLogger().debug("Initialisation of collectionFileName");
			
			ArrayList<String> allowedExtensionList = new ArrayList<String>();
			allowedExtensionList.add("xls");
			allowedExtensionList.add("xlsx");
			
			collectionFileName = UsefulMethod.getFilePath(
					allowedExtensionList,
					LanguageManagement.getString("collectionfilefetch"),
					LanguageManagement.getString("selectbutton"));
			}
		
		return collectionFileName;
		}

	public static void setCollectionFileName(String collectionFileName)
		{
		Variables.collectionFileName = collectionFileName;
		}

	public static Workbook getMyWorkbook() throws Exception
		{
		if(myWorkbook == null)
			{
			Variables.getLogger().debug("Initialisation of myWorkbook");
			myWorkbook = WorkbookFactory.create(new FileInputStream(Variables.getCollectionFileName()));
			}
		return myWorkbook;
		}

	public static void setMyWorkbook(Workbook myWorkbook)
		{
		Variables.myWorkbook = myWorkbook;
		}

	public static String getLanguageFileName()
		{
		return languageFileName;
		}

	public static void setLanguageFileName(String languageFileName)
		{
		Variables.languageFileName = languageFileName;
		}

	public static ArrayList<ArrayList<String[][]>> getLanguageContentList() throws Exception
		{
		if(languageContentList == null)
			{
			Variables.getLogger().debug("Initialisation of languageContentList");
			Variables.setLanguageContentList(UsefulMethod.readExtFile("language", Variables.getLanguageFileName()));
			}
		
		return languageContentList;
		}

	public static void setLanguageContentList(ArrayList<ArrayList<String[][]>> languageContentList)
		{
		Variables.languageContentList = languageContentList;
		}

	public static ArrayList<CliProfile> getCliProfileList()
		{
		return cliProfileList;
		}

	public static void setCliProfileList(ArrayList<CliProfile> cliProfileList)
		{
		Variables.cliProfileList = cliProfileList;
		}

	public static MainWindow getMainWindow()
		{
		return mainWindow;
		}

	public static void setMainWindow(MainWindow mainWindow)
		{
		Variables.mainWindow = mainWindow;
		}

	public static JFrame getMyWindow()
		{
		return myWindow;
		}

	public static void setMyWindow(JFrame myWindow)
		{
		Variables.myWindow = myWindow;
		}

	public static ArrayList<String> getAllowedItemsToProcess()
		{
		return allowedItemsToProcess;
		}

	public static void setAllowedItemsToProcess(ArrayList<String> allowedItemsToProcess)
		{
		Variables.allowedItemsToProcess = allowedItemsToProcess;
		}

	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

