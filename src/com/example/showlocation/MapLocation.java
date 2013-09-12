package com.example.showlocation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapLocation extends Activity {

	private double latitude;
	private double longitude;
	private GoogleMap map;

	protected void onCreate(Bundle savedInstanstate) {
		super.onCreate(savedInstanstate);
		setContentView(R.layout.map_layout);

		latitude = getIntent().getExtras().getDouble("lati");
		longitude = getIntent().getExtras().getDouble("longi");

		// if(latitude==0 || longitude==0)
		// {
		// latitude=6.796342;
		// longitude=79.900861;
		//
		// }
		String numLat = Double.toString(latitude);
		String numLon = Double.toString(longitude);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Latitude -" + numLat + "  Longitude-" + numLon);

		LatLng mycod = new LatLng(this.latitude, this.longitude);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycod, 15));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(mycod, 15.0f));
		map.setMyLocationEnabled(true);
		MarkerOptions MP = new MarkerOptions().position(mycod)
				.title("I'm here");
		map.addMarker(MP);

	};

	protected boolean isRouteDisplayed() {

		return false;
	}

}
