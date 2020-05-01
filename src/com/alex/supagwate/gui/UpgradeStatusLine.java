package com.alex.supagwate.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.alex.supagwate.device.Device;
import com.alex.supagwate.misc.Correction;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables.statusType;

/*************************************
 * Class used to display one line in
 * the AXL status window
 *************************************/
public class UpgradeStatusLine extends StatusLine
	{
	/************
	 * variables
	 ************/
	private JProgressBar progress;
	private JLabel progressLabel;
	
	/***************
	 * Constructor
	 ***************/
	public UpgradeStatusLine(ItemToProcess myItem) throws Exception
		{
		super(myItem);
		progressLabel = new JLabel("");
		progress = new JProgressBar(0, 100);
		
		left.add(progress);
		left.add(progressLabel);
		}
	
	public void doSetFond(Color couleur)
		{
		progress.setBackground(couleur);
		progressLabel.setBackground(couleur);
		}
	
	/*****
	 * Called when the line has to be updated
	 */
	public void doUpdateStatus()
		{
		int percent = UsefulMethod.getFTPTransfer(((Device)myItem).getIp()).getProgress();
		progressLabel.setText(percent+"%");
		progress.setValue(percent);
		}
	
	
	public void doMouseClicked(MouseEvent evt)
		{
		//Do something if needed
		}

	public void doItemStateChanged(ItemEvent evt)
		{
		//Do something if needed
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
