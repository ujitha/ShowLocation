package com.example.showlocation;

//Author Ujitha Iroshan
//send the location coordinates via SMS

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.showlocation.LocationSender.CreateNewLocation;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SMShandler extends Activity {

	private ProgressDialog pDialog;

	TextView sendOpt;
	Button btnsendSMS;
	Button btnSearch;
	EditText receiption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sender_layout);

		sendOpt = (TextView) findViewById(R.id.sendwayTV);
		btnSearch = (Button) findViewById(R.id.searchBtn);
		btnsendSMS = (Button) findViewById(R.id.sendBtn);
		receiption = (EditText) findViewById(R.id.receiverIDET);

		sendOpt.setText("Send Location via Text message");

		try {

			Bundle gotbasket = getIntent().getExtras();
			String no = gotbasket.getString("number");
			receiption.setText(no);

		} catch (Exception e) {
			e.printStackTrace();
		}

		btnsendSMS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// create the Asyncronic task to send location details in SMS
				new CreateNewMessage().execute();
				
			}
		});
		
		
		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle basket = new Bundle();
				basket.putString("stat", "show");
				Intent intent = new Intent(SMShandler.this, FriendsList.class);
				intent.putExtras(basket);
				startActivity(intent);

			}
		});

		

	}
	
	class CreateNewMessage extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Progress dialog
			pDialog = new ProgressDialog(SMShandler.this);
			pDialog.setMessage("Sending location..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String phoneNo = receiption.getText().toString();
			System.out.println(phoneNo);
			LocationObj LBObj = (LocationObj) getIntent()
					.getSerializableExtra("LocObj");
			String lat = LBObj.getLatitude();
			String lon = LBObj.getLongitude();
			String addr = "";

			Geocoder geocoder = new Geocoder(getBaseContext());
			try {
				List<Address> address = geocoder.getFromLocation(
						Double.parseDouble(lat), Double.parseDouble(lon), 1);

				if (address != null && address.size() > 0) {

					for (int i = 0; i < address.get(0)
							.getMaxAddressLineIndex(); i++) {
						addr += address.get(0).getAddressLine(i);
					}

					Toast.makeText(getBaseContext(), "Address is " + addr,
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String myNum = "";

			// get the System date and time
			Calendar cl = Calendar.getInstance();
			SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date = dateformat.format(cl.getTime());

			// Should get sender mobile number

			String message = "@locationfinder#Lat-" + lat + "#Lon-" + lon
					+ "#" + date + "#" + addr + "#";

			if (phoneNo.length() > 0) {
				sendSMS(phoneNo, message);
			} else {
				Toast.makeText(getBaseContext(),
						"Please enter phone number", Toast.LENGTH_SHORT)
						.show();
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
					Toast.makeText(getBaseContext(), "SMS sent",
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
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
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
			Intent i=new Intent(SMShandler.this,Menu.class);
			startActivity(i);
		}
		
		return super.onKeyDown(keyCode, event);
	}	
	

}
