package com.example.showlocation;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity{

	private String classes[]={"Show My Location","Send My Location","Friends","Moving mode","History","My Info"};
	private GPSTracker gps;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(Menu.this, R.layout.list_item, classes));
		
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		gps=new GPSTracker(Menu.this);
		
			if(position==0)
			{
				if(gps.cangetLocation())
				{
					double latitude=gps.getLatitude();
					double longitude=gps.getLongitude();
										
					
					Intent intent=new Intent(Menu.this,MapLocation.class);
					intent.putExtra("lati", latitude);
					intent.putExtra("longi",longitude);
					
					startActivity(intent);
					//Toast.makeText(getApplicationContext(),"Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
				}				
				else{
					gps.showSettingsAlert();
				}
				
			}
			else if(position==1)
			{
				if(gps.cangetLocation())
				{
					Double latitude=gps.getLatitude();
					Double longitude=gps.getLongitude();
							System.out.print(latitude);		
					Intent intent=new Intent(Menu.this,LocationSender.class);
					intent.putExtra("lati", latitude);
					intent.putExtra("longi",longitude);
					
					startActivity(intent);
										
				}
				else{
					gps.showSettingsAlert();
				}
			}	
		
				
	}

	
	
	

}
