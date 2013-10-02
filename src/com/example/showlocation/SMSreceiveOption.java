package com.example.showlocation;

import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SMSreceiveOption extends Activity {

	Databasehandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		db = new Databasehandler(this);

		Bundle gotBasket = getIntent().getExtras();
		String Msg = gotBasket.getString("Msg");
		String Number = gotBasket.getString("phoneNumber");
		StringTokenizer st = new StringTokenizer(Msg, "#");
		st.nextToken();
		String smLat = st.nextToken();
		final String latitude = smLat.substring(4);
		String smlon = st.nextToken();
		final String longitude = smlon.substring(4);
		final String date = st.nextToken();
		
		final String phoneNumber="0"+Number.substring(3);
		String sender =phoneNumber;
		
		if(db.checkContact(sender))
		{
			sender=db.getContact(phoneNumber).getName();
		}
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("New Location received");
		alert.setMessage("New Location received from "+sender);

		alert.setPositiveButton("Show", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				LocationObj loc = new LocationObj(phoneNumber, latitude,
						longitude, date);

				db.addLocation(loc);

				Intent i = new Intent(SMSreceiveOption.this, MapLocation.class);

				i.putExtra("LocObj", loc);
				startActivity(i);

			}
		});

		alert.setNegativeButton("Later", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				LocationObj loc = new LocationObj(phoneNumber, latitude,
						longitude, date);

				db.addLocation(loc);
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent); // Close the application
				dialog.cancel();

			}
		});

		AlertDialog al = alert.create();
		al.show();

	}

}
