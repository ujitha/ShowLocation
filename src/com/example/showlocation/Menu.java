package com.example.showlocation;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	private String classes[] = { "Show My Location", "Send My Location",
			"Friends", "Moving mode", "History", "My Info" };
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(Menu.this, R.layout.list_item,
				classes));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		gps = new GPSTracker(Menu.this);

		if (position == 0) {
			if (gps.cangetLocation()) {
				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();

				Intent intent = new Intent(Menu.this, MapLocation.class);
				intent.putExtra("lati", latitude);
				intent.putExtra("longi", longitude);

				startActivity(intent);
				// Toast.makeText(getApplicationContext(),"Your Location is - \nLat: "
				// + latitude + "\nLong: " + longitude,
				// Toast.LENGTH_LONG).show();
			} else {
				gps.showSettingsAlert();
			}

		} else if (position == 1) {
			if (gps.cangetLocation()) {
				final String items[] = { "Via Internet", "Via Text message" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);

				builder.setTitle("Send my location");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (which == 0) {
							Double latitude = gps.getLatitude();
							Double longitude = gps.getLongitude();

							Intent intent = new Intent(Menu.this,
									LocationSender.class);
							intent.putExtra("lati", latitude);
							intent.putExtra("longi", longitude);

							startActivity(intent);
						}
						else if(which==1)
						{
							Double latitude = gps.getLatitude();
							Double longitude = gps.getLongitude();

							Intent intent = new Intent(Menu.this,
									SMShandler.class);
							intent.putExtra("lati", latitude);
							intent.putExtra("longi", longitude);

							startActivity(intent);
						}

					}
				});
				builder.show();

			} else {
				gps.showSettingsAlert();
			}
		}

	}

	// Functionality for the back key
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Location Finder Quit");
			alert.setMessage("You really want to Quit ?");

			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish(); // Close the application

						}
					});

			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

						}
					});

			AlertDialog al = alert.create();
			al.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
