package swk.application.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import swk.application.fragments.AgreementFragmentN;
import swk.application.fragments.CameraFragment;
import swk.application.fragments.QRScanner;
import swk.application.fragments.StandortTabLogik;
import swk.application.fragments.WebViewFragment;


/**
 * Activity zur Darstellung der Detailansicht. vor --> SelectionActivty nach
 *  AgreementActivity
 * 
 * @author endres
 *
 */
@SuppressWarnings("deprecation")
public class DetailActivity extends FragmentActivity {
	
	/**
	 * Layout f�r NavigationDrawer
	 */
	private DrawerLayout mDrawerLayout;
	/**
	 * List f�r NavigationDrawer
	 */
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	/**
	 * Drawer-Titel
	 */
	private CharSequence mDrawerTitle;
	/**
	 * Fragment-Titel
	 */
	private CharSequence mTitle;
	/**
	 * Drawer-Bestandteile
	 */
	private String[] mDrawerItmes;

	/**
	 * Activity spezifische onCreate-Methode --> verantwortlich f�r NavigationDrawer
	 *  und Layoutressourcenzuweisung 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle(
				"Kunde: " + getIntent().getStringExtra("customer_number"));

		mTitle = mDrawerTitle = getTitle();
		mDrawerItmes = getResources().getStringArray(R.array.drawer_titles);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// oepns
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add items to the ListView
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerItmes));
		// Set the OnItemClickListener so something happens when a
		// user clicks on an item.
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Set the default content area to item 0
		// when the app opens for the first time
		if (savedInstanceState == null) {
			navigateTo(1);
		}

	}

	/*
	 * If you do not have any menus, you still need this function in order to
	 * open or close the NavigationDrawer when the user clicking the ActionBar
	 * app icon.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			navigateTo(position);

		}
	}

	/**
	 * Drawerdefinitionen
	 * @param position
	 */
	private void navigateTo(int position) {
		switch (position) {
		case 0:

			// getSupportFragmentManager() .beginTransaction()
			 //.add(R.id.content_frame, ItemOne.newInstance(),
			 //ItemOne.TAG).commit();

			finish();
			startActivity(new Intent(getApplication(),
			SelectionActivity.class));
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;

		case 1:

			StandortTabLogik tabbedAdressFragment = new StandortTabLogik();
			Bundle bundle = new Bundle();
			bundle.putString("customer_number", getIntent().getExtras()
					.getString("customer_number"));

			tabbedAdressFragment.setArguments(bundle);
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, tabbedAdressFragment).commit();
			mDrawerLayout.closeDrawer(mDrawerList);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		case 2:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new AgreementFragmentN())
					.show(new AgreementFragmentN()).commit();
			mDrawerLayout.closeDrawer(mDrawerList);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		case 3:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new QRScanner())
					.show(new QRScanner()).commit();
			mDrawerLayout.closeDrawer(mDrawerList);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		case 4:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new CameraFragment())
					.show(new CameraFragment()).commit();
			mDrawerLayout.closeDrawer(mDrawerList);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		case 5:
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, WebViewFragment.newInstance(),
							WebViewFragment.TAG).commit();
			mDrawerLayout.closeDrawer(mDrawerList);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("Detail", "Backbutton gedr�ckt --> Zur�ck zu Selection");
			finish();
			startActivity(new Intent(getApplication(),
					SelectionActivity.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

