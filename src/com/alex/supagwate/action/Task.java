package com.alex.supagwate.action;

import java.util.ArrayList;

import com.alex.supagwate.cli.CliTools;
import com.alex.supagwate.misc.ErrorTemplate;
import com.alex.supagwate.misc.ItemToProcess;
import com.alex.supagwate.utils.Variables;
import com.alex.supagwate.utils.Variables.actionType;
import com.alex.supagwate.utils.Variables.statusType;

/**********************************
 * Task
 * 
 * @author RATEL Alexandre
 **********************************/
public class Task extends Thread
	{
	/**
	 * Variables
	 */
	private ArrayList<ItemToProcess> todoList;
	private statusType status;
	private actionType action;
	private boolean pause, stop, started, end;
	private InjectorManager manager;
	
	/***************
	 * Constructor
	 ***************/
	public Task(ArrayList<ItemToProcess> todoList, actionType action)
		{
		this.todoList = todoList;
		this.action = action;
		this.status = statusType.init;
		stop = false;
		pause = true;
		started = false;
		end = false;
		manager = new InjectorManager();
		}
	
	/******
	 * Used to start the build process
	 * @throws Exception 
	 */
	public void build() throws Exception
		{
		//Build
		Variables.getLogger().info("Beginning of the build process");
		for(ItemToProcess myToDo : todoList)
			{
			myToDo.build();
			
			if(myToDo.getErrorList().size() != 0)
				{
				//Something happened during the building process so we disable the item
				Variables.getLogger().info("The following item has been disabled because some errors occurs during its preparation process : "+myToDo.getDeviceType().getName()+" "+myToDo.getName());
				for(ErrorTemplate e : myToDo.getErrorList())
					{
					Variables.getLogger().debug("- "+e.getTargetName()+" "+e.getIssueName()+" "+e.getErrorDesc()+" "+e.getError().name());
					}
				myToDo.setStatus(statusType.disabled);
				}
			}
		Variables.getLogger().info("End of the build process");
		}
	
	public void run()
		{
		started = true;
		try
			{
			/**
			 * We fill the CLI Manager
			 */
			Variables.getLogger().info("Task begins");
			
			/**
			 * Execution
			 */
			while(pause)
				{
				this.sleep(200);
				}
				
			if(!stop)
				{
				for(ItemToProcess myToDo : todoList)
					{
					if(myToDo.getStatus().equals(statusType.disabled))
						{
						Variables.getLogger().debug("The item \""+myToDo.getName()+"\" has been disabled so we do not process it");
						}
					else
						{
						manager.getInjectorList().add(myToDo.getInjector());
						}
					}
				manager.start();
				Variables.getLogger().debug("We wait for the injectors to end");
				while(manager.isAlive() && (!stop))
					{
					this.sleep(500);
					}
				Variables.getLogger().debug("Injectors tasks ends");
				}
			end = true;
			Variables.getLogger().info("Task ends");
			
			/**
			 * In case of 'get' instruction, we now write the result in a csv file
			 */
			if(action.equals(actionType.set) || action.equals(actionType.get))
				{
				CliTools.writeCliGetOutputToCSV();
				}
			
			/**
			 * End
			 */
			Variables.closeWorkbook();
			}
		catch (Exception e)
			{
			Variables.getLogger().debug("ERROR : "+e.getMessage(),e);
			}
		}
	
	public int getProgress()
		{
		int count = 0;
		for(ItemToProcess itp : todoList)
			{
			if(itp.getStatus().equals(statusType.error) ||
					itp.getStatus().equals(statusType.done) ||
					itp.getStatus().equals(statusType.disabled))
				{
				count++;
				}
			}
		return count;
		}

	public ArrayList<ItemToProcess> getTodoList()
		{
		return todoList;
		}

	public void setTodoList(ArrayList<ItemToProcess> todoList)
		{
		this.todoList = todoList;
		}

	public statusType getStatus()
		{
		return status;
		}

	public void setStatus(statusType status)
		{
		this.status = status;
		}

	public void Stop()
		{
		this.stop = true;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		
		if(this.pause)
			{
			Variables.getLogger().debug("The user asked to pause the task");
			}
		else
			{
			Variables.getLogger().debug("The user asked to resume the task");
			}
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		if(this.isAlive())
			{
			Variables.getLogger().debug("The Administrator asked to stop the process");
			if(manager != null)manager.setStop(stop);
			this.stop = stop;
			}
		else
			{
			Variables.getLogger().debug("The task is finished and therefore cannot be stopped again");
			}
		}

	public boolean isStarted()
		{
		return started;
		}

	public boolean isEnd()
		{
		return end;
		}

	public void setEnd(boolean end)
		{
		this.end = end;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

