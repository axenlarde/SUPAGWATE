package com.alex.supagwate.device;

import com.alex.supagwate.misc.ErrorTemplate;

/**********************************
 * 
 * 
 * @author RATEL Alexandre
 **********************************/
public class GwError extends ErrorTemplate
	{

	/***************
	 * Constructor
	 ***************/
	public GwError(String targetName, String issueName, String errorDesc, errorType error)
		{
		super(targetName, issueName, errorDesc, error);
		// TODO Auto-generated constructor stub
		}

	/***************
	 * Constructor
	 ***************/
	public GwError(String targetName, String issueName, String errorDesc,
			String advice, errorType error, boolean warning)
		{
		super(targetName, issueName, errorDesc, advice, error, warning);
		// TODO Auto-generated constructor stub
		}

	/***************
	 * Constructor
	 ***************/
	public GwError(String errorDesc)
		{
		super(errorDesc);
		// TODO Auto-generated constructor stub
		}

	
	
	/*2016*//*RATEL Alexandre 8)*/
	}

