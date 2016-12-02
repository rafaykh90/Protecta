package com.fyp.protecta;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	final SettingsValues global = (SettingsValues) getApplicationContext();
	EditText contact1;
	EditText contact2;
	EditText contact3;
	CheckedTextView wipeSD;
	CheckedTextView wipeMessage;
	CheckedTextView lockPhone;
	Button saveSettings;
	
	
	private class JsonService extends JsonServiceHelper {
		@Override
		protected void onPostExecute(String result) {
			jsonResponce(result);
		}
	}
	JsonService jsonHelper = new JsonService();
	
	
	
	private void jsonResponce(String result)
	{
		contact1 = (EditText) findViewById(R.id.contact_1);
		contact2 = (EditText) findViewById(R.id.contact_2);
		contact3 = (EditText) findViewById(R.id.contact_3);
		wipeSD = (CheckedTextView) findViewById(R.id.wipe_sd);
		wipeMessage = (CheckedTextView) findViewById(R.id.wipe_message);
		lockPhone = (CheckedTextView) findViewById(R.id.lock_phone);
		
		try {
		JSONObject jsonResponse = new JSONObject(result);
		String num1 = jsonResponse.optString("num1");
		String num2 = jsonResponse.optString("num2");
		String num3 = jsonResponse.optString("num3");
		String res_wipeSD = jsonResponse.optString("WipeData");
		String res_wipemess = jsonResponse.optString("WipeMsg");
		String res_lockphone = jsonResponse.optString("LockPhone");
		String response = jsonResponse.optString("save");
		wipeSD.setChecked(true);
		//Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
		if(num1 != null || num2 != null || num3 != null || res_wipeSD != null || res_wipemess != null
				|| res_lockphone != null)
		{
			contact1.setText(num1);
			global.setContact1(num1);
			contact2.setText(num2);
			global.setContact2(num2);
			contact3.setText(num3);
			global.setContact3(num3);
			if(res_wipeSD == "true"){
				wipeSD.setChecked(true);
				global.setWipesd("true");
			}
			else
				global.setWipesd("false");
			if(res_wipemess == "true"){
				wipeMessage.setChecked(true);
				global.setWipemsg("true");
			}
			else
				global.setWipemsg("false");
			if(res_lockphone == "true"){
				lockPhone.setChecked(true);
				global.setLockph("true");
			}
			else
				global.setLockph("false");
		}
		else if(response.equalsIgnoreCase("save"))
			Toast.makeText(getApplicationContext(), "Settings Successfully Saved", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Log.d(":) : ", e.getLocalizedMessage());
		}
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		getData();
		
		saveSettings = (Button) findViewById(R.id.save_settings);
		saveSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				attemptLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	
	public void getData() {
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
			JsonService jsonHelper = new JsonService();
			jsonHelper.activity = this;
			List<NameValuePair> name = new ArrayList<NameValuePair> (3);
			name.add( new BasicNameValuePair("IMEI", IMEI));
			name.add(new BasicNameValuePair("URL", "/Android/settings.php"));
			jsonHelper.execute(name);
	}
	public void attemptLogin() {
		String WS, WM, LP;
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
		contact1 = (EditText) findViewById(R.id.contact_1);
		contact2 = (EditText) findViewById(R.id.contact_2);
		contact3 = (EditText) findViewById(R.id.contact_3);
		wipeSD = (CheckedTextView) findViewById(R.id.wipe_sd);
		wipeMessage = (CheckedTextView) findViewById(R.id.wipe_message);
		lockPhone = (CheckedTextView) findViewById(R.id.lock_phone);
		if(wipeSD.isChecked()){
			WS = "true";
			global.setWipesd("true");
		}
		else{
			WS= "false";
			global.setWipesd("false");
		}
		if(wipeMessage.isChecked()){
			WM = "true";
			global.setWipemsg("true");
		}
		else{
			WM= "false";
			global.setWipemsg("false");
		}
		if(lockPhone.isChecked()){
			LP = "true";
			global.setLockph("true");
		}
		else{
			LP= "false";
			global.setLockph("false");
		}
		global.setContact1(contact1.getText().toString());
		global.setContact2(contact2.getText().toString());
		global.setContact3(contact3.getText().toString());
		
			JsonService jsonHelper = new JsonService();
			jsonHelper.activity = this;
			List<NameValuePair> name = new ArrayList<NameValuePair> (10);
			name.add(new BasicNameValuePair("contact1", contact1.getText().toString()));
			name.add(new BasicNameValuePair("contact2", contact2.getText().toString()));
			name.add(new BasicNameValuePair("contact3", contact3.getText().toString()));
			name.add(new BasicNameValuePair("sd", WS));
			name.add(new BasicNameValuePair("message", WM));
			name.add(new BasicNameValuePair("lock", LP));
			name.add( new BasicNameValuePair("IMEI", IMEI));
			name.add(new BasicNameValuePair("URL", "/Android/save.php"));
			jsonHelper.execute(name);
	}

}
