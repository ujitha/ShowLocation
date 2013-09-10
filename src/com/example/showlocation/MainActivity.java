package com.example.showlocation;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnShowMyLocation;
	GPSTracker gps;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnShowMyLocation = (Button) findViewById(R.id.myLocBtn);
		
		btnShowMyLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gps=new GPSTracker(MainActivity.this);
				
				
				if(gps.cangetLocation())
				{
					double latitude=gps.getLatitude();
					double longitude=gps.getLongitude();
										
					Intent intent=new Intent(MainActivity.this,MapLocation.class);
					intent.putExtra("lati", latitude);
					intent.putExtra("longi",longitude);
					
					startActivity(intent);
					//Toast.makeText(getApplicationContext(),"Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
					
				}
				else{
					gps.showSettingsAlert();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}