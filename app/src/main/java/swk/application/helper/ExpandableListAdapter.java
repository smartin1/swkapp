package swk.application.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import swk.application.activities.R;
import swk.application.database.Auftrag;
import swk.application.database.DatabaseHandler;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	public interface OnClickListenerZxing {
		void onClick();

	}

	private Activity activity;
	private List<Auftrag> auftragList;
 	String content_zxing =null;
	EditText ftu_ETf = null;
	private GMailSender sender = new GMailSender("inbetriebnahmeapplikation@gmail.com",
			"hirD$829hrAd");
	private String customernumber;


	public ExpandableListAdapter(Activity activity, List<Auftrag> auftragList, String customernumber) {
		this.activity = activity;
		this.auftragList = auftragList;
		this.customernumber = customernumber;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return (this.auftragList.get(groupPosition));
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Object childType = getChild(groupPosition, childPosition);

		EditText tarif;
		if (convertView == null || convertView.getTag() != childType) {
			Auftrag child = (Auftrag) getChild(groupPosition, childPosition);
			LayoutInflater infalInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (child.getWfpath()) {

			/*
			 * Nur testweise auskommentiert
			 */

			// case "11100":
			// case "11200":
			// case "11300":
			// case "11400":
			// case "430":
			// convertView = infalInflater.inflate(R.layout.as_tel, null);
			// // tarif =
			// // (EditText)convertView.findViewById(R.id.as_tel_tarif);
			// // tarif.setText("TESTETSESTESTE");
			//
			// // convertView =
			// // infalInflater.inflate(R.layout.activity_splashscreen, null);
			// // listIcon.setImageDrawable(
			// // activity.getResources().getDrawable(
			// // R.drawable.telefonie));
			// break;
			//

			/*
				 * 
				 */

			//Telefonie
			case "430":
			case "11400":
				Log.w("SIPCHILD",child.getSip());
				if(child.getSip()!=null) {
					convertView = infalInflater.inflate(R.layout.as_tel, null);
					final EditText sipuser = (EditText) convertView.findViewById(R.id.as_tel_sipuser);
					final EditText von = (EditText) convertView.findViewById(R.id.as_tel_block_von);
					final EditText bis = (EditText) convertView.findViewById(R.id.as_tel_block_bis);
					final EditText pass = (EditText) convertView.findViewById(R.id.as_tel_pass);
					final EditText realm = (EditText) convertView.findViewById(R.id.as_tel_realm);
					final EditText rufnummer = (EditText) convertView.findViewById(R.id.as_tel_rufnummer);

					if(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip())!=null) {
						sipuser.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getSipuser());
						von.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getVon());
						bis.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getBis());
						pass.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getPass());
						realm.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getRealm());
						rufnummer.setText(DatabaseHandler.getInstance(activity.getApplicationContext()).getSip(child.getSip()).getRufnummer());
					}
				}

			break;


			case "12100":
			case "12200":
			case "12300":
			case "12900":
			case "431":
			case "11100":
			case "11200":
			case "11300":


				convertView = infalInflater.inflate(R.layout.as_internet, null);
				final EditText speedtestED = (EditText) convertView
						.findViewById(R.id.as_internet_speedtest_ED);
				Button speedtest = (Button) convertView
						.findViewById(R.id.as_internet_BT_speedtest);
				final ProgressBar bar_down = (ProgressBar) convertView
						.findViewById(R.id.as_internet_barDown);

				Button ftu_Scan_BT = (Button) convertView
						.findViewById(R.id.as_internet_ftu_Button);
				ftu_ETf = (EditText) convertView
						.findViewById(R.id.as_internet_ftu_ETf);
				EditText cpe_ET = (EditText) convertView.findViewById(R.id.as_internet_cpe_ET);
				EditText cpesn_ET = (EditText) convertView.findViewById(R.id.as_internet_cpe_);
				EditText downloadRate= (EditText) convertView.findViewById(R.id.as_internet_ET_download);
				EditText uploadRate= (EditText) convertView.findViewById(R.id.as_internet_ET_upload);
				// FTU-Button Listener
				ftu_Scan_BT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						IntentIntegrator scanIntegrator = new IntentIntegrator(
								activity);
						scanIntegrator.initiateScan();
					}
				});

				cpe_ET.setText("FRITZ!Box Fon WLAN 7390");
				ftu_ETf.setText("KN-960225");
				cpesn_ET.setText("3431C453EF50");
				downloadRate.setText("25 Mbit/s");
				uploadRate.setText("1 Mbit/s");
				Button ftu_Send_BT = (Button) convertView
						.findViewById(R.id.as_internet_sendFTU_BT);

				ftu_Send_BT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {

							sender.sendMail(
									"***Neue-FTU-Erfassung***",
									"Die folgende FTU wurde beim Kunden: "+customernumber+" erfasst:\n"+ftu_ETf.getText(),
									"martin.endres@live.de",
									"martin.endres@live.de");
							Toast.makeText(activity, "FTU erfolgreich übertragen!", Toast.LENGTH_SHORT).show();
							Log.e("SendMail", "Mail erfolgreich versendet");
						} catch (Exception e) {
							Toast.makeText(activity, "Fehler beim übertragen der FTU!", Toast.LENGTH_SHORT).show();
							Log.e("SendMail", e.getMessage(), e);
						}
					}

				});

				Button speedtestnet = (Button) convertView
						.findViewById(R.id.as_internet_speedtestnet);

				speedtestnet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							sender.sendMail("***Neue-FTU-Erfassung***",
									"Die folgende FTU wurde beim Kunden:\n"
											+ " erfasst.",
									"martin.endres@live.de",
									"martin.endres@live.de");
						} catch (Exception e) {
							Log.e("SendMail", e.getMessage(), e);
						}

						// String script = "/storage/sdcard1/test.py";
						// Intent intent = launch_sl4a_script(script);
						// Log.d("SL4A Launcher", "The Intent is " +
						// intent.toString());
						// activity.startActivity(intent);

					}
				});

				speedtest.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

				break;
			case "13100":
				// tarif =
				// (EditText)convertView.findViewById(R.id.as_tel_tarif);
				// tarif.setText("TESTETSESTESTE");

				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.lc));
			case "14100":

				convertView = infalInflater.inflate(
						R.layout.activity_splashscreen, null);
				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.serverhousing));
				break;
			case "15100":
			case "15200":

				convertView = infalInflater.inflate(R.layout.as_tel, null);

				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.dsl));
				break;
			case "16100":

				convertView = infalInflater.inflate(
						R.layout.activity_splashscreen, null);
				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.docsis));
				break;
			case "17100":
			case "17200":
			case "17300":

				convertView = infalInflater.inflate(R.layout.as_tv, null);
				// txtListChild = (TextView) convertView
				// .findViewById(R.id.a_tv);
				// txtListChild.setText("TV NEEUUUUUUU");
				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.tv));
				break;
			case "18100":

				convertView = infalInflater.inflate(
						R.layout.activity_splashscreen, null);
				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.haus));
				break;
			default:

				convertView = infalInflater.inflate(
						R.layout.activity_splashscreen, null);
				// listIcon.setImageDrawable(activity.getResources().getDrawable(
				// R.drawable.question));
			}
		}
		// convertView.invalidate();
		// TextView txtListChild = (TextView) convertView
		// .findViewById(R.id.inbetriebnahme_internet_cpe);
		// txtListChild.setText(child.getBuchungstext());

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.auftragList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.auftragList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_row, null);
		}

		TextView listItemTitle = (TextView) convertView
				.findViewById(R.id.row_title);
		listItemTitle.setTypeface(null, Typeface.BOLD);
		listItemTitle.setText(auftragList.get(groupPosition).getBuchungstext());

		TextView assignmentNumber = (TextView) convertView
				.findViewById(R.id.row_auftragsnummer);
		assignmentNumber.setTypeface(null, Typeface.BOLD);
		assignmentNumber.setText(auftragList.get(groupPosition)
				.getAuftragsnummer());

		TextView wfpath = (TextView) convertView.findViewById(R.id.row_wfpath);
		wfpath.setTypeface(null, Typeface.BOLD);
		wfpath.setText(auftragList.get(groupPosition).getWfpath());

		ImageView listIcon = (ImageView) convertView
				.findViewById(R.id.row_imageview);

		String wfpathid = auftragList.get(groupPosition).getWfpath();
		switch (wfpathid) {
		case "11100":
		case "11200":
		case "11300":
		case "11400":
		case "430":

			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.telefonie));
			break;
		case "12100":
		case "12200":
		case "12300":
		case "12900":
		case "431":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.internet));
			break;
		case "13100":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.lc));
		case "14100":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.serverhousing));
			break;
		case "15100":
		case "15200":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.dsl));
			break;
		case "16100":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.docsis));
			break;
		case "17100":
		case "17200":
		case "17300":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.tv));
			break;
		case "18100":
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.haus));
			break;
		default:
			listIcon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.question));
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * Implementiert die Notwendige Zxing-Logik
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		// erzeugt das ZXing Object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check ob gültiges Ergebnis

		if (scanningResult != null) {
			Log.w("ZXING",scanningResult.getContents());

			try {
				content_zxing = new String(scanningResult.getContents());
			} catch (Exception e) {
				content_zxing = new String("Fehler, konnte keine FTU ermitteln");
				Log.w("ZXING Exception",e.getMessage());
			}
		}
		ftu_ETf = (EditText) activity.findViewById(R.id.as_internet_ftu_ETf);
		Log.w("ET",ftu_ETf.toString());
		ftu_ETf.setTextColor(Color.RED);
		Log.w("ZXING_Content",content_zxing);

		ftu_ETf.setText(content_zxing);
		ftu_ETf.setText("Test");

	}




	// public static Intent buildStartInTerminalIntent(File script) {
	// final ComponentName componentName = new ComponentName(
	// "com.googlecode.android_scripting",
	// "com.googlecode.android_scripting.activity.ScriptingLayerServiceLauncher");
	// Intent intent = new Intent();
	// intent.setComponent(componentName);
	// intent.setAction("com.googlecode.android_scripting.action.LAUNCH_BACKGROUND_SCRIPT");
	// intent.putExtra("com.googlecode.android_scripting.extra.SCRIPT_PATH",
	// script.getAbsolutePath());
	// return intent;
	// } // buildStartInTerminalIntent
	//
	// public static Intent launch_sl4a_script(String scriptName) {
	// final ComponentName componentName =new ComponentName(
	// "com.googlecode.android_scripting",
	// "com.googlecode.android_scripting.activity.ScriptingLayerServiceLauncher");
	// Intent intent = new Intent();
	// intent.setComponent(componentName);
	// intent.setAction("com.googlecode.android_scripting.action.LAUNCH_BACKGROUND_SCRIPT");
	// Log.d("SL4A Launcher", "Launching script " + scriptName);
	// // intent.putExtra("com.google.ase.extra.SCRIPT_NAME", scriptName);
	// return intent;
	// }
}
