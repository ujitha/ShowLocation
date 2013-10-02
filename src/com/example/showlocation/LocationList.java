package com.example.showlocation;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationList extends ListActivity {

	Databasehandler db = new Databasehandler(this);

	List<LocationObj> locationList;
	String locations[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try{
		locationList = db.getAllLocations();
		locations=new String[locationList.size()];
		}
		catch (Exception e)
		{
			
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
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(LocationList.this, MapLocation.class);

		i.putExtra("LocObj", locationList.get(position));
		startActivity(i);
	}

}
