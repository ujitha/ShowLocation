package com.example.showlocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendinMoveMode extends Service {

	private boolean isRunning;
	String Number;
	Double period;
	int count;
	private GPSTracker gps;

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		setRunning(true);
		gps = new GPSTracker(this);

		String Msg = intent.getStringExtra("Msg");
		Number = intent.getStringExtra("phoneNumber");
		StringTokenizer st = new StringTokenizer(Msg, "#");
		st.nextToken();
		String timeInt = st.nextToken();
		String cnt = st.nextToken();

		period = Double.parseDouble(timeInt);
		count = Integer.parseInt(cnt);

		int num = 0;

		do {
			new CreateNewMessage().execute();
			try {
				Thread.sleep((long) (period * 1000 * 60));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}

			num++;
		} while (num < count);

		stopSelf();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		setRunning(false);
		Toast.makeText(getBaseContext(), "Stop the service", Toast.LENGTH_LONG)
				.show();
	}

	class CreateNewMessage extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Double lat = gps.getLatitude();
			Double lon = gps.getLongitude();

			String lati = String.valueOf(lat);
			String longi = String.valueOf(lon);
			String addr = "";

			Geocoder geocoder = new Geocoder(getBaseContext());
			try {
				List<Address> address = geocoder.getFromLocation(lat, lon, 1);

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

			// Should get sender mobile number

			String message = "@locationfinderMovingMode#Lat-" + lati + "#Lon-" + longi
					+ "#" + date + "#" + addr + "#";

			if (Number.length() > 0) {
				
				sendSMS(Number, message);
			} else {
				Toast.makeText(getBaseContext(), "Error with the phone number",
						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {

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

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

}
