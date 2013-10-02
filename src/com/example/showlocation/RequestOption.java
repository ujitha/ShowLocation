package com.example.showlocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.showlocation.SMShandler.CreateNewMessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RequestOption extends Activity {

	Databasehandler db;
	private ProgressDialog pDialog;

	TextView sendOpt;
	TextView toTV;
	Button btnIgn;
	Button btnSend;
	EditText receiption;
	String Number;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sender_layout);
		
		db = new Databasehandler(this);
		
		gps = new GPSTracker(this);

		Bundle gotBasket = getIntent().getExtras();
		Number = gotBasket.getString("phoneNumber");

		sendOpt = (TextView) findViewById(R.id.sendwayTV);
		btnSend = (Button) findViewById(R.id.searchBtn);
		btnIgn = (Button) findViewById(R.id.sendBtn);
		receiption = (EditText) findViewById(R.id.receiverIDET);
		toTV=(TextView) findViewById(R.id.tvTO);

		receiption.setEnabled(false);
		String Num="0"+Number.substring(3);
		String sender=Num;
		
		if(db.checkContact(Num))
		{
			sender=db.getContact(Num).getName();
		}
		
		sendOpt.setText("New location request from " + sender +"\n"
				+ " Send location or not ?");
		btnSend.setText("Send");
		btnIgn.setText("Ignore");
		toTV.setText("");

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (gps.cangetLocation()) {
					new CreateNewMessage().execute();
				} else {
					gps.showSettingsAlert();
				}

			}
		});
		
		btnIgn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent); // Close the application
				
			}
		});

	}

	class CreateNewMessage extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Progress dialog
			pDialog = new ProgressDialog(RequestOption.this);
			pDialog.setMessage("Sending location..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			String lat = Double.toString(gps.getLatitude());
			String lon = Double.toString(gps.getLongitude());

			String addr = "";

			Geocoder geocoder = new Geocoder(getBaseContext());
			try {
				List<Address> address = geocoder.getFromLocation(
						Double.parseDouble(lat), Double.parseDouble(lon), 1);

				if (address != null && address.size() > 0) {

					for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++) {
						addr += address.get(0).getAddressLine(i);
					}

					Toast.makeText(getBaseContext(), "Address is " + addr,
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// get the System date and time
			Calendar cl = Calendar.getInstance();
			SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date = dateformat.format(cl.getTime());

			String message = "@locationfinder#Lat-" + lat + "#Lon-" + lon + "#"
					+ date + "#" + addr + "#";
			
			sendSMS(Number, message);

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			pDialog.dismiss();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent); // Close the application
			
			
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

}
