package swk.application.helper;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import swk.application.database.Assignment;
import swk.application.database.Auftrag;
import swk.application.database.Kunde;
import swk.application.database.Sip;
import swk.application.database.Standort;
import swk.application.database.Vertrag;

/**
 * Helperklasse verantwortlich f�r die gesamte GSON-Funktionalit�t
 * @author endres
 *
 */
public class GSONHelper {

	final String baseURL = "https://apigw.tk-bodensee.net/dev/v1.0/";
	final String customerURL = baseURL + "kunde/";
	final String adressURL = baseURL + "adresse/";
	final String vertragURL = baseURL + "vertrag/";
	final String standortURL = baseURL + "standort/";
	final String allCustomerURL = "https://apigw.tk-bodensee.net/dev/v1.0/kunde/kundennummer/";
	final String mappingFTUKunde =  "https://apigw.tk-bodensee.net/dev/v1.0/kunde/ftu/";



	String customerNR;
	Kunde kunde = null;
	Vertrag vertrag = null;



	public GSONHelper(String customerNR, InputStream contextAPP) {
		this.customerNR = customerNR;

	}

	public GSONHelper(InputStream contextAPP) {

	}



	public Kunde getKunde() throws UnknownHostException, MalformedURLException,
			IOException {

		HttpsURLConnection request = null;
		URL url = new URL(customerURL + customerNR);
		Log.i("GSON", url.toString());
		request = urlToConnect(url);


		JsonParser jsonParser = new JsonParser(); // gson
		InputStream stream = (InputStream) request.getContent();
		InputStreamReader streamReader = new InputStreamReader(stream);
		JsonObject kundeObj = jsonParser.parse(streamReader)
				.getAsJsonObject();
		stream.close();
		streamReader.close();

		if (kundeObj != null) {


			JsonObject adressObj = kundeObj.getAsJsonObject("_links");
			url = new URL(adressObj.get("adresse").getAsString());
			request = null;

			request = urlToConnect(url);

			InputStream stream1 = (InputStream) request.getContent();
			InputStreamReader streamReader1 = new InputStreamReader(stream1);
			adressObj = jsonParser.parse(streamReader1)
					.getAsJsonObject();
			stream1.close();
			streamReader1.close();


			if (adressObj != null) {
				Log.d("GSON-Abfrage",
						getAsStringOrNull(kundeObj.get("kunde.kundennummer"))
								+ ": "
								+ getAsStringOrNull(kundeObj.get("kunde.name"))
								+ ", "
								+ new Timestamp(new java.util.Date().getTime())
								.toString()
								+ ", "
								+ getAsStringOrNull(adressObj
								.get("name.anrede"))
								+ ", "
								+ getAsStringOrNull(adressObj.get("name.titel"))
								+ ", "
								+ getAsStringOrNull(kundeObj.get("kunde.art"))
								+ ", "
								+ getAsStringOrNull(adressObj
								.get("kontakt.telefon"))
								+ ", "
								+ getAsStringOrNull(adressObj
								.get("kontakt.telefax"))
								+ ", "
								+ getAsStringOrNull(adressObj
								.get("kontakt.mobil"))
								+ ", "
								+ getAsStringOrNull(adressObj
								.get("portal.mail")));
				kunde = new Kunde(
						getAsStringOrNull(kundeObj.get("kunde.kundennummer")),
						new Timestamp(new java.util.Date().getTime())
								.toString(),
						getAsStringOrNull(kundeObj.get("kunde.name")),
						getAsStringOrNull(adressObj.get("name.anrede")),
						getAsStringOrNull(adressObj.get("name.titel")),
						getAsStringOrNull(kundeObj.get("kunde.art")),
						getAsStringOrNull(adressObj.get("kontakt.telefon")),
						getAsStringOrNull(adressObj.get("kontakt.telefax")),
						getAsStringOrNull(adressObj.get("kontakt.mobil")),
						getAsStringOrNull(adressObj.get("portal.mail")));
				Log.i("Create",
						"Kunde:"
								+ getAsStringOrNull(kundeObj
								.get("kunde.kundennummer"))
								+ " erfolgreich erzeugt!");

			}
		}
		return kunde;
	}

