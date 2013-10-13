package com.example.showlocation;

//Author Ujitha Iroshan
//Edit the existing friend details and update to the database using this activity
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditFriend extends Activity {

	EditText name;
	EditText num;
	Button update;
	Button cancel;

	Databasehandler db;
	List<Contact> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.friendsadd_layout);

		name = (EditText) findViewById(R.id.nameET);
		num = (EditText) findViewById(R.id.numberET);
		update = (Button) findViewById(R.id.addBtn);
		cancel = (Button) findViewById(R.id.cancelBtn);

		db = new Databasehandler(this);

		contactList = db.getAllContacts();
		update.setText("Update");

		Bundle gotbasket = getIntent().getExtras();
		String phonNum = gotbasket.getString("number");

		final Contact contact = db.getContact(phonNum);

		name.setText(contact.getName());
		num.setText(contact.getPhoneNumber());

		update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Contact con = new Contact();
				con.setId(contact.getId());
				con.setName(name.getText().toString());
				con.setPhoneNumber(num.getText().toString());

				db.updateContact(con);
				if (db.checkContact(num.getText().toString())) {
					Toast.makeText(getBaseContext(),
							"Friend contact is successfully updated ",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getBaseContext(),
							"Error : Can not update the friend contact ",
							Toast.LENGTH_SHORT).show();
				}

				Bundle basket = new Bundle();
				basket.putString("stat", "Edit");
				Intent intent = new Intent(EditFriend.this, FriendsList.class);
				intent.putExtras(basket);
				startActivity(intent);

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle basket = new Bundle();
				basket.putString("stat", "Edit");
				Intent intent = new Intent(EditFriend.this, FriendsList.class);
				intent.putExtras(basket);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Bundle basket = new Bundle();
			basket.putString("stat", "Edit");
			Intent intent = new Intent(EditFriend.this, FriendsList.class);
			intent.putExtras(basket);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
