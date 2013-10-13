package com.example.showlocation;

import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	SmsMessage currentMessage;

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			final Bundle bundle = intent.getExtras();

			try {
				if (bundle != null) {
					Object[] pduObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pduObj.length; i++) {
						currentMessage = SmsMessage
								.createFromPdu((byte[]) pduObj[i]);

					}

					String phoneNumber = currentMessage.getOriginatingAddress();
					String Msg = currentMessage.getDisplayMessageBody()
							.toString();

					if (Msg.startsWith("@locationfinder#")) {

						Toast.makeText(context, Msg, Toast.LENGTH_LONG).show();
						abortBroadcast();
						Bundle basket = new Bundle();
						basket.putString("Msg", Msg);
						basket.putString("phoneNumber", phoneNumber);
						Intent i = new Intent(context, SMSreceiveOption.class);
						i.putExtras(basket);

						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);

					}
					else if(Msg.startsWith("#locationfinder#"))
					{
						Bundle basket = new Bundle();
						basket.putString("phoneNumber", phoneNumber);
						Intent i = new Intent(context, RequestOption.class);
						i.putExtras(basket);

						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					}
					else if(Msg.startsWith("#LFMovingModeRequest#"))
					{
						Bundle basket = new Bundle();
						basket.putString("Msg", Msg);
						basket.putString("phoneNumber", phoneNumber);
						Intent i = new Intent(context, MovingModereceiver.class);
						i.putExtras(basket);

						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					}
					else if(Msg.startsWith("#LFMovingModeON#"))
					{
						Intent i=new Intent(context,SendinMoveMode.class);
						i.putExtra("Msg", Msg);
						i.putExtra("phoneNumber",phoneNumber);
						
						context.startService(i);
					}
					else if(Msg.startsWith("@locationfinderMovingMode#"))
					{
						StringTokenizer st = new StringTokenizer(Msg, "#");
						st.nextToken();
						String smLat = st.nextToken();
						final String latitude = smLat.substring(4);
						String smlon = st.nextToken();
						final String longitude = smlon.substring(4);
						final String date = st.nextToken();
						
						LocationObj loc = new LocationObj(phoneNumber, latitude,
								longitude, date);
						
						Intent i = new Intent(context, MapLocation.class);

						i.putExtra("LocObj", loc);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					}
					
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
