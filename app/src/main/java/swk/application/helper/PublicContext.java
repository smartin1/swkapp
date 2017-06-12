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
 * Created by endres on 27.02.2017.
 */

public class PublicContext {

    private static PublicContext ourInstance = new PublicContext();
    private Context mContext;
    private SSLSocketFactory factory;
    private String username;

    public static PublicContext getInstance() {
        return ourInstance;
    }

    private PublicContext() {
    }

    public Context context(){
        return mContext;
    }
    public SSLSocketFactory factory(){
        return factory;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public void loadProperties(Context context) {
        mContext = context;

        try {
            factory = getFactory((mContext.getResources().openRawResource(R.raw.test1)), "Bj0Pg4rw");
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private SSLSocketFactory getFactory(InputStream pKeyFile, String pKeyPassword ) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyManagerFactory keyManagerFactory;

        keyManagerFactory = KeyManagerFactory.getInstance("X509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");


        keyStore.load(pKeyFile, pKeyPassword.toCharArray());
        pKeyFile.close();

        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        return context.getSocketFactory();

    }

}