package swk.application.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import swk.application.database.DatabaseHandler;
import swk.application.helper.GSONDatabaseHandler;
import swk.application.helper.GSONHelper;
import swk.application.helper.IntentIntegrator;
import swk.application.helper.IntentResult;
import swk.application.helper.NetworkStateReceiver;
import swk.application.helper.NetworkStateReceiver.NetworkStateReceiverListener;
import swk.application.helper.SpaceTokenizer;

/**
 * Activity zur Darstellung der Auswahlansicht. vor --> LoginActivity nach
 * DetailActivity
 * 
 * @author endres
 *
 */
public class SelectionActivity extends Activity implements
		NetworkStateReceiverListener {

	// GUI-Bestandteile
	private MultiAutoCompleteTextView customerNrMACT = null;
	private MultiAutoCompleteTextView ftuNrMACT = null;
	private Activity activity = null;
	private Button ftu_Scan_BT = null;
	private NetworkStateReceiver networkStateReceiver;
	private boolean swkConnectionState = false;
	private Menu optionsMenu;
	private ProgressDialog progressDialog;
	private ArrayList<String> ftus = new ArrayList<String>();

	/**
	 * Activity spezifische onCreate-Methode --> verantwortlich f�r
	 * Auswahl-Logik und Layoutressourcenzuweisung
	 */


	//Handler Object to detect expiration of defined inactivity Time
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//Operate dependig of generated Message

			//msg == 1 --> Five Minutes of Inactivity detected
			if (msg.what == 1){
				Log.i("Timer", "Inactivity detected!");
				//Start LoginActivity (SelectionActivity changes in Pause-State)
				Intent loginAcitivity = new Intent(getApplicationContext(), LoginActivity.class);
				loginAcitivity.putExtra("CallerActivity", "SelectionActivity");
				startActivity(loginAcitivity);
			}
		}
	};

	@Override
	public void onUserInteraction() {
		//Reset Handler
		mHandler.removeMessages(1);
		//Start Timer
		mHandler.sendEmptyMessageDelayed(1, 300000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		activity = this;

		// Netzwerkstatusreceiver
		// --> Uberpr�ft ob eine Verbindung zum SWK-Netzwerk besteht
		networkStateReceiver = new NetworkStateReceiver();
		networkStateReceiver.addListener(this);
		this.registerReceiver(this.networkStateReceiver, new IntentFilter(
				android.net.ConnectivityManager.CONNECTIVITY_ACTION));

		setContentView(R.layout.activity_selection);

		ftu_Scan_BT = (Button) findViewById(R.id.Selection_FTUNrButton);

		// FTU-Scan Button Zuweisung
		ftu_Scan_BT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(activity, "gedrückt", Toast.LENGTH_SHORT).show();

				IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
				scanIntegrator.initiateScan();
			}
		});

		// Textfeldzuweisungen
		customerNrMACT = (MultiAutoCompleteTextView) findViewById(R.id.Selection_CustomerNr);
		customerNrMACT.setThreshold(2);
		customerNrMACT.setTokenizer(new SpaceTokenizer());

		ftuNrMACT = (MultiAutoCompleteTextView) findViewById(R.id.Selection_FTUNr);
		ftuNrMACT.setThreshold(2);
		ftuNrMACT.setTokenizer(new SpaceTokenizer());

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		customerNrMACT.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemSelected(parent.getItemAtPosition(position).toString());
			}
		});

		try {
			new GSONHelper(getBaseContext().getResources().openRawResource(R.raw.test1)).getMappingFTUKunde();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Listener
		customerNrMACT.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					try {
						if (new GSONHelper(getBaseContext().getResources().openRawResource(R.raw.test1)).getAllKundenNummer().contains(
								customerNrMACT.getText().toString())) {
							unregisterReceiver(networkStateReceiver);
							itemSelected(customerNrMACT.getText().toString());

						} else {
							Toast.makeText(getApplicationContext(),
									"Datensatz nicht vorhanden",
									Toast.LENGTH_SHORT).show();
						}

					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		});
	}

	// Starte Datenbezug
	protected void itemSelected(final String selected) {

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Einen Moment bitte...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
		Log.i("Selection", "Folgender Datensatz ausgewählt: " + selected);
		final Intent nextScreen = new Intent(getApplicationContext(),
				DetailActivity.class);
		final Context context=getApplicationContext();
		// Thread zur Vermeidung von GUI-Freeze
		new Thread() {
			@Override
			public void run() {
				GSONDatabaseHandler gsonDatabaseHandler = new GSONDatabaseHandler(
						getApplicationContext());
				gsonDatabaseHandler.getOrUpdateCustomer(selected);
				nextScreen.putExtra("customer_number", selected);
				startActivity(nextScreen);
				progressDialog.dismiss();
				progressDialog.cancel();
				finish();
				}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		optionsMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Bestandteile der ActionBar
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent a = new Intent(this, DetailActivity.class);
			startActivity(a);
			return true;
		} else if (id == R.id.about) {
			new AlertDialog.Builder(this)
					.setTitle("About")
					.setMessage(
							"SWK_Applikation V. " + "\n"
									+ "Stadtwerke Konstanz GmbH" + "\n"
									+ "Martin Endres" + "\n"
									+ "M.Endres@stadtwerke.konstanz.de")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (swkConnectionState) {
			menu.findItem(R.id.state_connect).setVisible(true);
			menu.findItem(R.id.state_disconnect).setVisible(false);
		} else {
			menu.findItem(R.id.state_connect).setVisible(false);
			menu.findItem(R.id.state_disconnect).setVisible(true);
		}
		return true;
	}

	@Override
	public void networkAvailable() {
		swkConnectionState = true;
		if (optionsMenu != null) {
			onPrepareOptionsMenu(optionsMenu);
		}
		try {
			customerNrMACT.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line,
					new GSONHelper(getResources().openRawResource(R.raw.test1)).getAllKundenNummer()));
			ArrayList<String[]> l= (ArrayList<String[]>) new GSONHelper(getResources().openRawResource(R.raw.test1)).getMappingFTUKunde();
			Log.w("liste Selection", "size: "+l.size());

			for(int j=0;j<l.size();j++){
				ftus.add(l.get(j)[0] + " | "+ l.get(j)[1]);
				Log.w("liste selection", "array[0] "+l.get(j)[0]);
			}
			ftuNrMACT.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ftus));
			//ftuNrMACT.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void networkUnavailable() {
		swkConnectionState = false;
		if (optionsMenu != null) {
			onPrepareOptionsMenu(optionsMenu);
		}
		customerNrMACT.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, DatabaseHandler
						.getInstance(this).getAllCustomerNr()));
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String sContent_Zxing = null;
		// erzeugt das ZXing Object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check ob gültiges Ergebnis
		if (scanningResult != null) {
			try {
				sContent_Zxing = new String(scanningResult.getContents());
			} catch (Exception e) {
				Toast.makeText(activity, R.string.startup_error,
						Toast.LENGTH_SHORT).show();
				sContent_Zxing = new String("ERROR");
			}
		}
		// setze Ergebnis

		ftuNrMACT.setText(sContent_Zxing);
		int cnt=0;
		ArrayList<String> customerNrs=new ArrayList<String>();
		for(int i=0; i<ftus.size();i++){
			if(ftus.get(i).contains(sContent_Zxing)){
				String[] out = ftus.get(i).split("\\|\\s");
				Log.w("FTU-SCAN"," substring : "+ out[1]);
				customerNrs.add(out[1]);
				cnt++;

			}
		}
		Log.w("FTU-Debug", customerNrs.get(0));
		if(cnt==1){
			Log.w("FTU-Debug-IN IF", customerNrs.get(0));
			itemSelected(customerNrs.get(0));
		}
	}
}