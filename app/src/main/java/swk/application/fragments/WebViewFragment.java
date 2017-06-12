package swk.application.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import swk.application.activities.R;

/**
 * Diese Klasse implementiert den internen Appbrowser. �ber ihn kann die
 * Homepage der SWK innerhalb der App aufgerufen werden und dem Kunden
 * gegebenenfalls Informationen �ber das Angebotskontingent der SWK gegeben
 * werden.
 * 
 * @author endres
 */
public class WebViewFragment extends Fragment {

	public final static String TAG = WebViewFragment.class.getSimpleName();

	public static Fragment newInstance() {
		return new WebViewFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_webview, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		WebView webView = (WebView) view.findViewById(R.id.webView);

		webView.loadUrl("www.google.de");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

}
