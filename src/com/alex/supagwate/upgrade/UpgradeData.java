package com.alex.supagwate.upgrade;

import java.util.ArrayList;

import com.alex.supagwate.cli.OneLine;

/**
 * To store upgrade data
 *
 * @author Alexandre RATEL
 */
public class UpgradeData
	{
	/**
	 * Variables
	 */
	private String upgradeFile;
	private ArrayList<OneLine> checkcurrentversion, checkdiskspace, checkexistingfile, filedelete, startupgrade, checkfile, boot;
	
	public UpgradeData(String upgradeFile, ArrayList<OneLine> checkcurrentversion, ArrayList<OneLine> checkdiskspace, ArrayList<OneLine> checkexistingfile,
			ArrayList<OneLine> filedelete, ArrayList<OneLine> startupgrade, ArrayList<OneLine> checkfile,
			ArrayList<OneLine> boot)
		{
		super();
		this.upgradeFile = upgradeFile;
		this.checkcurrentversion = checkcurrentversion;
		this.checkdiskspace = checkdiskspace;
		this.checkexistingfile = checkexistingfile;
		this.filedelete = filedelete;
		this.startupgrade = startupgrade;
		this.checkfile = checkfile;
		this.boot = boot;
		}

	public String getUpgradeFile()
		{
		return upgradeFile;
		}

	public ArrayList<OneLine> getCheckdiskspace()
		{
		return checkdiskspace;
		}

	public ArrayList<OneLine> getCheckexistingfile()
		{
		return checkexistingfile;
		}

	public ArrayList<OneLine> getFiledelete()
		{
		return filedelete;
		}

	public ArrayList<OneLine> getStartupgrade()
		{
		return startupgrade;
		}

	public ArrayList<OneLine> getCheckfile()
		{
		return checkfile;
		}

	public ArrayList<OneLine> getBoot()
		{
		return boot;
		}

	public ArrayList<OneLine> getCheckcurrentversion()
		{
		return checkcurrentversion;
		}
	
	

	

	
	/*2020*//*RATEL Alexandre 8)*/
	}
