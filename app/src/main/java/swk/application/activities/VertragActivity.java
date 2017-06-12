package swk.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.List;

import swk.application.database.Auftrag;
import swk.application.database.DatabaseHandler;
import swk.application.helper.ExpandableListAdapter;

public class VertragActivity extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<Auftrag> auftragList;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertrag);

		bundle = this.getIntent().getExtras();

		// Actionbarupdate
		getActionBar().setTitle("Vertrag " + bundle.getString("vertragid"));
		auftragList = DatabaseHandler.getInstance(getApplicationContext())
				.getAuftrag(bundle.getString("vertragid"));

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.ExpandableListView);

		listAdapter = new ExpandableListAdapter(this, auftragList,bundle.getString("customernumber"));

		// setting list adapter
		expListView.setAdapter(listAdapter);

		expListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
					int groupPosition = ExpandableListView
							.getPackedPositionGroup(id);

					Toast.makeText(getApplicationContext(),
							auftragList.get(groupPosition).getBuchungstext(),
							Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    listAdapter.onActivityResult(requestCode, resultCode, data);
	}

}
