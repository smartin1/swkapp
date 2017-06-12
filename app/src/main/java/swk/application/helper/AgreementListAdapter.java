package swk.application.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import swk.application.activities.R;
import swk.application.database.Auftrag;
import swk.application.database.DatabaseHandler;
/**
 * Custom List Adapter f�r Listview bei Auftrags�bersicht
 * @author endres
 *
 */
public class AgreementListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Auftrag> auftragList;

	public AgreementListAdapter(Activity activity, List<Auftrag> auftragList) {
		this.activity = activity;
		this.auftragList = auftragList;
	}

	public AgreementListAdapter(Activity activity, String vertragid) {
		this.activity = activity;
		this.auftragList = DatabaseHandler.getInstance(
				activity.getApplicationContext()).getAuftrag(vertragid);
	}

	@Override
	public int getCount() {
		return auftragList.size();
	}

	@Override
	public Object getItem(int location) {
		return auftragList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		ImageView listIcon = (ImageView) convertView
				.findViewById(R.id.row_imageview);
		TextView title = (TextView) convertView.findViewById(R.id.row_title);
		TextView wfpath = (TextView) convertView.findViewById(R.id.row_auftrags);
		TextView genre = (TextView) convertView.findViewById(R.id.row_wfpath);
		TextView vertragid = (TextView) convertView.findViewById(R.id.row_auftragsnummer);

		// Bildhandling fuer Listenansicht abhaengig von der wfpathid
		Auftrag m = auftragList.get(position);
		String wfpathid = auftragList.get(position).getWfpath();
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

		// title
		title.setText(m.getBuchungstext());

		// Wfpath
		wfpath.setText("WFPath: " + String.valueOf(m.getWfpath()));

		genre.setText(" " + String.valueOf(m.getAuftragsnummer()));

		// Vertragid
		vertragid.setText(String.valueOf(m.getVertragid()));

		return convertView;
	}

}