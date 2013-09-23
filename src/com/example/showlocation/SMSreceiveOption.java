package com.example.showlocation;

import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SMSreceiveOption extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Bundle gotBasket = getIntent().getExtras();
		String Msg = gotBasket.getString("Msg");
		final String phoneNumber = gotBasket.getString("phoneNumber");
		StringTokenizer st = new StringTokenizer(Msg, "#");
		st.nextToken();
		String smLat = st.nextToken();
		final String latitude = smLat.substring(4);
		String smlon = st.nextToken();
		final String longitude = smlon.substring(4);
		final String date = st.nextToken();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("New Location received");
		alert.setMessage("New Location received from ");

		alert.setPositiveButton("Show", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				LocationObj loc = new LocationObj(phoneNumber, latitude,
						longitude, date);

				Intent i = new Intent(SMSreceiveOption.this, MapLocation.class);

				i.putExtra("LocObj", loc);
				startActivity(i);

			}
		});

		alert.setNegativeButton("Ignore",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

					}
				});

		AlertDialog al = alert.create();
		al.show();

	}

}
