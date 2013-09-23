package com.example.showlocation;

//Author Ujitha Iroshan
//Menu with the main functionalities of the app

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

	// List options strings
	private String classes[] = { "Show My Location", "Send My Location",
			"Friends", "Moving mode", "History", "My Info" };
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(Menu.this, R.layout.list_item,
				classes));

	}

	// When the list item is click
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		gps = new GPSTracker(Menu.this);

		// if show my location list item is clicked MapLocation activity starts
		// and map loads
		if (position == 0) {
			if (gps.cangetLocation()) {

				String latitude = Double.toString(gps.getLatitude());
				String longitude = Double.toString(gps.getLongitude());

				LocationObj LB = new LocationObj();
				LB.setLatitude(latitude);
				LB.setLongitude(longitude);

				Intent intent = new Intent(Menu.this, MapLocation.class);
				intent.putExtra("LocObj", LB);

				startActivity(intent);

			} else {
				gps.showSettingsAlert();
			}
			// if send my location list item is clicked
		} else if (position == 1) {
			if (gps.cangetLocation()) {
				final String items[] = { "Via Internet", "Via Text message" };

				// Alert dialog to select the option to send location via
				// internet or via text messaging
				AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);

				builder.setTitle("Send my location");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// if via internet
						if (which == 0) {

							String latitude = Double.toString(gps.getLatitude());
							String longitude = Double.toString(gps
									.getLongitude());

							LocationObj LB = new LocationObj();
							LB.setLatitude(latitude);
							LB.setLongitude(longitude);

							Intent intent = new Intent(Menu.this,
									LocationSender.class);
							intent.putExtra("LocObj", LB);

							startActivity(intent);
							// via text message
						} else if (which == 1) {

							String latitude = Double.toString(gps.getLatitude());
							String longitude = Double.toString(gps
									.getLongitude());

							LocationObj LB = new LocationObj();
							LB.setLatitude(latitude);
							LB.setLongitude(longitude);

							Intent intent = new Intent(Menu.this,
									SMShandler.class);
							intent.putExtra("LocObj", LB);

							startActivity(intent);
						}

					}
				});
				builder.show();

			} else {
				gps.showSettingsAlert();
			}
		} else if (position == 2) {
			Intent i = new Intent(Menu.this, FriendsSettings.class);
			startActivity(i);
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
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent); // Close the application

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
