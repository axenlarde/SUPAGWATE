package com.alex.supagwate.action;

import java.util.ArrayList;

import com.alex.supagwate.utils.UsefulMethod;
import com.alex.supagwate.utils.Variables;


/**
 * Used to manage cli injection sessions
 *
 * @author Alexandre RATEL
 */
public class InjectorManager extends Thread
	{
	/**
	 * Variables
	 */
	private ArrayList<Injector> injectorList;
	private boolean stop, pause;
	private int maxThread;
	
	public InjectorManager()
		{
		stop = false;
		pause = false;
		injectorList = new ArrayList<Injector>();
		
		try
			{
			this.maxThread = Integer.parseInt(UsefulMethod.getTargetOption("maxclithread"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Couldn't find the max simultaneous thread value. Applying default value");
			this.maxThread = 10;
			}
		}
	
	/*****
	 * Here we will start cliInjector all at the same time
	 * according to the max thread value 
	 */
	public void run()
		{
		try
			{
			int index = 0;
			while((index < injectorList.size()) && (!stop))
				{
				for(int i=index; i<injectorList.size(); i++)
					{
					if(startNewThread()&&(!pause))
						{
						injectorList.get(i).start();
						index++;
						}
					else
						{
						/**
						 * If the max thread is reach or if the pause is on there is no point of trying the next cliInjector
						 * So we break here to directly go to the sleep step
						 */ 
						break;
						}
					}
				this.sleep(100);
				}
			
			/**
			 * To end this thread we wait for all the cliInjector to end
			 */
			boolean alive = true;
			while(alive)
				{
				alive = false;
				for(Injector clii : injectorList)
					{
					if(clii.isAlive())
						{
						alive = true;
						break;
						}
					}
				this.sleep(100);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong with the cli manager : "+e.getMessage(),e);
			}
		}
	
	private boolean startNewThread()
		{
		int totalInProgress = 0;
		for(Injector clii : injectorList)
			{
			if(clii.isAlive())totalInProgress++;
			}
		
		if(totalInProgress<=maxThread)return true;
		return false;
		}
	
	public ArrayList<Injector> getInjectorList()
		{
		return injectorList;
		}

	public void setInjectorList(ArrayList<Injector> cliIList)
		{
		this.injectorList = cliIList;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		this.stop = stop;
		}
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
