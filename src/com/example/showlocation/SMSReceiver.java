package com.example.showlocation;

import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
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
					
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
