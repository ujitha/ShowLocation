package com.example.showlocation;

//Author Ujitha Iroshan
//Load the map with given location coordinates

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapLocation extends Activity {

	private double latitude;
	private double longitude;
	private GoogleMap map;

	Databasehandler db;

	protected void onCreate(Bundle savedInstanstate) {
		super.onCreate(savedInstanstate);
		setContentView(R.layout.map_layout);

		// get the latitude and longitude from values from Menu class
		// if(getIntent().hasExtra("lati"))
		// {
		// latitude = getIntent().getExtras().getDouble("lati");
		// longitude = getIntent().getExtras().getDouble("longi");
		// }

		db = new Databasehandler(MapLocation.this);
		LocationObj LBObj = (LocationObj) getIntent().getSerializableExtra(
				"LocObj");
		String numLat = LBObj.getLatitude();
		String numLon = LBObj.getLongitude();
		String sender = LBObj.getSender();

		String Num = sender;

		if (db.checkContact(Num)) {
			sender = db.getContact(Num).getName();
		}

		latitude = Double.parseDouble(numLat);
		longitude = Double.parseDouble(numLon);

		// Print the latitude and longitude in the text view
		numLat = Double.toString(latitude);
		numLon = Double.toString(longitude);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Latitude -" + numLat + "  Longitude-" + numLon);

		// load the google map
		LatLng mycod = new LatLng(this.latitude, this.longitude);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycod, 15));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(mycod, 15.0f));
		map.setMyLocationEnabled(true);
		MarkerOptions MP = new MarkerOptions().position(mycod).title(sender);
		map.addMarker(MP);

	};

	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(MapLocation.this, Menu.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
