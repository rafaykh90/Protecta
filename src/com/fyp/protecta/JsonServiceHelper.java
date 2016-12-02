package com.fyp.protecta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.fyp.protecta.JsonServiceHelper;


class JsonServiceHelper extends AsyncTask<List<NameValuePair>, Void, String> {

	static HttpClient httpClient = new DefaultHttpClient();
	public Activity activity;

	protected String doInBackground(List<NameValuePair>... url) {
		return readJSONFeed(url[0]);
	}

	public String readJSONFeed(List<NameValuePair> test) {
		StringBuilder stringBuilder = new StringBuilder();
		String page = null;
			for(NameValuePair nvp : test)
			{
				if(nvp.getName().toString() == "URL")
				{
					page = nvp.getValue();
					Log.d("url", page);
					
				}
			}
		String URL = Config.APP_SERVER_URL + page;
		HttpPost httpGet = new HttpPost(URL);
		try {
//			Log.d("username", test.get(0).getValue().toString());
//			Log.i("passwd", test.get(1).getValue().toString());
			httpGet.setEntity(new UrlEncodedFormEntity(test));
			HttpResponse response = httpClient.execute(httpGet);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.d("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
			}
		return stringBuilder.toString();
	}

	protected void onProgressUpdate(Integer... progress) {

	}

	protected void onPostExecute(String result) {
		// ((MainPropertyList)activity).populateListView(result);
	}

}