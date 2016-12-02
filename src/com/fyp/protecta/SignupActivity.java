package com.fyp.protecta;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.fyp.protecta.JsonServiceHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

	private class JsonService extends JsonServiceHelper {
		@Override
		protected void onPostExecute(String result) {
			jsonResponce(result);
		}
	}
	JsonService jsonHelper = new JsonService();
	
	private void jsonResponce(String result)
	{
		//showProgress(false);
		try {
		JSONObject jsonResponse = new JSONObject(result);
		String response = jsonResponse.optString("SIGNUP");
		//Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
		if(response.equalsIgnoreCase("UE"))
		{
			Toast.makeText(getApplicationContext(), "Username Exist", Toast.LENGTH_LONG).show();
			username.setError("Username Exist");
			username.requestFocus();
		}
		else if (response.equalsIgnoreCase("EE"))
		{
			Toast.makeText(getApplicationContext(), "Email Address Already Registered", Toast.LENGTH_LONG).show();
			email_address.setError("Username Exist");
			email_address.requestFocus();
		}
		else if (response.equalsIgnoreCase("SS"))
		{
			Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(), SettingActivity.class);
			startActivity(i);
			finish();
		}
//		else
//		{
//			JSONObject jsonMainNode = jsonResponse.optJSONObject("User");
//		}
		

		} catch (Exception e) {
			Log.d(":) : ", e.getLocalizedMessage());
		}
	}
	Button signup;
	EditText first_name;
	EditText last_name;
	EditText username;
	EditText password;
	EditText repeat_pass;
	EditText email_address;
	String IMEI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		

		password = (EditText) findViewById(R.id.pwd);
		repeat_pass = (EditText) findViewById(R.id.re_pwd);
		signup = (Button) findViewById(R.id.sign);
		signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
//				if(password.getText().toString() == repeat_pass.getText().toString())
//				{
//					attemptLogin();
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "Passwords Does Not Match", Toast.LENGTH_LONG).show();
//				}
				attemptLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}
	
	public void attemptLogin() {
		first_name = (EditText) findViewById(R.id.f_name);
		last_name = (EditText) findViewById(R.id.L_name);
		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.pwd);
		repeat_pass = (EditText) findViewById(R.id.re_pwd);
		email_address = (EditText) findViewById(R.id.email);
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		
			JsonService jsonHelper = new JsonService();
			jsonHelper.activity = this;
//			jsonHelper.execute(getString(R.string.BaseURL)
//					+ getString(R.string.signIn) + "?eMail="
//					+ mEmail+"&uPass="+mPassword);
			List<NameValuePair> name = new ArrayList<NameValuePair> (8);
			name.add( new BasicNameValuePair("fname", first_name.getText().toString()));
			name.add(new BasicNameValuePair("lname", last_name.getText().toString()));
			name.add(new BasicNameValuePair("username", username.getText().toString()));
			name.add(new BasicNameValuePair("password", password.getText().toString()));
			name.add(new BasicNameValuePair("email", email_address.getText().toString()));
			name.add(new BasicNameValuePair("IMEI", IMEI));
			name.add(new BasicNameValuePair("signup", "true"));
			name.add(new BasicNameValuePair("URL", "/Android/signup.php"));
			jsonHelper.execute(name);
    //}
	}


}
