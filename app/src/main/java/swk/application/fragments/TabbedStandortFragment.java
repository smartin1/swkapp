package swk.application.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;

import swk.application.activities.R;
import swk.application.activities.VertragActivity;
import swk.application.database.DatabaseHandler;
import swk.application.database.Vertrag;

/**
 * Tabbare (Swiping) Standortansicht als Fragment
 * @author endres
 *
 */
public class TabbedStandortFragment extends Fragment {

	// View-Bestandteile
	private EditText art;
	private EditText anrede;
	private EditText titel;
	private EditText name;
	private EditText telefon;
	private EditText mobil;
	private EditText mail;
	private EditText telefax;
	private EditText strasse;
	private EditText hhnr;
	private EditText hhnrzusatz;
	private EditText plz;
	private EditText ort;
	private Button button;
	private NumberPicker numberPicker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tabbed_standort,
				container, false);
		// Bundle mit den Parametern aus der Klasse /StandortTabLogik/
		final Bundle bundle = this.getArguments();

		// Layout-Verknuepfungen
		art = (EditText) rootView.findViewById(R.id.standort_art);
		anrede = (EditText) rootView.findViewById(R.id.standort_anrede);
		titel = (EditText) rootView.findViewById(R.id.standort_titel);
		name = (EditText) rootView.findViewById(R.id.standort_name);
		telefon = (EditText) rootView.findViewById(R.id.standort_telefon);
		mobil = (EditText) rootView.findViewById(R.id.standort_mobil);
		mail = (EditText) rootView.findViewById(R.id.standort_email);
		telefax = (EditText) rootView.findViewById(R.id.standort_telefax);
		strasse = (EditText) rootView.findViewById(R.id.standort_strasse);
		hhnr = (EditText) rootView.findViewById(R.id.standort_hhnr);
		hhnrzusatz = (EditText) rootView.findViewById(R.id.standort_hhnrzusatz);
		plz = (EditText) rootView.findViewById(R.id.standort_plz);
		ort = (EditText) rootView.findViewById(R.id.standort_ort);
		numberPicker = (NumberPicker) rootView
				.findViewById(R.id.standort_numberpicker);
		button = (Button) rootView.findViewById(R.id.standort_button);

		// Bundle zu View Verknuepfungen
		art.setText(bundle.getString("art"));
		anrede.setText(bundle.getString("anrede"));
		titel.setText(bundle.getString("titel"));
		name.setText(bundle.getString("name"));
		telefon.setText(bundle.getString("telefon"));
		mobil.setText(bundle.getString("mobil"));
		mail.setText(bundle.getString("mail"));
		telefax.setText(bundle.getString("telefax"));
		strasse.setText(bundle.getString("strasse"));
		hhnr.setText(bundle.getString("hhnr"));
		hhnrzusatz.setText(bundle.getString("hhnrzusatz"));
		plz.setText(bundle.getString("plz"));
		ort.setText(bundle.getString("ort"));

		// Bezug der Vertr�ge abhaengig vom Standort und der Kundennummer
		ArrayList<Vertrag> vertragList = DatabaseHandler.getInstance(
				getActivity().getApplicationContext()).getAllVertrag(
				bundle.getString("standortid"),
				bundle.getString("kundennummer"));

		// vertragList zu Stringarray Konvertierung (NumberPicker benoetigt ein
		// Stringarray)
		final String[] values = new String[vertragList.size()];
		for (int i = 0; i < vertragList.size(); i++) {
			values[i] = vertragList.get(i).getVertragid();
		}

		Log.w("Standort-Debug",  "Values-Array = " + values.length);

		// Fokushandling innerhalb des Fragments--> Fokus wird hierdurch auf den
		// Button gelegt, da der Fokus innerhalb des Numberpickers den Text
		// markiert hat
		numberPicker
				.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		// Numberpickerkonfiguration
		numberPicker.setMinValue(0);
		if(values.length!=0) {
			numberPicker.setMaxValue(values.length - 1);
			numberPicker.setDisplayedValues(values);
		}else{
			numberPicker.setMaxValue(0);
		}

		numberPicker.setWrapSelectorWheel(false);


		// Button Listener-->abhaengig vom Numberpickervalue Vertrag in
		// Detailansicht
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent nextScreen = new Intent(getActivity()
						.getApplicationContext(), VertragActivity.class);
				nextScreen.putExtra("vertragid",
						new String(values[numberPicker.getValue()]));
				nextScreen.putExtra("customernumber", bundle.getString("kundennummer"));
				startActivity(nextScreen);
			}
		});

		return rootView;
	}
}
