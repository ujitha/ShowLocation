package com.example.showlocation;

//Author Ujitha Iroshan
//This activity shows the list of History of locations 
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LocationList extends ListActivity {

	Databasehandler db = new Databasehandler(this);

	List<LocationObj> locationList;
	String locations[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			locationList = db.getAllLocations();
			locations = new String[locationList.size()];
		} catch (Exception e) {

		}

		for (int i = 0; i < locationList.size(); i++) {

			String sender = locationList.get(i).getSender();

			if (db.checkContact(sender)) {
				Contact con = db.getContact(sender);
				sender = con.getName();
			}

			locations[i] = sender + "\n" + locationList.get(i).getDate();
		}

		setListAdapter(new ArrayAdapter<String>(LocationList.this,
				R.layout.list_item, locations));
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {

		super.onListItemClick(l, v, position, id);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Location history is selected");
		alert.setMessage("What you want to do with the Loacation history ?");

		// Show the selected location in a map
		alert.setNegativeButton("Show", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				Intent i = new Intent(LocationList.this, MapLocation.class);

				i.putExtra("LocObj", locationList.get(position));
				startActivity(i);

			}
		});

		// Delete the selected location
		alert.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							db.deleteLocation(locationList.get(position));
							Toast.makeText(getBaseContext(),
									"Location history is Deleted successfully",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(LocationList.this,
									LocationList.class);
							startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(getBaseContext(),
									"Error : can not delete",
									Toast.LENGTH_SHORT).show();
						}

					}
				});

		AlertDialog al = alert.create();
		al.show();

		/*
		 * Intent i = new Intent(LocationList.this, MapLocation.class);
		 * 
		 * i.putExtra("LocObj", locationList.get(position)); startActivity(i);
		 */
	}

	protected void onLongListItemClick(ListView l, View v, final int position,
			long id) {

		super.onListItemClick(l, v, position, id);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Delete location history");
		alert.setMessage("You really want to Delete the Loacation history ?");

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				try {
					db.deleteLocation(locationList.get(position));
					Toast.makeText(getBaseContext(),
							"Location history is Deleted successfully",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LocationList.this,
							LocationList.class);
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), "Error : can not delete",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		AlertDialog al = alert.create();
		al.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(LocationList.this, Menu.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
