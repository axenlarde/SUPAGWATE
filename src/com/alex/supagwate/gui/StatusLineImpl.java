package com.alex.supagwate.gui;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;

public interface StatusLineImpl
	{
	public boolean getCheckStatus();
	public void enableSelect(boolean b);
	public void manageSelection();
	public void updateStatus();
	public void doUpdateStatus();
	public void setFond(Color couleur);
	public void doSetFond(Color couleur);
	public String getResult();
	public void updateErrorBuffer();
	public void doMouseClicked(MouseEvent evt);
	public void doItemStateChanged(ItemEvent evt);
	}
