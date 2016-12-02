package com.fyp.protecta;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.protecta.JsonServiceHelper;
//import com.fyp.protecta.MainActivity.JsonService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterActivity extends Activity {
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
		String response = jsonResponse.optString("User");
		String sign_response = jsonResponse.optString("IMEI");
		//Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
		if(response.equalsIgnoreCase("correct"))
		{
			Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(), SettingActivity.class);
			startActivity(i);
			finish();
		}
		else if (response.equalsIgnoreCase("incorrect"))
		{
			Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
		}
		else if(sign_response.equalsIgnoreCase("notexist")){
			Toast.makeText(getApplicationContext(), "Username Does not Exist", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(), SignupActivity.class);
			startActivity(i);
			finish();
		}
		else if(sign_response.equalsIgnoreCase("exist")){
			Toast.makeText(getApplicationContext(), "This Device is Already Registered. Use Forget Password.", Toast.LENGTH_LONG).show();
		}
//		else
//		{
//			JSONObject jsonMainNode = jsonResponse.optJSONObject("User");
//		}
		

		} catch (Exception e) {
			Log.d(":) : ", e.getLocalizedMessage());
		}
	}

	Button btnGCMRegister;
	Button btnAppShare;
	Button signup;
	EditText username;
	EditText password;
	TextView forgot_pass;
	GoogleCloudMessaging gcm;
	Context context;
	String regId;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		context = getApplicationContext();
		
		forgot_pass = (TextView) findViewById(R.id.forgot_pass);
		forgot_pass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//call next activity to recover password.s
				Intent i = new Intent(getApplicationContext(), ForgotPassActivity.class);
				startActivity(i);
				finish();
			}
		});
		signup = (Button) findViewById(R.id.sign_up);
		signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//android.telephony.TelephonyManager.getDeviceId()
//				Intent sign = new Intent(getApplicationContext(), SignupActivity.class);
//				startActivity(sign);
//				finish();
				sendIMEI();
			}
		});
		btnGCMRegister = (Button) findViewById(R.id.btnGCMRegister);
		btnGCMRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if (TextUtils.isEmpty(regId)) {
//					//regId = registerGCM();
//					//Log.d("RegisterActivity", "GCM RegId: " + regId);
//				} else {
//				//	Toast.makeText(getApplicationContext(), "Already Registered with GCM Server!", Toast.LENGTH_LONG).show();
//				}
				attemptLogin();
			}
		});

		btnAppShare = (Button) findViewById(R.id.btnAppShare);
		btnAppShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(regId)) {
					Toast.makeText(getApplicationContext(), "RegId is empty!",
							Toast.LENGTH_LONG).show();
				} else {
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					i.putExtra("regId", regId);
					Log.d("RegisterActivity",
							"onClick of Share: Before starting main activity.");
					startActivity(i);
					finish();
					Log.d("RegisterActivity", "onClick of Share: After finish.");
				}
			}
		});
	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			Toast.makeText(getApplicationContext(),
					"RegId already available. RegId: " + regId,
					Toast.LENGTH_LONG).show();
		}
		return regId;
	}

	@SuppressLint("NewApi") private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(getApplicationContext(),
						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
						.show();
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}
	public void attemptLogin() {
		username = (EditText) findViewById(R.id.uname);
		password = (EditText) findViewById(R.id.passwd);
			JsonService jsonHelper = new JsonService();
			jsonHelper.activity = this;
//			jsonHelper.execute(getString(R.string.BaseURL)
//					+ getString(R.string.signIn) + "?eMail="
//					+ mEmail+"&uPass="+mPassword);
			List<NameValuePair> name = new ArrayList<NameValuePair> (5);
			name.add( new BasicNameValuePair("username", username.getText().toString()));
			name.add(new BasicNameValuePair("password", password.getText().toString()));
			name.add(new BasicNameValuePair("URL", "/Android/login.php"));
			jsonHelper.execute(name);
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.getDeviceId();
    //}
	}
	
	public void sendIMEI()
	{
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
		JsonService jsonHelper = new JsonService();
		jsonHelper.activity = this;
//		jsonHelper.execute(getString(R.string.BaseURL)
//				+ getString(R.string.signIn) + "?eMail="
//				+ mEmail+"&uPass="+mPassword);
		List<NameValuePair> name = new ArrayList<NameValuePair> (5);
		name.add( new BasicNameValuePair("IMEI", IMEI));
		name.add(new BasicNameValuePair("URL", "/Android/signup.php"));
		jsonHelper.execute(name);
	}
	
}
