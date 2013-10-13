package com.example.showlocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Myinfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_layout);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(Myinfo.this, Menu.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}


	
}
