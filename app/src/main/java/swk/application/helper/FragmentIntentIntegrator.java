package swk.application.helper;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Integrator nur zu Testzwecken!
 * 
 * @author endres
 *
 */
public final class FragmentIntentIntegrator extends IntentIntegrator {

	private final Fragment fragment;

	public FragmentIntentIntegrator(Fragment fragment) {
		super(fragment.getActivity());
		this.fragment = fragment;
	}

	@Override
	protected void startActivityForResult(Intent intent, int code) {
		fragment.startActivityForResult(intent, code);
	}
}