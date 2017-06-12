package swk.application.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * NetworkStatusReceiver --> Handled die Internetanbindungsunterschiede
 * @author endres
 *
 */

public class NetworkStateReceiver extends BroadcastReceiver {

	protected List<NetworkStateReceiverListener> listeners;
	protected Boolean connected=false;

	public NetworkStateReceiver() {
		listeners = new ArrayList<NetworkStateReceiverListener>();
		connected = null;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getExtras() == null)
			return;

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = manager.getActiveNetworkInfo();

		if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
			URLConnection connection;
			try {
				//TODO URL in Final variable auslagern die f�r die gesamte App gilt nicht so statisch...
				connection = new URL(
						"https://apigw.tk-bodensee.net/")
						.openConnection();

				connection.connect();
				connected = true;
			} catch (MalformedURLException e1) {
				connected = false;
				e1.printStackTrace();
			} catch (IOException e1) {
				connected = false;
				e1.printStackTrace();
			}

		} else if (intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
			connected = false;
		}

		notifyStateToAll();
	}

	private void notifyStateToAll() {
		for (NetworkStateReceiverListener listener : listeners)
			notifyState(listener);
	}

	private void notifyState(NetworkStateReceiverListener listener) {
		if (connected == null || listener == null)
			return;

		if (connected == true)
			listener.networkAvailable();
		else
			listener.networkUnavailable();
	}

	public void addListener(NetworkStateReceiverListener l) {
		listeners.add(l);
		notifyState(l);
	}

	public void removeListener(NetworkStateReceiverListener l) {
		listeners.remove(l);
	}

	public interface NetworkStateReceiverListener {
		public void networkAvailable();

		public void networkUnavailable();
	}
}