	public Vertrag[] getAllVertrag() throws UnknownHostException,
			MalformedURLException, IOException {
		Vertrag[] vertragArray = null;
		URL url = new URL(customerURL + customerNR + "/vertrag/");
		Log.d("gson", url.toString());

		HttpsURLConnection request = urlToConnect(url);
		request.connect();
		JsonParser jsonParser = new JsonParser(); // gson
		InputStream stream = (InputStream) request.getContent();
		InputStreamReader streamReader = new InputStreamReader(stream);
		JsonArray jsonArray = jsonParser.parse(
				streamReader)
				.getAsJsonArray();
		stream.close();
		streamReader.close();
		if (jsonArray != null) {
			vertragArray = new Vertrag[jsonArray.size()];

			for (int i = 0; i < jsonArray.size(); i++) {
				vertragArray[i] = new Vertrag(jsonArray.get(i)
						.getAsJsonObject().get("vertrag.vertragnummer")
						.toString(), customerNR);
				Log.i("GSON", "Vertrag mit Nummer: " + jsonArray.get(i)
						.getAsJsonObject().get("vertrag.vertragnummer")
						.toString());
			}
			return vertragArray;
		} else {
			Log.w("GSON-Error", "error in creating Vertrag Object");
			return vertragArray;
		}
	}

