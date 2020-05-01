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
public abstract class StatusLine extends JPanel implements StatusLineImpl, MouseListener, ItemListener
	{
	/************
	 * variables
	 ************/
	protected ItemToProcess myItem;
	protected StringBuffer errorBuffer;
	protected ArrayList<String> errorList;
	
	protected JPanel left;
	protected JPanel right;
	protected JCheckBox select;
	protected JLabel name;
	protected JLabel desc;
	protected JLabel info;
	protected JLabel displayResult;
	
	/***************
	 * Constructeur
	 ***************/
	public StatusLine(ItemToProcess myItem) throws Exception
		{
		this.myItem = myItem;
		updateErrorBuffer();
		
		select = new JCheckBox();
		left = new JPanel();
		right = new JPanel();
		
		if((myItem.getStatus().equals(statusType.disabled)) ||
				(myItem.getStatus().equals(statusType.error)))
			{
			select.setSelected(false);
			}
		else
			{
			select.setSelected(true);
			}
		
		name = new JLabel(myItem.getName()+" "+myItem.getType().getName()+" : "+myItem.getAction().name());
		desc = new JLabel("");
		info = new JLabel(" [..] ");
		displayResult = new JLabel("waiting");
		
		//Disposition
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		left.setLayout(new BoxLayout(left,BoxLayout.X_AXIS));
		right.setLayout(new BoxLayout(right,BoxLayout.X_AXIS));
		
		
		//Assignation
		left.add(select);
		left.add(name);
		left.add(info);
		left.add(desc);
		this.add(left);
		this.add(Box.createHorizontalGlue());
		right.add(displayResult);
		this.add(right);
		
		//Events
		select.addItemListener(this);
		info.addMouseListener(this);
		}
	
	public void setFond(Color couleur)
		{
		setBackground(couleur);
		select.setBackground(couleur);
		
		doSetFond(couleur);
		}
	
	public JCheckBox getSelect()
		{
		return this.select;
		}
	
	public boolean getCheckStatus()
		{
		return select.isSelected();
		}
	
	public void enableSelect(boolean b)
		{
		select.setEnabled(b);
		}

	public String getResult()
		{
		return displayResult.getText();
		}

	public void setResult(String result)
		{
		displayResult.setText(result);
		}

	public void setName(String name)
		{
		this.name.setText(name+" | ");
		}
	
	public void setDesc(String desc)
		{
		this.desc.setText(desc);
		}

	public ItemToProcess getItem()
		{
		return myItem;
		}

	public void actionPerformed(ActionEvent evt)
		{
		
		}
	
	/*********
	 * Used to manage when the user choose to
	 * select or deselect the item
	 */
	public void manageSelection()
		{
		if(this.select.isSelected())
			{
			myItem.setStatus(statusType.waiting);
			}
		else
			{
			myItem.setStatus(statusType.disabled);
			}
		updateStatus();
		}
	
	/*****
	 * Called when the line has to be updated
	 */
	public void updateStatus()
		{
		displayResult.setText(LanguageManagement.getString(myItem.getStatus().name()));
		updateErrorBuffer();
		
		doUpdateStatus();
		}
	
	public void updateErrorBuffer()
		{
		errorBuffer = new StringBuffer("");
		errorList = new ArrayList<String>();
		
		//Errors
		if(myItem.getErrorList().size() == 0)
			{
			errorBuffer.append(LanguageManagement.getString("noerror"));
			errorList.add(LanguageManagement.getString("noerror"));
			}
		else
			{
			for(ErrorTemplate er : myItem.getErrorList())
				{
				errorList.add(LanguageManagement.getString("errorlist")+" :");
				errorBuffer.append(er.getErrorDesc()+"\r\n");
				errorList.add(er.getErrorDesc());
				}
			}
		
		//Correction
		if(myItem.getCorrectionList().size() == 0)
			{
			errorList.add(LanguageManagement.getString("nocorrection"));
			}
		else
			{
			errorList.add(LanguageManagement.getString("correctionlist")+" :");
			for(Correction c : myItem.getCorrectionList())
				{
				errorList.add(c.getDescription());
				}
			}
		}
	
	public void mouseClicked(MouseEvent evt)
		{
		if(evt.getSource() == this.info)
			{
			try
				{
				new DisplayInfoWindow(errorList);
				}
			catch (Exception exc)
				{
				exc.printStackTrace();
				}
			}
		
		doMouseClicked(evt);
		}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent evt)
		{
		if(evt.getSource() == this.select)
			{
			manageSelection();
			}
		
		doItemStateChanged(evt);
		}
	
	/*2012*//*RATEL Alexandre 8)*/
	}
