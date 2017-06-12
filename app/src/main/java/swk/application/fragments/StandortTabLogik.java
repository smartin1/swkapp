package swk.application.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import swk.application.activities.R;
import swk.application.database.DatabaseHandler;
import swk.application.database.Kunde;
import swk.application.database.Standort;

/**
 * Logikklasse f�r die Tabbare Adressansicht --> Verkn�pft die vorhandenen Standorte mittels Swipebarer Seite zusammen
 * 
 * @author endres
 *
 */
public class StandortTabLogik extends Fragment {

	// Logik-Bestandteile
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ArrayList<Standort> locationList;
	private Kunde customer;
	private Bundle args;
	private String customerNr;
	private ViewPager mViewPager;

	public StandortTabLogik newInstance() {
		return new StandortTabLogik();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		customerNr = new Bundle(getArguments()).getString("customer_number");
		customer = DatabaseHandler.getInstance(
				getActivity().getApplicationContext()).getKunde(customerNr);
		locationList = DatabaseHandler.getInstance(
				getActivity().getApplicationContext()).getAllStandort(
				customerNr);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_item_one, container, false);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getChildFragmentManager());

		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		return v;
	}

	// Logik fuer den Sektionspager hier werden den einzelnen Fragmente die
	// entsprechenden Parameter uebergeben
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// Parameteruebergabe abhaengig von der Pagerposition
			Fragment fragment = new TabbedStandortFragment();
			args = new Bundle();
			
			Log.w("Debug Test", "Kundennummer = " + customer.getKundennummer());
			args.putString("kundennummer", customer.getKundennummer());
			args.putString("art", customer.getArt());
			args.putString("anrede", customer.getAnrede());
			args.putString("titel", customer.getTitel());
			args.putString("name", customer.getName());
			args.putString("telefon", customer.getTelefon());
			args.putString("mobil", customer.getMobil());
			args.putString("mail", customer.getMail());
			args.putString("telefax", customer.getTelefax());
			args.putString("strasse", locationList.get(position).getStrasse());
			args.putString("hhnr", locationList.get(position).getHhnr());
			args.putString("hhnrzusatz", locationList.get(position)
					.getHhnrzusatz());
			args.putString("plz", locationList.get(position).getPlz());
			args.putString("ort", locationList.get(position).getOrt());
			args.putString("standortid", locationList.get(position)
					.getStandortid());

			fragment.setArguments(args);
			return fragment;
		}

		// Anzahl der Standortfragmente
		@Override
		public int getCount() {
			return locationList.size();
		}

		// Titeldefinition der Standortfragmente
		@Override
		public CharSequence getPageTitle(int position) {
			for (int i = 0; i < getCount();) {
				return locationList.get(position).getStandortid();
			}
			return null;
		}
	}
}
