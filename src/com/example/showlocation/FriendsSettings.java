package com.example.showlocation;

//Author Ujitha Iroshan
//Friend settings activity which has all the functionalities about Friends circle
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendsSettings extends ListActivity {

	private String options[] = { "Search Friends", "Add new Friend",
			"Edit Friend", "Delete Friend" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(FriendsSettings.this,
				R.layout.list_item, options));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		if (position == 0) {
			Bundle basket = new Bundle();
			basket.putString("stat", "search");
			Intent intent = new Intent(FriendsSettings.this, FriendsList.class);
			intent.putExtras(basket);
			startActivity(intent);

		} else if (position == 1) {
			Intent intent = new Intent(FriendsSettings.this, AddFriend.class);
			startActivity(intent);
		} else if (position == 3) {
			Bundle basket = new Bundle();
			basket.putString("stat", "Delete");
			Intent intent = new Intent(FriendsSettings.this, FriendsList.class);
			intent.putExtras(basket);
			startActivity(intent);
		} else if (position == 2) {
			Bundle basket = new Bundle();
			basket.putString("stat", "Edit");
			Intent intent = new Intent(FriendsSettings.this, FriendsList.class);
			intent.putExtras(basket);
			startActivity(intent);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(FriendsSettings.this, Menu.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
