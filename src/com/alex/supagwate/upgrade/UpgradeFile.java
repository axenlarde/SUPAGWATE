package com.alex.supagwate.upgrade;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 * Used to store data about an upgrade file
 * @author ratelal
 *
 */
public class UpgradeFile
	{
	/**
	 * Variables
	 */
	private File file;
	private String name, md5Hash;
	
	public UpgradeFile(File file) throws IOException
		{
		super();
		this.file = file;
		this.name = file.getName();
		this.md5Hash = DigestUtils.md5Hex(FileUtils.readFileToByteArray(file));
		}

	public File getFile()
		{
		return file;
		}

	public String getName()
		{
		return name;
		}

	public String getMd5Hash()
		{
		return md5Hash;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
