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
import android.widget.EditText;
import android.widget.LinearLayout;

import swk.application.activities.R;
import swk.application.helper.FragmentIntentIntegrator;
import swk.application.helper.IntentIntegrator;
import swk.application.helper.IntentResult;

/**
 * Test zum QR-Scan �ber NavigationDrawer--> Ausblick
 * @author endres
 *
 */
public class QRScanner extends Fragment implements OnClickListener {

	final int SIGNATURE_ACTIVITY = 1;

	Button button;
	EditText result;
	EditText format;
	String content_zxing = "";
	String format_zxing = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_qr, container, false);

		button = new Button(getActivity());
		result = new EditText(getActivity());
		format = new EditText(getActivity());
		result.setText("Ergebnis");
		button.setText("QR");
		format.setText("Format");
		button.setOnClickListener(this);
		button.setGravity(Gravity.CENTER_HORIZONTAL);
		result.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.addView(button);
		linearLayout.addView(result);
		linearLayout.addView(format);
		return linearLayout;
	}

	@Override
	public void onClick(View v) {
		content_zxing = "";
		format_zxing = "";
		FragmentIntentIntegrator scanIntegrator = new FragmentIntentIntegrator(
				this);
		scanIntegrator.initiateScan();
		if (content_zxing.equals("") && format_zxing.equals("")) {
			content_zxing = getResources().getString(R.string.startup_error);
			format_zxing = getResources().getString(R.string.startup_error);
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check we have a valid result

		if (scanningResult != null) {
			try {
				content_zxing = new String(scanningResult.getContents());
				format_zxing = new String(scanningResult.getFormatName());
			} catch (Exception e) {
				result.setText(R.string.startup_error);
				format.setText(R.string.startup_error);
			}
		}
		result.setText(content_zxing);
		format.setText(format_zxing);
	}
}