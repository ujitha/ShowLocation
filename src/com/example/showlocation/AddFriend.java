package com.example.showlocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriend extends Activity {

	EditText name;
	EditText num;
	Button add;
	Button cancel;

	Databasehandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.friendsadd_layout);

		name = (EditText) findViewById(R.id.nameET);
		num = (EditText) findViewById(R.id.numberET);
		add = (Button) findViewById(R.id.addBtn);
		cancel = (Button) findViewById(R.id.cancelBtn);

		db = new Databasehandler(this);

		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String frndName = name.getText().toString();
				String phoneNum = num.getText().toString();

				if (frndName.length() > 0 && phoneNum.length() > 0) {

					if (!db.checkContact(phoneNum)) {
						Contact contact = new Contact();
						contact.setName(frndName);
						contact.setPhoneNumber(phoneNum);

						db.addContact(contact);

						if (db.checkContact(phoneNum)) {
							Toast.makeText(
									getBaseContext(),
									"New friend contact is successfully added ",
									Toast.LENGTH_SHORT).show();
														
						} else {
							Toast.makeText(
									getBaseContext(),
									"Error : Can not add the new friend contact ",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(getBaseContext(),
								"Error : Phone number exist ",
								Toast.LENGTH_SHORT).show();
						num.setText("");
						num.findFocus();
					}
				}

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				name.setText("");
				num.setText("");
				name.findFocus();
			}
		});

	}

}
