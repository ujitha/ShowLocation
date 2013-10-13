package com.example.showlocation;

//Author Ujitha Iroshan
//This activity List the exisiting Friends details in the friend table 
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

public class FriendsList extends ListActivity {

	Databasehandler db = new Databasehandler(this);

	List<Contact> contactList;
	String friends[];
	String status = null;

	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Get all the Friend contact details
		contactList = db.getAllContacts();
		friends = new String[contactList.size()];

		gps = new GPSTracker(getBaseContext());

		for (int i = 0; i < contactList.size(); i++) {
			friends[i] = contactList.get(i).getName() + " \n"
					+ contactList.get(i).getPhoneNumber();
		}

		// Create the FriendList List view
		setListAdapter(new ArrayAdapter<String>(FriendsList.this,
				R.layout.list_item, friends));
		try {
			Bundle gotbasket = getIntent().getExtras();
			status = gotbasket.getString("stat");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {

		super.onListItemClick(l, v, position, id);
		// If FriendList activity is started from clicking Delete friend menu
		// item from FriendSettings activity
		if (status.equals((String) "Delete")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Delete friend contact");
			alert.setMessage("You really want to Delete the contact ?");

			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String chkNum = contactList.get(position)
									.getPhoneNumber();
							// Delete the selected friend contact from database
							// friend table
							db.deleteContact(contactList.get(position));

							if (!db.checkContact(chkNum)) {
								Toast.makeText(getBaseContext(),
										"Friend contact is Deleted",
										Toast.LENGTH_SHORT).show();

								Bundle basket = new Bundle();
								basket.putString("stat", "Delete");
								Intent i = new Intent(FriendsList.this,
										FriendsList.class);
								i.putExtras(basket);
								startActivity(i);

							} else {
								Toast.makeText(getBaseContext(),
										"Error : can not delete",
										Toast.LENGTH_SHORT).show();
							}

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
		}

		// If FriendList activity is started from clicking Edit Friend menu item
		// in Friend Settings activity
		// Clicking a Friend will start EditFriend acticity and able to Edit the
		// Friend detail
		else if (status.equals((String) "Edit")) {
			Bundle basket = new Bundle();
			basket.putString("number", contactList.get(position)
					.getPhoneNumber());
			Intent intent = new Intent(FriendsList.this, EditFriend.class);
			intent.putExtras(basket);
			startActivity(intent);

		}
		// If FriendList is started from clicking Search button in SMShandler
		// activity
		else if (status.equals((String) "show")) {
			String phoneNum = contactList.get(position).getPhoneNumber();

			String latitude = Double.toString(gps.getLatitude());
			String longitude = Double.toString(gps.getLongitude());

			LocationObj LB = new LocationObj();
			LB.setLatitude(latitude);
			LB.setLongitude(longitude);

			Bundle basket = new Bundle();
			basket.putString("number", phoneNum);
			Intent intent = new Intent(FriendsList.this, SMShandler.class);
			intent.putExtras(basket);
			intent.putExtra("LocObj", LB);
			startActivity(intent);
		}
		// If friendList is started from clicking Search button in Request
		// Location activity
		else if (status.equals((String) "request")) {
			String phoneNum = contactList.get(position).getPhoneNumber();
			Bundle basket = new Bundle();
			basket.putString("number", phoneNum);
			Intent intent = new Intent(FriendsList.this, RequestLocation.class);
			intent.putExtras(basket);
			startActivity(intent);
		}
		// If friendList is started from clicking Search button in
		// MovingModeOption activity
		else if (status.equals((String) "moveMode")) {
			Bundle gotbasket = getIntent().getExtras();

			String phoneNum = contactList.get(position).getPhoneNumber();
			Bundle basket = new Bundle();
			basket.putString("number", phoneNum);
			try {
				basket.putString("timeInt", gotbasket.getString("timeInt"));
				basket.putString("count", gotbasket.getString("count"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(FriendsList.this, MovingmodeOption.class);
			intent.putExtras(basket);
			startActivity(intent);
		} else if (status.equals((String) "search")) {

		}

	}

	// Back key operation to start FriendSettings activity
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(FriendsList.this, FriendsSettings.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
