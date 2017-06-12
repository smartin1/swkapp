package swk.application.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.net.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import swk.application.helper.FactoryHelper;
import swk.application.helper.NetworkStateReceiver;
import swk.application.helper.NetworkStateReceiver.NetworkStateReceiverListener;
import swk.application.helper.PublicContext;

//import swk.application.helper.Custom_HttpClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>,
		NetworkStateReceiverListener {

	private static final double version = 1.3;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	FactoryHelper factoryHelper;
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private boolean connectionB = false;
	private Menu optionsMenu;
	private NetworkStateReceiver networkStateReceiver;
	private String callerActivity = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(getIntent()==null){
			Log.i("Intent LoginActivity","is null");
		}else{
			callerActivity = getIntent().getStringExtra("CallerActivity");
		}

		PublicContext.getInstance().loadProperties(getApplicationContext());

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();

		// Workaround!!!
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);




		networkStateReceiver = new NetworkStateReceiver();
		networkStateReceiver.addListener(this);
		this.registerReceiver(this.networkStateReceiver, new IntentFilter(
				android.net.ConnectivityManager.CONNECTIVITY_ACTION));

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);

	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		return true;
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String username;
		private final String mPassword;

		UserLoginTask(String email, String password) {
			username = email;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			boolean returnval = false;
			SharedPreferences preferences = getSharedPreferences("Login", 0);

			// encode data on your side using BASE64
			byte[] bytesEncoded = Base64.encodeBase64((username + ":" + mPassword).getBytes());
			String authEncoded = new String(bytesEncoded);
			Log.w("authenc",authEncoded);
			PublicContext.getInstance().setUsername(username);

			try {
					URL url = new URL("https://apigw.tk-bodensee.net/dev/v1.0/kunde/102274");
					HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

					con.setSSLSocketFactory(PublicContext.getInstance().factory());
					con.setRequestProperty("Authorization", "Basic " + authEncoded);
					Log.w("Response", con.getResponseMessage());
					Log.w("Response", ""+con.getResponseCode());
				//Login successfull
				if (con.getResponseCode() == 200) {
					// Disconnect connection
					con.disconnect();
					// Load Credentials
					SharedPreferences sp=getSharedPreferences("SWK_Application_Login", 0);
					// Check if Entry with accepted Username exists
					if(sp.contains(username)) {
						// if Username exists but Passmismatch
						if (!sp.getString(username, null).equals(authEncoded)) {
							//Update Credentials
							SharedPreferences.Editor Ed = sp.edit();
							Ed.remove(username);
							Ed.putString(username, authEncoded);
							Ed.commit();
							returnval = true;
						} else {
							//Username exists and Pass match
							returnval = true;
						}
					}else{
					// Create new Credentials
						SharedPreferences.Editor Ed=sp.edit();
						Ed.putString(username, authEncoded);
						Ed.commit();
						returnval = true;
					}
					returnval = true;
				}
			} catch (Exception e) {
				// if there are any Problems with the Server
				Log.e("Serverfailure", e.getMessage());
				SharedPreferences sp1=getSharedPreferences("SWK_Application_Login",0);
				if(sp1.contains(username)){
					if(authEncoded.equals(sp1.getString(username,null))){
						returnval =true;
					}
				}

			}
		
			return returnval;

		}
		

	private  SSLSocketFactory getFactory( InputStream pKeyFile, String pKeyPassword ) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException  {
		  KeyManagerFactory keyManagerFactory;
		
			  keyManagerFactory = KeyManagerFactory.getInstance("X509");
			  KeyStore keyStore = KeyStore.getInstance("PKCS12");


			  keyStore.load(pKeyFile, pKeyPassword.toCharArray());
		      pKeyFile.close();

			  keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

			  SSLContext context = SSLContext.getInstance("TLS");
			  context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
			  return context.getSocketFactory();
  
		}

		

		@Override
		protected void onPostExecute(final Boolean success) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			mAuthTask = null;
			showProgress(false);

			if (success) {
			unregisterReceiver(networkStateReceiver);
				if(callerActivity==null) {
					Intent i = new Intent(LoginActivity.this,
							SelectionActivity.class);
					finish();

					startActivity(i);
				}else{
					finish();
				}

			} else {
				
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		optionsMenu = menu;
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (connectionB) {
			menu.findItem(R.id.state_connect).setVisible(true);
			menu.findItem(R.id.state_disconnect).setVisible(false);
		} else {
			menu.findItem(R.id.state_connect).setVisible(false);
			menu.findItem(R.id.state_disconnect).setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.login_help) {
			new AlertDialog.Builder(this)
					.setTitle("About")
					.setMessage("SWK_Applikation V. " + version)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(R.drawable.ic_launcher).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void networkAvailable() {
		Log.w("login-Network", "connect");
		connectionB = true;
		if (optionsMenu != null) {
			onPrepareOptionsMenu(optionsMenu);
		}

	}

	@Override
	public void networkUnavailable() {
		Log.w("login-Network", "disconnect");
		connectionB = false;
		if (optionsMenu != null) {
			onPrepareOptionsMenu(optionsMenu);
		}

	}
}

