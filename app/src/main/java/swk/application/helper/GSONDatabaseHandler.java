package swk.application.helper;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import swk.application.activities.R;
import swk.application.database.Auftrag;
import swk.application.database.DatabaseHandler;
import swk.application.database.Kunde;
import swk.application.database.Vertrag;

/**
 * Diese Klasse steuert das Zusammenspiel zwischen der GSONHelper Klasse und der DatabaseHandlerklasse
 * Ziel --> Durch einen Methodenaufruf alle vorhandenen Daten in die Datenbankstruktur inserten.
 * Hier soll zudem die Updatefunktion bei schon vorhandenem Datensatz durchgef�hrt werden. 
 * @author endres
 *
 */
public class GSONDatabaseHandler {
	private DatabaseHandler datenbank;
	private GSONHelper gson;
	Context context;
	
	public GSONDatabaseHandler(Context context){
		this.datenbank=DatabaseHandler.getInstance(context);
		this.context=context;
	}


	
	public boolean getOrUpdateCustomer(String kundennummer){
		gson=new GSONHelper(kundennummer,context.getResources().openRawResource(R.raw.test1));
		try {

			Kunde k=gson.getKunde();
			Log.w("Kunde Databasehandler",k.getKundennummer());
			datenbank.addKunde(k);
			Vertrag[] vertragArray= gson.getAllVertrag();
			ArrayList<Auftrag> auftragList;
			for(int i=0 ; i<vertragArray.length;i++){
				datenbank.addVertrag(vertragArray[i]);
				auftragList=gson.getAllAuftrag(vertragArray[i].getVertragid());
				for (int b= 0 ; b<auftragList.size();b++){
					datenbank.addAuftrag(auftragList.get(b));
					Log.w("Standortid  ", auftragList.get(b).getStandortid());
					datenbank.addStandort(gson.getStandort(auftragList.get(b).getStandortid()));
					if(auftragList.get(b).getSip()!="") {
						datenbank.addSip(gson.getSip(auftragList.get(b).getSip()));
					}



					if(auftragList.get(b).getAssignment()!="" && gson.getAssignment(auftragList.get(b).getAssignment())!=null) {
						datenbank.addAssignment(gson.getAssignment(auftragList.get(b).getAssignment()));
						Log.w("In GSONDATABASEHANDLER", "assignement = "+auftragList.get(b).getAssignment());

					}
					}

			}
			
			
        } catch (UnknownHostException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (MalformedURLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
		
		return true;
	}
}
