package com.alex.supagwate.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;

/**
 * @author Alexandre RATEL
 *
 * Miscellaneous static methods
 */
public class MiscTools
	{
	
	/**
	 * Write the overall result to a csv file
	 */
	public static void writeOverallResultToCSV(ArrayList<ItemToProcess> itemList)
		{
		try
			{
			Variables.getLogger().debug("Writing the overall result to a file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getOverallResultFileName()+"_"+time.format(date);
			BufferedWriter csvBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+".csv"), false));
			
			//FirstLine
			csvBuffer.write("Name"+splitter+"Type"+splitter+"Status"+cr);
			
			for(ItemToProcess item : itemList)
				{
				csvBuffer.write(item.getName()+splitter+item.getDeviceType()+splitter+item.getStatus()+cr);
				}
			
			csvBuffer.flush();
			csvBuffer.close();
			Variables.getLogger().debug("Writing overall result : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing overall result to CSV : "+e.getMessage(),e);
			}
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
