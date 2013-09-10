package com.example.showlocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationSender extends Activity{

	private ProgressDialog pDialog;
	
	JSONParser jsonparser=new JSONParser();
	
	EditText receiverID;
	Button btnSearch;
	Button btnSend;
	String lat;
	String lon;
	
	private static String url_create_location = "http://10.216.155.44/locationFinderTest1/create_location.php";
	 
	// JSON Node names
	 private static final String TAG_SUCCESS = "success";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sender_layout);
		
		receiverID=(EditText) findViewById(R.id.receiverIDET);
		btnSearch=(Button) findViewById(R.id.searchBtn);
		btnSend=(Button) findViewById(R.id.sendBtn);
		
		lat=Double.toString(getIntent().getExtras().getDouble("lati"));
		lon=Double.toString(getIntent().getExtras().getDouble("longi"));
		
		
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				new CreateNewLocation().execute();
				
			}
		});
		
	}
		class CreateNewLocation extends AsyncTask<String,String,String>
		{
			int success=0;
			@Override
			protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(LocationSender.this);
		        pDialog.setMessage("Sending location..");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(true);
		        pDialog.show();
		    }
			
			@Override
			protected String doInBackground(String... arg0) {
				
				String receiver=receiverID.getText().toString();
				String sender="0001";
				
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("senderID",sender));
				params.add(new BasicNameValuePair("receiverID",receiver));
				params.add(new BasicNameValuePair("lat",lat));
				params.add(new BasicNameValuePair("lon",lon));
				
				
				// getting JSON Object
	            // Note that create product url accepts POST method
				JSONObject json=jsonparser.makeHttpRequest(url_create_location,"POST", params);
				
				// check log cat fro response
	            Log.d("Create Response", json.toString());
	            
	         // check for success tag
	            try {
	                 success = json.getInt(TAG_SUCCESS);
	                          
	                	               
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
				
				return null;
								
			}
			
			 protected void onPostExecute(String file_url) {
		            // dismiss the dialog once done
				 if(success==1)
				 {
				 Toast.makeText(getApplicationContext(),"Location is succesfully sent ",Toast.LENGTH_SHORT).show();
				 } 
				 else{
					 Toast.makeText(getApplicationContext(),"Error in sending location... ",Toast.LENGTH_SHORT).show();
				 }
				 pDialog.dismiss();
		        }
		 
			
		}
		
		
	
	
}
