package swk.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

//import swk.application.helper.DropboxCheckout;

/**
 * Activity zur Darstellung des Splashscreens. InitialisierungsActivity
 * 
 * @author endres
 *
 */
public class SplashScreen extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 5000;
	private static String file_url = "https://dl.dropboxusercontent.com/u/12880539/Users%20Application.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		//TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//Log.w("TELEFONMANAGER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!","JA");
		//Log.w("ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", telephonyManager.getDeviceId());
		//new DownloadFileFromURL().execute(telephonyManager.getDeviceId());

		new Handler().postDelayed(new Runnable() {

		/*
		 * Showing splash screen with a timer. This will be useful when you want
		 * to show case your app logo / company
		 */

		 @Override
		 public void run() {
		 // This method will be executed once the timer is over
		 // Start your app main activity
		 Intent i = new Intent(SplashScreen.this, LoginActivity.class);
		 startActivity(i);
				 // close this activity
		 finish();
		 }
		 }, SPLASH_TIME_OUT);
	}

	private class DownloadFileFromURL extends
			AsyncTask<String, String, Boolean> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected Boolean doInBackground(String... imei) {
			Boolean b = true;
			try {
				// URL url = new
				// URL("https://dl.dropboxusercontent.com/u/12880539/Users%20Application.txt");
				// Scanner s = new Scanner(url.openStream());
				URLConnection connection = new URL(
						"https://dl.dropboxusercontent.com/u/12880539/Users%20Application.txt")
						.openConnection();
				Scanner scanner = new Scanner(connection.getInputStream());
				String text = scanner.useDelimiter("\\Z").next();
				
				Log.w("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",text);
				Log.w("DEVICE-ID", imei[0]);
				Log.w("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEVICE-ID", imei[0]);
//				if (text.toLowerCase().contains(imei[0].toLowerCase())) {
//					Log.w("IMEI-CHECK", "IMEI exists in list");
//					b = true;
//				} else {
//					b = false;
//					Log.w("IMEI-CHECK", "IMEI is not present in list");
//				}
				b=true;
				scanner.close();
			} catch (Exception e) {
				Log.w("Error: ", e);
				b = true;
			}

			return b;
		}

		@Override
		protected void onPostExecute(Boolean a) {
			super.onPostExecute(a);
			if (a) {
				Intent i = new Intent(SplashScreen.this, LoginActivity.class);
				startActivity(i);
				finish();
			} else {
				Intent i = new Intent(SplashScreen.this, ForbiddenScreen.class);
				startActivity(i);
				finish();
			}
		}

	}
}