	public ArrayList<Auftrag> getAllAuftrag(String vertragid)
			throws UnknownHostException, MalformedURLException, IOException {
		ArrayList<Auftrag> auftragList = new ArrayList<Auftrag>();
		JsonObject jsonObject = null;
		JsonObject jsonObjectL = null;
		String standortid = null;
		URL url = new URL(vertragURL + vertragid + "/produkt/");
		Log.d("gson", url.toString());

		HttpsURLConnection request = urlToConnect(url);
		request.connect();
		JsonParser jsonParser = new JsonParser(); // gson
		JsonArray jsonArray = jsonParser.parse(
				new InputStreamReader((InputStream) request.getContent()))
				.getAsJsonArray();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.get(i).getAsJsonObject();
				jsonObjectL = jsonObject.get("_links").getAsJsonObject();
				if (!jsonObjectL.get("standort").isJsonNull()) {
					standortid = jsonObjectL.get("standort").toString();
					standortid = standortid.substring(standortid.length() - 6);
					standortid = standortid.substring(0,
							standortid.length() - 1);

					JsonObject linkObj = jsonObject.getAsJsonObject("_links");
					String sip="";
					if(linkObj.get("sip/")!=null) {
						if (linkObj.get("sip/").isJsonNull()!=true) {
							sip = getAsStringOrNull(linkObj.get("sip/"));
							sip = sip.substring(sip.indexOf("produkt/") + 8, sip.indexOf("/sip/"));
						}

					}
					String assignment ="";
					if(linkObj.get("cpe/")!=null) {
						if (linkObj.get("cpe/").isJsonNull()!=true) {
							assignment = getAsStringOrNull(linkObj.get("cpe/"));
							assignment = assignment.substring(assignment.indexOf("cid/") + 4, assignment.indexOf("/assignment/"));
						}
					}

					auftragList.add(new Auftrag(vertragid,
							getAsStringOrNull(jsonObject
									.get("produkt.auftragsnummer")),
							getAsStringOrNull(jsonObject
									.get("produkt.buchungstext")), standortid,

							getAsStringOrNull(jsonObject.get("produkt.wfpath_id")),
							sip, assignment));

					Log.d("GSON-Auftrag", "Auftrag: " + vertragid +" , "+
							getAsStringOrNull(jsonObject
									.get("produkt.auftragsnummer")) +" , "+
							getAsStringOrNull(jsonObject
									.get("produkt.buchungstext")) + standortid +" , "+
							getAsStringOrNull(jsonObject
									.get("produkt.wfpath_id"))+" , "+
							sip+", "+ assignment);

				} else {
					Log.i("Null-Object", "Auftrag: " + vertragid + " nullPointer");
				}
			}
			return auftragList;
		} else {

			Log.w("GSON-Error", "error in creating Customer Object");
			return auftragList;
		}
	}

	public Standort getStandort(String standortid) throws UnknownHostException,
			MalformedURLException, IOException {
		Standort standort = null;
		HttpsURLConnection request = null;
		URL url = new URL(standortURL + standortid);
		Log.i("GSON", url.toString());
		request = urlToConnect(url);
		request.connect();

		JsonParser jsonParser = new JsonParser(); // gson
		JsonObject jsonObj = jsonParser.parse(
				new InputStreamReader((InputStream) request.getContent()))
				.getAsJsonObject();
		if (jsonObj != null) {
			standort = new Standort(standortid,
					getAsStringOrNull(jsonObj.get("wohnung.lage")),
					getAsStringOrNull(jsonObj.get("standort.strasse")),
					getAsStringOrNull(jsonObj.get("wohnung.bemerkung")),
					getAsStringOrNull(jsonObj.get("gebaeude.bemerkung")),
					getAsStringOrNull(jsonObj.get("gebaeude.tk_station")),
					getAsStringOrNull(jsonObj.get("gebaeude.tk_station_plan")),
					getAsStringOrNull(jsonObj.get("standort.plz")),
					getAsStringOrNull(jsonObj.get("standort.ort")),
					getAsStringOrNull(jsonObj.get("wohnung.stockwerk")),
					getAsStringOrNull(jsonObj.get("standort.hhnr")),
					getAsStringOrNull(jsonObj.get("wohnung.raum")),
					getAsStringOrNull(jsonObj.get("standort.hhnrzusatz")));

			Log.i("GSON",
					"Beziehe Standort: " + " , "
							+ standortid
							+ " , "
							+ getAsStringOrNull(jsonObj.get("wohnung.lage"))
							+ " , "
							+ getAsStringOrNull(jsonObj.get("standort.strasse"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("wohnung.bemerkung"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("gebaeude.bemerkung"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("gebaeude.tk_station"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("gebaeude.tk_station_plan"))
							+ " , "
							+ getAsStringOrNull(jsonObj.get("standort.plz"))
							+ " , "
							+ getAsStringOrNull(jsonObj.get("standort.ort"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("wohnung.stockwerk"))
							+ " , "
							+ getAsStringOrNull(jsonObj.get("standort.hhnr"))
							+ " , "
							+ getAsStringOrNull(jsonObj.get("wohnung.raum"))
							+ " , "
							+ getAsStringOrNull(jsonObj
							.get("standort.hhnrzusatz")));

			return standort;
		} else {
			Log.w("GSON-Error", "error in creating Customer Object");
			return standort;
		}
	}

	private String getAsStringOrNull(JsonElement element) {

		if (element.isJsonNull() || element == null) {
			return " ";
		} else {
			return element.getAsString();
		}
	}

	public List<String> getAllKundenNummer() throws IOException,
			UnknownHostException, MalformedURLException {
		ArrayList<String> customerNrList = new ArrayList<String>();
		// Vorab �berpr�fung ob Verbdinung besteht

		// Verbindungsaufbau zur entsprechenden URL
		HttpsURLConnection urlConnection = null;
		URL url = new URL(allCustomerURL);
		urlConnection = urlToConnect(url);
		// JSON-Abhandlung
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		JsonParser jsonParser = new JsonParser();
		JsonElement rootElement = jsonParser.parse(inputStreamReader);
		JsonArray customerNrArray = rootElement.getAsJsonArray();
		for (int i = 0; i < customerNrArray.size(); i++) {
			customerNrList.add(customerNrArray.get(i).toString());
		}
		// Erfolgreiche List gebaut
		Log.i("GSON", "Erfolgreich alle Kundennummern abgerufen");
		return customerNrList;

	}


	public Sip getSip(String sipid) throws UnknownHostException,
			MalformedURLException, IOException {

		Sip sip= null;
		HttpsURLConnection request = null;
		URL url = new URL(baseURL+"produkt/" + sipid + "/sip/");
		Log.i("GSON", url.toString());
		request = urlToConnect(url);
		request.connect();

		JsonParser jsonParser = new JsonParser(); // gson
		JsonArray jsonArray = jsonParser.parse(
				new InputStreamReader((InputStream) request.getContent()))
				.getAsJsonArray();
		if (!jsonArray.isJsonNull() ) {
			JsonObject jsonObj=jsonArray.get(0).getAsJsonObject();
			sip = new Sip(sipid,
					getAsStringOrNull(jsonObj.get("sip_user")),
					getAsStringOrNull(jsonObj.get("block_von")),
					getAsStringOrNull(jsonObj.get("block_bis")),
					getAsStringOrNull(jsonObj.get("sip_pass")),
					getAsStringOrNull(jsonObj.get("rufnummer")),
					getAsStringOrNull(jsonObj.get("sip_realm")));

			Log.i("GSON",
					"Beziehe Sip: " + " , "
							+ sip
							+ " , "+
					getAsStringOrNull(jsonObj.get("sip_user"))
							+ " , "+
					getAsStringOrNull(jsonObj.get("block_von"))
							+ " , "+
					getAsStringOrNull(jsonObj.get("block_bis"))
							+ " , "+
					getAsStringOrNull(jsonObj.get("sip_pass"))
							+ " , "+
					getAsStringOrNull(jsonObj.get("rufnummer"))
							+ " , "+
					getAsStringOrNull(jsonObj.get("sip_realm")));

			return sip;
		} else {
			Log.w("GSON-Error", "error in creating Customer Object");
			return sip;
		}
	}


	private HttpsURLConnection urlToConnect(URL url) throws IOException {
		SharedPreferences sp=PublicContext.getInstance().context().getSharedPreferences("SWK_Application_Login", 0);

		HttpsURLConnection con = null;

		Log.w("urlToConnect", url.toString());



			try {
				con = (HttpsURLConnection) url.openConnection();
				con.setRequestProperty("Authorization", "Basic " + sp.getString(PublicContext.getInstance().getUsername(),null));
				con.setSSLSocketFactory(PublicContext.getInstance().factory());
				con.connect();
			} catch (IOException e) {
				e.printStackTrace();
			}

		return con;
	}



	public List<String[]> getMappingFTUKunde() throws IOException,
			UnknownHostException, MalformedURLException {
		ArrayList<String[]> ftuKunde = new ArrayList<>();
		// Vorab �berpr�fung ob Verbdinung besteht

		// Verbindungsaufbau zur entsprechenden URL
		HttpsURLConnection urlConnection = null;
		URL url = new URL(mappingFTUKunde);
		urlConnection = urlToConnect(url);
		// JSON-Abhandlung
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		JsonParser jsonParser = new JsonParser();
		JsonElement rootElement = jsonParser.parse(inputStreamReader);
		JsonArray result = rootElement.getAsJsonArray();


		for (int i = 0; i < result.size(); i++) {

			JsonObject jsonObj = result.get(i).getAsJsonObject();
			String[] value={jsonObj.get("ftu").getAsString(),jsonObj.get("kundennummer").getAsString()};
			ftuKunde.add(value);

		}

		Log.i("FTU-Scan", "Erfolgreich alle FTUS abgerufen");
		return ftuKunde;

	}



	public Assignment getAssignment(String assignmentid) throws UnknownHostException,
			MalformedURLException, IOException {

		Assignment assignment= null;
		HttpsURLConnection request = null;

		URL url = new URL(baseURL+"cid/" + assignmentid+ "/assignment/");;
		Log.w("Assignment in Gson Helper", "url= "+url);
		//Log.w("Assignment in Gson Helper", "baseurl= "+baseURL);

		//Log.i("GSON", url.toString());
		request = urlToConnect(url);
		request.connect();

		JsonParser jsonParser = new JsonParser(); // gson
		JsonArray jsonArray = jsonParser.parse(
				new InputStreamReader((InputStream) request.getContent()))
				.getAsJsonArray();
		if (!jsonArray.isJsonNull() ) {
			//Log.w("Assignment-JSON","ist ungleich Null");
			//Log.w("Assignment ARRAY",jsonArray.toString()+ "                   -                     "+ jsonArray.size());
			if (jsonArray.size()!=0) {
				JsonObject jsonObj = jsonArray.get(0).getAsJsonObject();
				//Log.w("Assignment OBJECT", jsonObj.toString());
				assignment = new Assignment(assignmentid,
						getAsStringOrNull(jsonObj.get("cwmp")),
						getAsStringOrNull(jsonObj.get("gid")),
						getAsStringOrNull(jsonObj.get("cid")),
						getAsStringOrNull(jsonObj.get("wid")),
						getAsStringOrNull(jsonObj.get("valid_from")),
						getAsStringOrNull(jsonObj.get("valid_until")));

				Log.i("GSON",
						"Beziehe Assignment: " + " , "
								+ assignment
								+ " , " +
								getAsStringOrNull(jsonObj.get("cwmp"))
								+ " , " +
								getAsStringOrNull(jsonObj.get("gid"))
								+ " , " +
								getAsStringOrNull(jsonObj.get("cid"))
								+ " , " +
								getAsStringOrNull(jsonObj.get("wid"))
								+ " , " +
								getAsStringOrNull(jsonObj.get("valid_from"))
								+ " , " +
								getAsStringOrNull(jsonObj.get("valid_until")));
			}
			return assignment;
		} else {
			Log.w("GSON-Error", "error in creating Customer Object");
			return assignment;
		}
	}




}


