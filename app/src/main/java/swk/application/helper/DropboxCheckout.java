//package swk.application.helper;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.channels.Channels;
//import java.nio.channels.ReadableByteChannel;
//import java.util.Scanner;
//
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.util.Log;
//
//public class DropboxCheckout extends AsyncTask<String, Void, String> {
//
//	private Exception exception;
//
//	protected String doInBackground(String... urls) {
//		String value = null;
//
//		// URL url = new URL();
//		// InputStream input = new URL(
//		// "https://www.dropbox.com/s/ajkl35cd3jlm57g/Users%20Application.txt?dl=0"
//		// ).openStream();
//		// URL oracle = new
//		// URL("");
//		 //The file that you want to download
//		String page=null;
//		String url="https://www.dropbox.com/s/ajkl35cd3jlm57g/Users%20Application.txt?dl=0";
//		String filename=Environment.getExternalStorageDirectory()+"/Users Application.txt";
//
//		try{
//		    URL download=new URL(url);
//		    ReadableByteChannel rbc=Channels.newChannel(download.openStream());
//		    FileOutputStream fileOut = new FileOutputStream(filename);
//		    fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
//		    fileOut.flush();
//		    Log.w("DB",fileOut.);
//		    fileOut.close();
//		    rbc.close();
//		    
//		}catch(Exception e){ e.printStackTrace(); }
//		return page;
//	}
//
//	protected void onPostExecute(String string) {
//
//	}
//}