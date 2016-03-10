package aho.rpi.unipi;

import java.util.TimerTask;

public abstract class UniWatchTimerTask extends TimerTask {
	
	private int index;
	private UniProperty prop;
	
	/**
	 * Timer with index and watch ability
	 * @param index
	 * @param prop
	 */
	public UniWatchTimerTask(int index, UniProperty prop){
		super();
		this.index = index;
		this.prop = prop;
	}
	
	/**
	 * Get timer's index
	 * @return timer's index
	 */
	public int getIndex(){
		return index;
	}
	
	/**
	 * Get timer's stored watched property
	 * @return timer's stored watched property
	 */
	public UniProperty getProperty(){
		return prop;
	}
}
