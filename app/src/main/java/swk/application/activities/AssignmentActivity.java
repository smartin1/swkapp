package swk.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import swk.application.helper.IntentIntegrator;
import swk.application.helper.IntentResult;

/**
 * Activity zur Darstellung der Inbetriebnahme. vor --> AgreementActivity nach
 * -
 * 
 * --> Achtung nur Entwurf!
 * 
 * @author endres
 *
 */
public class AssignmentActivity extends Activity {
	/**
	 * View-Bestandteil Button button
	 */
	Button button;
	/**
	 * View-Bestandteil EditText ftu
	 */
	EditText ftu;
	/**
	 * View-Bestandteil EditText cpe
	 */
	EditText cpe;
	/**
	 * View-Bestandteil EditText seriennummer
	 */
	EditText seriennummer;
	/**
	 * View-Bestandteil Button zfa
	 */
	Button zfa;
	/**
	 * String f�r Zxing-Funktionalit�t
	 */
	String content_zxing = "";
	/**
	 * String f�r Zxing-Funktionalit�t
	 */
	String format_zxing = "";

	/**
	 * Activity spezifische onCreate-Methode --> verantwortlich f�r Begrenzte
	 * Fenster-Logik und Layoutressourcenzuweisung Ist innerhalb dieser Activity
	 * f�r die Inbetriebnahme verantwortlich.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Zuweisungen
		setContentView(R.layout.activity_inbetriebnahme);
		final Bundle bundle = getIntent().getExtras();
		getActionBar().setIcon(R.drawable.internet);
		getActionBar().setTitle(bundle.getString("auftragsnummer"));
		

		//Custom Screenview
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.gravity = Gravity.CENTER;
		wmlp.height = 1200;
		wmlp.windowAnimations = android.R.style.Animation_Translucent;

		//View-Layout zuweisung
		button = (Button) findViewById(R.id.inbetriebnahme_internet_button);
		ftu = (EditText) findViewById(R.id.inbetriebnahme_internet_ftu);
		cpe = (EditText) findViewById(R.id.inbetriebnahme_internet_cpe);
		cpe.setText("FRITZ!Box Fon WLAN 7390");
		zfa = (Button) findViewById(R.id.inbetriebnahme_internet_buttonzfa);
		seriennummer = (EditText) findViewById(R.id.inbetriebnahme_internet_seriennummer);
		seriennummer.setText("0869D7496BCC");

		//FTU-Button Listener
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				IntentIntegrator scanIntegrator = new IntentIntegrator(
						AssignmentActivity.this);
				scanIntegrator.initiateScan();

			}

		});
		
		
		//ZFA-�bertragbutton Listener
		zfa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"84e3403688362d3ac5b334f02cbe1a9ef43efa6c", ""
										.toCharArray());
					}
				});
			
				try {

				    URL url1 = new URL("https://apigw.tk-bodensee.net/dev/v1.0/produkt/"+bundle.getString("auftragsnummer")+"/servicelog/");
				    HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
				    conn.setDoOutput(true);
				    conn.setRequestMethod("POST");
				    conn.setRequestProperty("Content-Type", "application/json");
				    TimeZone tz = TimeZone.getTimeZone("UTC");
				    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
				    df.setTimeZone(tz);
				    String nowAsISO = df.format(new Date());
				    String input = "{\"auftragid\":\""+bundle.getString("auftragsnummer")+"\",\"servicelog_created\":\""+nowAsISO+"\",\"servicelog_created_by\":\"Name\",\"servicelog_type\":\"art\",\"servicelog_text\":\""+"FTU="+ftu.getText()+"\",\"servicelog_jsonprops\":\"1\"}";

				    OutputStream os = conn.getOutputStream();
				    os.write(input.getBytes());
				    os.flush();

				    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
						Toast.makeText(getApplicationContext(), "Es ist ein Fehler aufgetreten",
								Toast.LENGTH_SHORT).show();
				        throw new RuntimeException("Failed : HTTP error code : "
				            + conn.getResponseCode());
				    }

				    BufferedReader br = new BufferedReader(new InputStreamReader(
				            (conn.getInputStream())));
				    Toast.makeText(getApplicationContext(), "Daten �bermittelt",
							Toast.LENGTH_SHORT).show();
				    String output;
				    System.out.println("Output from Server .... \n");
				    while ((output = br.readLine()) != null) {
				        System.out.println(output);
				    }

				    conn.disconnect();

				  } catch (MalformedURLException e) {

				    e.printStackTrace();

				  } catch (IOException e) {

				    e.printStackTrace();

				 }

				
			}

		});

	}

	/**
	 * Implementiert die Notwendige Zxing-Logik
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// erzeugt das ZXing Object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check ob g�ltiges Ergebnis
		if (scanningResult != null) {
			try {
				content_zxing = new String(scanningResult.getContents());
				format_zxing = new String(scanningResult.getFormatName());
			} catch (Exception e) {
				ftu.setText(R.string.startup_error);

			}
		}
		ftu.setText(content_zxing);
	}

}
