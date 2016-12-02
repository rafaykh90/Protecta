package com.fyp.protecta;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassActivity extends Activity {

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
		Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
		if(response.equalsIgnoreCase("create new account"))
		{
			
		}
		else if (response.equalsIgnoreCase("incorrect password"))
		{
			Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
		}
		

		} catch (Exception e) {
			Log.d(":) : ", e.getLocalizedMessage());
		}
	}
	
	EditText email;
	Button send;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pass);
		send.findViewById(R.id.sign);
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_pass, menu);
		return true;
	}
	


public void attemptLogin() {

			email = (EditText) findViewById(R.id.email);
			JsonService jsonHelper = new JsonService();
			jsonHelper.activity = this;
//			jsonHelper.execute(getString(R.string.BaseURL)
//					+ getString(R.string.signIn) + "?eMail="
//					+ mEmail+"&uPass="+mPassword);
			List<NameValuePair> name = new ArrayList<NameValuePair> (5);
			name.add( new BasicNameValuePair("email", email.getText().toString()));
			name.add(new BasicNameValuePair("URL", "/Android/forgetpass.php"));
			jsonHelper.execute(name);
    //}
	}

}
