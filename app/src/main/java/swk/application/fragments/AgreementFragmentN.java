package swk.application.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import swk.application.activities.R;
import swk.application.helper.CaptureSignature;
/**
 * Nur zum TEST siehe NavigationDrawer
 * @author endres
 *
 */
public class AgreementFragmentN extends Fragment implements OnClickListener {

	final int SIGNATURE_ACTIVITY = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_agreement_n, container, false);

		Button signature = new Button(getActivity());
		signature.setText(R.string.agreement_signature);
		signature.setOnClickListener(this);
		signature.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.addView(signature);
		return linearLayout;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), CaptureSignature.class);
		startActivityForResult(intent, SIGNATURE_ACTIVITY);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case SIGNATURE_ACTIVITY:

			if (resultCode == getActivity().RESULT_OK) {
				Bundle bundle = data.getExtras();
				String status = bundle.getString("status");
				if (status.equalsIgnoreCase("done")) {
					Toast toast = Toast
							.makeText(getActivity(),
									"Signature capture successful!",
									Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 105, 50);
					toast.show();
				}
			}
			break;
		}
	}

	public static AgreementFragmentN newInstance() {
		return new AgreementFragmentN();
	}
}
