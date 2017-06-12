package swk.application.helper;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import swk.application.activities.R;

/**
 * Created by endres on 22.02.2017.
 */

public class FactoryHelper {

    private static Context context;
    private static FactoryHelper instance = null;
    protected FactoryHelper() {
        // Exists only to defeat instantiation.
    }
    public static FactoryHelper getInstance(Context appContext) {
        if(instance == null) {
            instance = new FactoryHelper();
           context =appContext;
        }
        return instance;
    }


     SSLSocketFactory getFactory() throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyManagerFactory keyManagerFactory;

        keyManagerFactory = KeyManagerFactory.getInstance("X509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream stream=context.getResources().openRawResource(R.raw.test1);
        keyStore.load(stream, "Bj0Pg4rw".toCharArray());
        stream.close();

        keyManagerFactory.init(keyStore, "Bj0Pg4rw".toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        return context.getSocketFactory();

    }
}

