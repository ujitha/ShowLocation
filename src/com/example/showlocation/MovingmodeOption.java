package com.example.showlocation;
//Author Ujitha Iroshan
//Moving mode funcion is enabled and configure in this activity
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MovingmodeOption extends Activity {

	private ProgressDialog pDialog;
	private GPSTracker gps;

	CheckBox movMod;
	EditText senderET;
	Button btnsearch;
	Button btnsend;
	TextView toTV;
	Spinner intervalSpin;
	Spinner countlocSpin;
	Button btnAbort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.moving_layout);

		movMod = (CheckBox) findViewById(R.id.movingModeChk);
		senderET = (EditText) findViewById(R.id.ETreceiver);
		btnsearch = (Button) findViewById(R.id.BtnSearch);
		btnsend = (Button) findViewById(R.id.BtnSend);
		toTV = (TextView) findViewById(R.id.TOtv);
		intervalSpin = (Spinner) findViewById(R.id.timeinterval_spin);
		countlocSpin = (Spinner) findViewById(R.id.nolocation_spin);
		btnAbort = (Button) findViewById(R.id.BtnAbort);

		boolean isChecked = getBooleanFromPreferences("isChecked");
		setLists();
		movMod.setChecked(isChecked);
		if (isChecked) {
			showOptions();

			if (isServiceRunning("SendinMoveMode")) {
				btnsend.setEnabled(false);
			} else {
				btnAbort.setEnabled(false);
			}
		} else {
			hideOptions();
		}

		try {

			Bundle gotbasket = getIntent().getExtras();
			String no = gotbasket.getString("number");
			String timeInt=gotbasket.getString("timeInt");
			String count=gotbasket.getString("count");
			senderET.setText(no);
			intervalSpin.setSelection(getIndex(intervalSpin,timeInt));
			countlocSpin.setSelection(getIndex(countlocSpin,count));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		movMod.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				MovingmodeOption.this.putBooleanInPreferences(isChecked,
						"isChecked");
				if (!isChecked) {
					hideOptions();
				} else {

					showOptions();
					btnAbort.setEnabled(false);
				}
			}
		});

		btnsearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle basket = new Bundle();
				basket.putString("stat", "moveMode");
				basket.putString("timeInt", intervalSpin.getSelectedItem().toString());
				basket.putString("count", countlocSpin.getSelectedItem().toString());
				Intent intent = new Intent(MovingmodeOption.this,
						FriendsList.class);
				intent.putExtras(basket);
				startActivity(intent);

			}
		});

		btnsend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gps = new GPSTracker(MovingmodeOption.this);
				if (gps.cangetLocation()) {

					if (!senderET.getText().toString().equals("")) {
						new CreateMoveRequest().execute();

					} else {
						Toast.makeText(getBaseContext(),
								"Please enter phone number", Toast.LENGTH_SHORT)
								.show();
						senderET.findFocus();
					}

				} else {
					gps.showSettingsAlert();
				}

			}
		});

		btnAbort.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stopService(new Intent(MovingmodeOption.this,
						SendinMoveMode.class));

			}
		});

	}
	
	private int getIndex(Spinner spinner, String myString)
	 {
	  int index = 0;

	  for (int i=0;i<spinner.getCount();i++){
	   if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
	    index = i;
	   }
	  }
	  return index;
	 } 
	
	public void putBooleanInPreferences(boolean isChecked, String key) {
		SharedPreferences sharedPreferences = this
				.getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, isChecked);
		editor.commit();
	}

	public boolean getBooleanFromPreferences(String key) {
		SharedPreferences sharedPreferences = this
				.getPreferences(Activity.MODE_PRIVATE);
		Boolean isChecked = sharedPreferences.getBoolean(key, false);
		return isChecked;
	}

	public void putStringInPreferences(String value, String key) {
		SharedPreferences sharedPreferences = this
				.getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getStringFromPreferences(String key) {
		SharedPreferences sharedPreferences = this
				.getPreferences(Activity.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	private Boolean isServiceRunning(String serviceName) {

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo runningServiceInfo : activityManager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(runningServiceInfo.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public void hideOptions() {
		senderET.setEnabled(false);
		btnsearch.setEnabled(false);
		btnsend.setEnabled(false);
		btnAbort.setEnabled(false);
		intervalSpin.setEnabled(false);
		countlocSpin.setEnabled(false);
	}

	public void showOptions() {
		senderET.setEnabled(true);
		btnsearch.setEnabled(true);
		btnsend.setEnabled(true);
		btnAbort.setEnabled(true);
		intervalSpin.setEnabled(true);
		countlocSpin.setEnabled(true);
	}

	public void setLists() {
		intervalSpin = (Spinner) findViewById(R.id.timeinterval_spin);
		countlocSpin = (Spinner) findViewById(R.id.nolocation_spin);

		List<String> timeList = new ArrayList<String>();
		timeList.add("0.5");
		timeList.add("1");
		timeList.add("2");
		timeList.add("5");
		timeList.add("10");
		timeList.add("30");
		timeList.add("60");

		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, timeList);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		intervalSpin.setAdapter(dataAdapter1);

		List<String> countList = new ArrayList<String>();
		countList.add("5");
		countList.add("10");
		countList.add("15");
		countList.add("20");

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, countList);
		dataAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countlocSpin.setAdapter(dataAdapter2);
	}

	class CreateMoveRequest extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Progress dialog
			pDialog = new ProgressDialog(MovingmodeOption.this);
			pDialog.setMessage("Sending Moving mode request..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			String phoneNo = senderET.getText().toString();
			String timeInt = intervalSpin.getSelectedItem().toString();
			String count = countlocSpin.getSelectedItem().toString();
			String message = "#LFMovingModeRequest#" + timeInt + "#" + count
					+ "#";

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
					Toast.makeText(getBaseContext(),
							"Moving mode Request sent", Toast.LENGTH_SHORT)
							.show();
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

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(MovingmodeOption.this, Menu.class);
			startActivity(i);
		}

		return super.onKeyDown(keyCode, event);
	}
}
