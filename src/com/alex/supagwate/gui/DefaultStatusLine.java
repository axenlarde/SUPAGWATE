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

import com.alex.supagwate.misc.Correction;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.Variables.statusType;

/*************************************
 * Class used to display one line in
 * the AXL status window
 *************************************/
public class DefaultStatusLine extends StatusLine
	{
	/************
	 * variables
	 ************/
	private JLabel displayError;
	private JLabel descError;
	
	/***************
	 * Constructeur
	 ***************/
	public DefaultStatusLine(ItemToProcess myItem) throws Exception
		{
		super(myItem);
		displayError = new JLabel("| < |");
		descError = new JLabel("");
		
		right.add(new JLabel(" "));
		right.add(displayError);
		right.add(descError);
		descError.setVisible(false);
		}
	
	public void doSetFond(Color couleur)
		{
		displayError.setBackground(couleur);
		}

	public JLabel getDescError()
		{
		return descError;
		}

	public void setDescError(String descError)
		{
		this.descError.setText(descError);
		}
	
	/*****
	 * Called when the line has to be updated
	 */
	public void doUpdateStatus()
		{
		descError.setText(errorBuffer.toString());
		}
	
	
	public void doMouseClicked(MouseEvent evt)
		{
		if(evt.getSource() == this.displayError)
			{
			if(displayError.getText().compareTo("| < |")==0)
				{
				this.desc.setVisible(false);
				this.displayResult.setVisible(false);
				this.descError.setVisible(true);
				this.displayError.setText("| > |");
				}
			else
				{
				this.desc.setVisible(true);
				this.displayResult.setVisible(true);
				this.descError.setVisible(false);
				this.displayError.setText("| < |");
				}
			}
		}

	public void doItemStateChanged(ItemEvent evt)
		{
		//Do something if needed
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
