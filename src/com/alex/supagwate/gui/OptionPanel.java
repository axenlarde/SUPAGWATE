package com.alex.supagwate.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.alex.supagwate.device.GwTask;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;
import com.alex.supagwate.utils.xMLReader;


/**********************************
 * Used to treat a user process
 * 
 * @author RATEL Alexandre
 **********************************/
public class OptionPanel extends BaseLaunchPanel
	{
	/**
	 * Variables
	 */
	
	
	/***************
	 * Constructor
	 ***************/
	public OptionPanel(JFrame mainFrame, String panelTitle,
			ArrayList<OptionLine> myOptionList, actionType action)
		{
		super(mainFrame, panelTitle, myOptionList, action);
		}
	
	
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent evt)
		{
		if(evt.getSource() == this.launch)
			{
			Variables.getLogger().debug("Button launch pressed");
			
			manageSelectedBox();
			
			if(Variables.getAllowedItemsToProcess().size() == 0)
				{
				JOptionPane.showMessageDialog(null,LanguageManagement.getString("atleastoneoption"),LanguageManagement.getString("error"),JOptionPane.WARNING_MESSAGE);
				}
			else
				{
				this.disableButton();
				
				try
					{
					new GwTask(action);
					}
				catch(Exception exc)
					{
					Variables.getLogger().error("Error : "+exc.getMessage(), exc);
					}
				}
			}
		else if(evt.getSource() == this.cancel)
			{
			Variables.getLogger().debug("Button cancel pressed");
			this.mainFrame.getContentPane().removeAll();
			this.mainFrame.repaint();
			this.mainFrame.validate();
			}
		}
	
	
	
	
	
	
	/*2016*//*RATEL Alexandre 8)*/
	}

