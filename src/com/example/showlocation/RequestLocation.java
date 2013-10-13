package com.example.showlocation;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RequestLocation extends Activity {

	private ProgressDialog pDialog;

	TextView sendOpt;
	Button btnReq;
	Button btnSearch;
	EditText receiption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sender_layout);

		sendOpt = (TextView) findViewById(R.id.sendwayTV);
		btnSearch = (Button) findViewById(R.id.searchBtn);
		btnReq = (Button) findViewById(R.id.sendBtn);
		receiption = (EditText) findViewById(R.id.receiverIDET);

		sendOpt.setText("Request location");
		btnReq.setText("Request");

		try {

			Bundle gotbasket = getIntent().getExtras();
			String no = gotbasket.getString("number");
			receiption.setText(no);

		} catch (Exception e) {
			e.printStackTrace();
		}

		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle basket = new Bundle();
				basket.putString("stat", "request");
				Intent intent = new Intent(RequestLocation.this,
						FriendsList.class);
				intent.putExtras(basket);
				startActivity(intent);

			}
		});

		btnReq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// create the Asyncronic task to send location details in SMS
				new CreateRequest().execute();

			}
		});

	}

	class CreateRequest extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Progress dialog
			pDialog = new ProgressDialog(RequestLocation.this);
			pDialog.setMessage("Sending location..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			String phoneNo = receiption.getText().toString();
			String message = "#locationfinder#Where are you ?";

			if (phoneNo.length() > 0) {
				sendSMS(phoneNo, message);
			} else {
				Toast.makeText(getBaseContext(), "Please enter phone number",
						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			pDialog.dismiss();
		}

	}

	private void sendSMS(String phoneNo, String msg) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "Request sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "Request delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "Request not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNo, null, msg, sentPI, deliveredPI);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			Intent i=new Intent(RequestLocation.this,Menu.class);
			startActivity(i);
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
