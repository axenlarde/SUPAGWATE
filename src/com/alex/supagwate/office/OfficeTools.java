package com.alex.supagwate.office;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;

/**
 * Static office method
 * 
 * @author Alexandre RATEL
 */
public class OfficeTools
	{
	
	
	/**
	 * Used to import offices from a csv file
	 */
	public static void importOfficeList()
		{
		try
			{
			Variables.getLogger().info("Importing office list from a csv file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String rangeSplitter = UsefulMethod.getTargetOption("rangesplitter");
			boolean replace = false;
			String cr = "\r\n";
			
			Variables.getLogger().debug("Get file path");
			String currentUsage = " :\r\nname;fullname;country;templatename;internalprefix;receptionnumber;cucm;voiceipranges (comma separated);dataipranges (comma separated);didranges (comma separated)";
			JOptionPane.showMessageDialog(null,LanguageManagement.getString("toimportoffice")+currentUsage+"\r\n"+LanguageManagement.getString("noticeimportoffice"),LanguageManagement.getString("officemngt"),JOptionPane.INFORMATION_MESSAGE);
			
			//We ask the user if we should append or replace the new content
			String message = LanguageManagement.getString("appendorreplaceoffice");
			String[] options = new String[]{LanguageManagement.getString("replace"),LanguageManagement.getString("append")}; 
			replace = (JOptionPane.showOptionDialog(null, message, LanguageManagement.getString("officemngt"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]) == 1)?false:true;
			
			ArrayList<String> allowedExtensionList = new ArrayList<String>();
			allowedExtensionList.add("csv");
			allowedExtensionList.add("txt");
			
			String officeListFileName = UsefulMethod.getFilePath(
					allowedExtensionList,
					LanguageManagement.getString("collectionfilefetch"),
					LanguageManagement.getString("selectbutton"));
			
			Variables.getLogger().debug("File to use for the import : "+officeListFileName);
			
			FileReader csvFile = new FileReader(officeListFileName);
			BufferedReader csvTampon = new BufferedReader(csvFile);
			String[] cells = csvTampon.readLine().split(splitter);
			ArrayList<Office> tempOList = new ArrayList<Office>();
			int count = 0;
			
			while(true)
				{
				String line = csvTampon.readLine();
				if(line != null)
					{
					String[] values = line.split(splitter);
					
					//We first check if the office already exists
					String officeName = values[getIndexColumn(cells, "name")];
					
					if(replace || (UsefulMethod.getOffice(officeName, false) == null))//Doesn't exist
						{
						ArrayList<IPRange> voiceIpRange = new ArrayList<IPRange>();
						ArrayList<IPRange> dataIpRange = new ArrayList<IPRange>();
						ArrayList<DidRange> didRanges = new ArrayList<DidRange>();
						ArrayList<CustomSettings> settings = new ArrayList<CustomSettings>();
						
						//Voice IP Range
						String[] temp = values[getIndexColumn(cells, "voiceipranges")].split(rangeSplitter);
						for(String range : temp)
							{
							voiceIpRange.add(new IPRange(range));
							}
						
						//Data IP Ranges
						temp = values[getIndexColumn(cells, "dataipranges")].split(rangeSplitter);
						for(String range : temp)
							{
							dataIpRange.add(new IPRange(range));
							}
						
						//DID Ranges
						int lastIndex = getIndexColumn(cells, "didranges");
						temp = values[lastIndex].split(rangeSplitter);
						for(String range : temp)
							{
							didRanges.add(new DidRange(range));
							}
						
						/**
						 * Here we look for custom settings
						 * 
						 * Anything after the last normal value is considered as a custom one
						 */
						if(values.length>lastIndex+1)
							{
							for(int i=lastIndex+1; i<values.length; i++)
								{
								settings.add(new CustomSettings(cells[i], values[i]));
								}
							}
						
						tempOList.add(new Office(values[getIndexColumn(cells, "name")],
								values[getIndexColumn(cells, "templatename")],
								values[getIndexColumn(cells, "fullname")],
								values[getIndexColumn(cells, "internalprefix")],
								values[getIndexColumn(cells, "receptionnumber")],
								UsefulMethod.getCountry(values[getIndexColumn(cells, "country")]),
								voiceIpRange,
								dataIpRange,
								didRanges,
								settings));
						
						Variables.getLogger().debug("New office found : "+officeName);
						count++;
						}
					}
				else
					{
					break;
					}
				}
			
			/**
			 * Now we add the new offices to the existing ones
			 * 
			 * If the user ask to replace the list we do
			 */
			Variables.getLogger().debug("total new office found : "+count);
			if(replace)
				{
				Variables.getLogger().debug("The user ask to replace the existing office list");
				Variables.setOfficeList(tempOList);
				}
			else
				{
				Variables.getLogger().debug("The user ask to keep the existing offices and append the new ones");
				Variables.getOfficeList().addAll(tempOList);
				}
			Variables.getLogger().debug("Total office after importation task : "+Variables.getOfficeList().size());
			/********/
			
			/**
			 * Then we write all the offices in the database file
			 */
			Variables.getLogger().debug("Now writing down office list");
			BufferedWriter xmlBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+Variables.getOfficeListFileName()), false));
			xmlBuffer.write("<!--"+cr);
			xmlBuffer.write("office list"+cr);
			xmlBuffer.write("-->"+cr);
			xmlBuffer.write("<xml>"+cr);
			xmlBuffer.write("	<offices>"+cr);
			
			for(Office o : Variables.getOfficeList())
				{
				xmlBuffer.write("		<office>"+cr);
				xmlBuffer.write("			<name>"+o.getName()+"</name>"+cr);
				xmlBuffer.write("			<fullname>"+o.getFullname()+"</fullname>"+cr);
				xmlBuffer.write("			<country>"+o.getCountry().getName()+"</country>"+cr);
				xmlBuffer.write("			<templatename>"+o.getTemplatename()+"</templatename>"+cr);
				xmlBuffer.write("			<internalprefix>"+o.getInternalprefix()+"</internalprefix>"+cr);
				xmlBuffer.write("			<receptionnumber>"+o.getReceptionnumber()+"</receptionnumber>"+cr);
				
				//Voice IP Ranges
				xmlBuffer.write("			<voiceipranges>"+cr);
				for(IPRange range : o.getVoiceIpRange())
					{
					xmlBuffer.write("				<range>"+range.getCIDRFormat()+"</range>"+cr);
					}
				xmlBuffer.write("			</voiceipranges>"+cr);
				
				//Data IP Ranges
				xmlBuffer.write("			<dataipranges>"+cr);
				for(IPRange range : o.getDataIpRange())
					{
					xmlBuffer.write("				<range>"+range.getCIDRFormat()+"</range>"+cr);
					}
				xmlBuffer.write("			</dataipranges>"+cr);
				
				//DID Ranges
				xmlBuffer.write("			<didranges>"+cr);
				for(DidRange range : o.getDidRanges())
					{
					xmlBuffer.write("				<range>"+range.getPattern()+"</range>"+cr);
					}
				xmlBuffer.write("			</didranges>"+cr);
				
				//Custom settings
				xmlBuffer.write("			<customsettings>"+cr);
				for(CustomSettings cs : o.getSettings())
					{
					xmlBuffer.write("				<"+cs.getTargetname()+">"+cs.getValue()+"</"+cs.getTargetname()+">"+cr);
					}
				xmlBuffer.write("			</customsettings>"+cr);
				
				xmlBuffer.write("		</office>"+cr);
				}
			
			xmlBuffer.write("	</offices>"+cr);
			xmlBuffer.write("</xml>"+cr);
			xmlBuffer.flush();
			xmlBuffer.close();
			
			JOptionPane.showMessageDialog(null,LanguageManagement.getString("newofficeimported"),LanguageManagement.getString("officemngt"),JOptionPane.INFORMATION_MESSAGE);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			JOptionPane.showMessageDialog(null,"ERROR : "+e.getMessage(),LanguageManagement.getString("error"),JOptionPane.ERROR_MESSAGE);
			}
		}
	
	/**
	 * using the column name we get the cell value
	 * @throws Exception 
	 */
	private static int getIndexColumn(String[] cells, String target) throws Exception
		{
		for(int i=0;i<cells.length; i++)
			{
			if(cells[i].equals(target))return i;
			}
		
		throw new Exception("Column name not found");
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
