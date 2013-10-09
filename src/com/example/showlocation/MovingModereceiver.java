package com.example.showlocation;

import java.util.StringTokenizer;

import com.example.showlocation.MovingmodeOption.CreateMoveRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MovingModereceiver extends Activity{

	Databasehandler db;
	private ProgressDialog pDialog;
	String Number;
	String timeInt;
	String count;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		db = new Databasehandler(this);
		
		Bundle gotBasket = getIntent().getExtras();
		String Msg = gotBasket.getString("Msg");
		Number = gotBasket.getString("phoneNumber");
		StringTokenizer st = new StringTokenizer(Msg, "#");
		st.nextToken();
		timeInt=st.nextToken();
		count=st.nextToken();
		
		final String phoneNumber="0"+Number.substring(3);
		String sender =phoneNumber;
		
		if(db.checkContact(sender))
		{
			sender=db.getContact(phoneNumber).getName();
		}
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Moving mode request");
		alert.setMessage("Moving mode request from "+sender);
		
		alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				new CreateMovemodeON().execute();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent); // Close the application
				dialog.cancel();
			}
		});
		
		alert.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

					
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent); // Close the application
				dialog.cancel();
				
			}
		});
		
		AlertDialog al = alert.create();
		al.show();
		
		
	}
	
	class CreateMovemodeON extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Progress dialog
			pDialog = new ProgressDialog(MovingModereceiver.this);
			pDialog.setMessage("Sending Moving mode on accepting...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			String phoneNo=Number;
			String message = "#LFMovingModeON#"+timeInt+"#"+count+"#";

			if (phoneNo.length() > 0) {
				sendSMS(phoneNo, message);
			} else {
				Toast.makeText(getBaseContext(), "Error invalid sender",
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
					Toast.makeText(getBaseContext(), "Moving modeOn sent",
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
	
	
}
