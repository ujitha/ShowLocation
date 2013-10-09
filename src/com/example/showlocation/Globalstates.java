package com.example.showlocation;

import android.app.Application;

public class Globalstates extends Application{
	
	private boolean isSending;
	
	public boolean getIsSending()
	{
		return isSending;
	}
	
	public void setIsSending(boolean state)
	{
		this.isSending=state;
	}
	
	public Globalstates()
	{
		isSending=false;
	}
}
