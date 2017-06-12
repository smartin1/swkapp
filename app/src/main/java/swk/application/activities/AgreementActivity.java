package swk.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import swk.application.database.Auftrag;
import swk.application.database.DatabaseHandler;
import swk.application.helper.AgreementListAdapter;

/**
 * Activity zur Darstellung der Verträge. vor --> DetailActivity nach -->
 * Assignmentactivity
 * 
 * @author endres
 *
 */
public class AgreementActivity extends Activity {

	/**
	 * Arraylist über die Vertragsspezifischen Aufträge
	 */
	private ArrayList<Auftrag> produktList = new ArrayList<Auftrag>();

	/**
	 * Modellierte Listenansicht
	 */
	private ListView listView;
	/**
	 * Custom Listadapter
	 */
	private AgreementListAdapter adapter;
	/**
	 * Bundle für Datenaustausch
	 */
	private Bundle bundle;

	/**
	 * Activity spezifische onCreate-Methode --> verantwortlich für View-Logik
	 * und Layoutressourcenzuweisung Ist innerhalb dieser Activity für die
	 * Generierung der CustomList verantwortlich.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Settings
		setContentView(R.layout.agreement_activity);
		bundle = this.getIntent().getExtras();

		// Actionbarupdate
		getActionBar().setTitle("Vertrag: " + bundle.getString("vertragid"));

		// Erzeugung der CustomList
		listView = (ListView) findViewById(R.id.list);
		produktList = DatabaseHandler.getInstance(this).getAuftrag(
				bundle.getString("vertragid"));
		adapter = new AgreementListAdapter(this, produktList);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// Listview-Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// Start der entsprechenden AssignmentActivity
//				Intent nextScreen = new Intent(getApplicationContext(),
//						AssignmentActivity.class);

				Intent nextScreen = new Intent(getApplicationContext(),
						VertragActivity.class);
				
				nextScreen.putExtra("auftragsnummer", produktList.get(position)
						.getAuftragsnummer());
				startActivityForResult(nextScreen, 1);
			}
		});

	}

	/**
	 * Activity spezifische onCreateOptionsMenu-Methode -->TODO
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO -->MENU
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}