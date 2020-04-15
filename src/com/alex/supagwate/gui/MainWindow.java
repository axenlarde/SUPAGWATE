package com.alex.supagwate.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.alex.supagwate.device.DeviceType;
import com.alex.supagwate.office.OfficeTools;
import com.alex.supagwate.utils.LanguageManagement;
import com.alex.supagwate.utils.Position;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;


public class MainWindow extends JFrame implements ActionListener, WindowListener
	{
	/************
	 * Variables
	 ************/
	//Dimensions
	public Dimension dimInfo;
	
	//Menu
	public JMenuBar myMenuBar;
	public JMenu menu;
	public JMenu gatewayMngt;
	public JMenuItem sendCMD;
	public JMenuItem sendCmdFromProfile;
	public JMenuItem getInfo;
	public JMenuItem compliance;
	public JMenuItem upgrade;
	public JMenuItem advancedWizard;
	public JMenuItem exit;
	public JMenu miscMngt;
	public JMenuItem profileMngt;
	public JMenuItem officeMngt;
	public JMenuItem cucmMngt;
	public JMenu tools;
	public JMenuItem option;
	public JMenuItem genProfileFile;
	public JMenu help;
	public JMenuItem about;
	private JLabel logo;
	private JPanel main;
	
	/***************
	 * Constructor
	 ***************/
	public MainWindow()
		{
		/*************
		 * Variables
		 *************/
		//Menu
		myMenuBar = new JMenuBar();
		menu = new JMenu(LanguageManagement.getString("mainmenutitle"));
		gatewayMngt = new JMenu(LanguageManagement.getString("gatewaymngt"));
		sendCMD = new JMenuItem(LanguageManagement.getString("sendcmd"));
		sendCMD.setToolTipText(LanguageManagement.getString("tttsendcmd"));
		sendCmdFromProfile = new JMenuItem(LanguageManagement.getString("sendcmdfromprofile"));
		sendCmdFromProfile.setToolTipText(LanguageManagement.getString("tttsendcmdfromprofile"));
		getInfo = new JMenuItem(LanguageManagement.getString("getinfo"));
		getInfo.setToolTipText(LanguageManagement.getString("tttgetinfo"));
		compliance = new JMenuItem(LanguageManagement.getString("compliance"));
		compliance.setToolTipText(LanguageManagement.getString("tttcompliance"));
		upgrade = new JMenuItem(LanguageManagement.getString("upgrade"));
		upgrade.setToolTipText(LanguageManagement.getString("tttupgrade"));
		advancedWizard = new JMenuItem(LanguageManagement.getString("advancedwizard"));
		advancedWizard.setToolTipText(LanguageManagement.getString("tttadvancedwizard"));
		miscMngt = new JMenu(LanguageManagement.getString("miscmngt"));
		profileMngt = new JMenuItem(LanguageManagement.getString("profilemngt"));
		officeMngt = new JMenuItem(LanguageManagement.getString("officemngt"));
		cucmMngt = new JMenuItem(LanguageManagement.getString("cucmmngt"));
		exit = new JMenuItem(LanguageManagement.getString("exit"));
		tools = new JMenu(LanguageManagement.getString("tools"));
		option = new JMenuItem(LanguageManagement.getString("option"));
		genProfileFile = new JMenuItem(LanguageManagement.getString("genprofilefile"));
		help = new JMenu(LanguageManagement.getString("help"));
		about = new JMenuItem(LanguageManagement.getString("about"));
		
		//Logo & icon
		try
			{
			//Aspect.load(this, getClass().getResource("/art/IconSofuto.png").getPath(), Variables.getSoftwareName());
			//logo = new JLabel(new ImageIcon(getClass().getResource("/art/LogoSofuto.png")));
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		
		main = new JPanel();
		main.setLayout(new BoxLayout(main,BoxLayout.X_AXIS));
		main.setBackground(Color.WHITE);
		
		//Dimensions
		dimInfo = new Dimension(this.getWidth(), 100);
		
		//Titre
		setTitle(Variables.getSoftwareName()+" - "+Variables.getSoftwareVersion());
		
		//Positionnement
		this.setSize(new Dimension(600,400));
		Position.center(this);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		//Assignation
		myMenuBar.add(menu);
		myMenuBar.add(tools);
		myMenuBar.add(help);
		gatewayMngt.add(sendCMD);
		gatewayMngt.add(sendCmdFromProfile);
		gatewayMngt.add(getInfo);
		gatewayMngt.add(compliance);
		gatewayMngt.add(upgrade);
		gatewayMngt.add(advancedWizard);
		menu.add(gatewayMngt);
		miscMngt.add(profileMngt);
		miscMngt.add(officeMngt);
		miscMngt.add(cucmMngt);
		menu.add(miscMngt);
		menu.add(exit);
		tools.add(option);
		tools.add(genProfileFile);
		help.add(about);
		setJMenuBar(myMenuBar);
		
		//Logo
		this.getContentPane().setBackground(Color.WHITE);
		main.add(Box.createHorizontalGlue());
		//main.add(logo);
		main.add(Box.createHorizontalGlue());
		add(Box.createVerticalGlue());
		add(main);
		add(Box.createVerticalGlue());
		
		
		//Events
		sendCMD.addActionListener(this);
		sendCmdFromProfile.addActionListener(this);
		getInfo.addActionListener(this);
		compliance.addActionListener(this);
		upgrade.addActionListener(this);
		advancedWizard.addActionListener(this);
		profileMngt.addActionListener(this);
		officeMngt.addActionListener(this);
		cucmMngt.addActionListener(this);
		exit.addActionListener(this);
		option.addActionListener(this);
		genProfileFile.addActionListener(this);
		help.addActionListener(this);
		about.addActionListener(this);
		
		this.addWindowListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//setResizable(false);
		setVisible(true);
		
		Variables.setMyWindow(this);
		Variables.getLogger().info("Main window started");
		}

	public void actionPerformed(ActionEvent evt)
		{
		if(evt.getSource() == this.sendCMD)
			{
			Variables.getLogger().info("Send command button pressed");
			
			//To be written
			}
		else if(evt.getSource() == this.sendCmdFromProfile)
			{
			Variables.getLogger().info("Send command from profile button pressed");
			
			Variables.setAllowedItemsToProcess(new ArrayList<String>());
			ArrayList<OptionLine> myOptionList = new ArrayList<OptionLine>();
			
			for(DeviceType dt : Variables.getDeviceTypeList())
				{
				myOptionList.add(new OptionLine(dt.getName()));
				}
			
			this.getContentPane().removeAll();
			this.getContentPane().add(new OptionPanel(this, LanguageManagement.getString("sendcmdfromprofile"), myOptionList, actionType.set));
			this.repaint();
			this.validate();
			}
		else if(evt.getSource() == this.officeMngt)
			{
			Variables.getLogger().info("Office management button pressed");
			
			OfficeTools.importOfficeListFromCSVFile(this);
			}
		else if(evt.getSource() == this.exit)
			{
			this.dispose();
			}
		else if(evt.getSource() == this.about)
			{
			Variables.getLogger().info("About button pressed");
			new WindowApropos(LanguageManagement.getString("about")+Variables.getSoftwareName(),
					LanguageManagement.getString("softwarename")+Variables.getSoftwareName(),
					LanguageManagement.getString("softwareversion")+Variables.getSoftwareVersion(),
					LanguageManagement.getString("author")+"Alexandre RATEL",
					LanguageManagement.getString("contact")+"alexandre.ratel@gmail.com");
			}
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0)
		{
		Variables.getLogger().info("The user exit the application willfully");
		System.exit(0);
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0)
		{
		// TODO Auto-generated method stub
		
		}
	
	/*2012*//*RATEL Alexandre 8)*/
	}
