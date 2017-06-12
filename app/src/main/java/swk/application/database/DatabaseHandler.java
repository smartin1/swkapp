package swk.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLCipher-Datenbank-Handler Fuehrt alle notwendigen Datenbankoperationen
 * durch. Aus Performancegruenden als Singleton implementiert
 * 
 * @author endres
 *
 */

public class DatabaseHandler extends SQLiteOpenHelper {
	// Allgemeine statische Variablen
	private static final String DATABASE_NAME = "SWK_Application_Database.db";
	private final String PWD = "84e3403688362d3ac5b334f02cbe1a9ef43efa6c";
	private static final int DATABASE_VERSION = 1;

	// SQL Create-Befehle
	private final String createKundeTable = "CREATE TABLE kunde (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, kundennummer TEXT NOT NULL UNIQUE, timestamp TEXT, name TEXT, anrede TEXT, titel TEXT, art TEXT, telefon TEXT, telefax TEXT, mobil TEXT, mail TEXT)";
	private final String createVertragTable = "CREATE TABLE vertrag (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, vertragid TEXT NOT NULL UNIQUE, kundennummer TEXT NOT NULL)";
	private final String createAuftragTable = "CREATE TABLE auftrag (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, vertragid TEXT NOT NULL , auftragsnummer TEXT NOT NULL UNIQUE, buchungstext TEXT, standortid TEXT NOT NULL, wfpath TEXT NOT NULL, sip TEXT, assignment TEXT)";
	private final String createStandortTable = "CREATE TABLE standort (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, standortid TEXT NOT NULL UNIQUE, lage TEXT, strasse TEXT, bemerkungW TEXT, bemerkungG TEXT,tk TEXT, tkPlan TEXT, plz TEXT, ort TEXT,stockwerk TEXT,hhnr TEXT, raum TEXT, hhnrzusatz TEXT)";
	private final String dataMinimisation = "DELETE FROM kunde WHERE timestamp < NOW() - INTERVAL 7 DAY";
	private final String createSipTable = "CREATE TABLE sip (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, sipid TEXT NOT NULL UNIQUE, sipuser TEXT, von TEXT, bis TEXT, pass TEXT, rufnummer TEXT, realm TEXT)";
	private final String createAssignmentTable = "CREATE TABLE assignment (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, assignmentid TEXT NOT NULL UNIQUE, cwmp TEXT, gid TEXT, cid TEXT, wid TEXT, valid_from DATETIME, valid_until DATETIME)";


	// (String vertragid, String auftragsnummer,
	// String buchungstext, String standortid, String wfpath)
	private static DatabaseHandler sInstance = null;
	private SQLiteDatabase db = null;

	/*
	 * �ffentliche getInstance Methode
	 */
	public static DatabaseHandler getInstance(Context context) {
		// Falls der Singleton noch nicht erzeugt wurde
		if (sInstance == null) {
			sInstance = new DatabaseHandler(context, DATABASE_NAME,
					DATABASE_VERSION);

		}
		return sInstance;
	}

	/*
	 * privater Konstruktor
	 */
	private DatabaseHandler(final Context context, final String name,
			final int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		SQLiteDatabase.loadLibs(context);
		db = DatabaseHandler.this.getWritableDatabase(PWD);

		//db.execSQL(dataMinimisation);
	}

	/*
	 * Anpassung der von SQLCipher close()-Methode
	 */
	@Override
	public synchronized void close() {
		if (sInstance != null)
			db.close();
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL("drop SWK_Application_Database.db");
		db.execSQL(createKundeTable);
		db.execSQL(createVertragTable);
		db.execSQL(createAuftragTable);
		db.execSQL(createStandortTable);
		db.execSQL(createSipTable);
		db.execSQL(createAssignmentTable);
	}

	// Datenbankupgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("drop database SWK_Application_Database.db");
		db.execSQL("DROP TABLE IF EXISTS kunde");
		db.execSQL("DROP TABLE IF EXISTS vertrag");
		db.execSQL("DROP TABLE IF EXISTS auftrag");
		db.execSQL("DROP TABLE IF EXISTS standort");
		db.execSQL("DROP TABLE IF EXISTS sip");
		db.execSQL("DROP TABLE IF EXISTS assignment");
		// Erzeugt die Tabellen erneut
		onCreate(db);
	}

	public List<String> getAllCustomerNr() {

		ArrayList<String> namesList = null;
		Cursor cursor = null;

		try {
			// String query = "Delete from customer";
			String query = "select kundennummer from kunde";
			cursor = db.rawQuery(query, null);
			
			if (cursor != null && cursor.moveToFirst()) {
				namesList = new ArrayList<String>();
				do {
					namesList.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			namesList = null;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}

		}
		return namesList;
	}

	public boolean addKunde(Kunde kunde) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		ContentValues values = new ContentValues();
		values.put("kundennummer", kunde.getKundennummer());
		values.put("timestamp",
				new Timestamp(new java.util.Date().getTime()).toString());
		values.put("name", kunde.getName());
		values.put("anrede", kunde.getAnrede());
		values.put("titel", kunde.getTitel());
		values.put("art", kunde.getArt());
		values.put("telefon", kunde.getTelefon());
		values.put("telefax", kunde.getTelefax());
		values.put("mobil", kunde.getMobil());
		values.put("mail", kunde.getMail());

		// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
		try {
			if (db.insertWithOnConflict("kunde", null, values,
					SQLiteDatabase.CONFLICT_REPLACE) != -1) {
				Log.i("Datenbank", "kunde: " + kunde.getKundennummer()
						+ " hinzugef�gt");
				return true;
			} else {
				Log.e("Datenbank", "UNIQE Constrainterror");
				return false;
			}
		} catch (Exception e) {
			Log.e("Datenbank",e.getMessage());
			Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
			return false;
		}
	}

	public Kunde getKunde(String kundennummer) {

		// Notwendigen Parameter
		Kunde kunde = null;
		String selectQuery = "SELECT * FROM kunde WHERE kundennummer="
				+ kundennummer;
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Cursor zur auf selectQuery basierend
		Cursor cursor = db.rawQuery(selectQuery, null);
		// 3. Sammle alle hinterlegten Werte und schreibe sie in customer
		if (cursor.moveToFirst()) {
			do {

				kunde = new Kunde(cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10));

				Log.d("Datenbank",
						"Beziehe Daten: Kunde: " + cursor.getString(0) + ","
								+ cursor.getString(1) + ","
								+ cursor.getString(2) + ","
								+ cursor.getString(3) + ","
								+ cursor.getString(4) + ","
								+ cursor.getString(5) + ","
								+ cursor.getString(6) + ","
								+ cursor.getString(7) + ","
								+ cursor.getString(8) + ","
								+ cursor.getString(9) + ","
								+ cursor.getString(10));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return kunde;
	}

	public boolean addVertrag(Vertrag vertrag) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		ContentValues values = new ContentValues();
		values.put("vertragid", vertrag.getVertragid());
		values.put("kundennummer", vertrag.getKundennummer());

		// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
		try {
			if (db.insertWithOnConflict("vertrag", null, values,
					SQLiteDatabase.CONFLICT_REPLACE) != -1) {
				Log.i("Datenbank", "vertrag: " + vertrag.getVertragid()
						+ " hinzugef�gt");
				Log.i("Datenbank", "vertrag: " + vertrag.getVertragid()
						+ "    ;      " + vertrag.getKundennummer()
						+ " hinzugef�gt");
				return true;
			} else {
				Log.e("Datenbank", "UNIQE Constrainterror");
				return false;
			}
		} catch (Exception e) {
			Log.e("Datenbank",e.getMessage());
			Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
			return false;
		}
	}

	public ArrayList<Vertrag> getAllVertrag(String standortid,
			String kundennummer) {

		// Notwendigen Parameter
		ArrayList<Vertrag> vertragList = new ArrayList<Vertrag>();
		String selectQuery = "SELECT DISTINCT v.* FROM kunde k "
				+ "INNER JOIN vertrag v ON (v.kundennummer = k.kundennummer) "
				+ "INNER JOIN auftrag a ON (v.vertragid = a.vertragid) "
				+ "LEFT JOIN standort s ON (s.standortid = a.standortid) "
				+ "WHERE k.kundennummer=" + kundennummer + " "
				+ "AND s.standortid=" + standortid;
		
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Cursor zur auf selectQuery basierend
		Cursor cursor = db.rawQuery(selectQuery, null);
		Log.d("DATENBANK: ",
				"Numberpicker ColumnCount: " + cursor.getColumnCount());
		// 3. Sammle alle hinterlegten Werte und schreibe sie in customer
		if (cursor.moveToFirst()) {
			do {

				vertragList.add(new Vertrag(cursor.getString(1), cursor
						.getString(2)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		Log.d("Datenbank: ", "ColumnArrayListCount: " + vertragList.size());
		return vertragList;
	}

	// public ArrayList<Vertrag> getVertrag(String kundennummer) {
	//
	// // Notwendigen Parameter
	// ArrayList<Vertrag> vertragList = new ArrayList<Vertrag>();
	// String selectQuery = "SELECT * FROM vertrag where kundennummer= "
	// + kundennummer;
	//
	// // 1. Verbindung zur SQLCipher-Datenbank
	// // SQLiteDatabase db = getReadableDatabase(PWD);
	// // bzw. db=getReadableDatabase(PWD);
	// // --> F�llt durch die Implementierung als Singleton weg!
	// // Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
	// // erfolgt.
	//
	// // 2. Cursor zur auf selectQuery basierend
	// Cursor cursor = db.rawQuery(selectQuery, null);
	// // 3. Sammle alle hinterlegten Werte und schreibe sie in customer
	// if (cursor.moveToFirst()) {
	// do {
	//
	// vertragList.add(new Vertrag(cursor.getString(1), cursor
	// .getString(2)));
	// Log.w("Datenbank",
	// "Beziehe Daten: Vertrag: " + cursor.getString(1) + "  "
	// + cursor.getString(2));
	// Log.e("Cursor", "Cursor: " + cursor.getColumnCount());
	// } while (cursor.moveToNext());
	// }
	// cursor.close();
	// return vertragList;
	// }

	public ArrayList<Standort> getAllStandort(String kundennummer) {

		// Notwendigen Parameter
		ArrayList<Standort> standortList = new ArrayList<Standort>();
		String selectQuery = "SELECT DISTINCT s.* FROM kunde k "
				+ "INNER JOIN vertrag v ON (v.kundennummer = k.kundennummer) "
				+ "INNER JOIN auftrag a ON (v.vertragid = a.vertragid) "
				+ "LEFT JOIN standort s ON (s.standortid = a.standortid) "
				+ "WHERE k.kundennummer =" + kundennummer;

		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Cursor zur auf selectQuery basierend
		Cursor cursor = db.rawQuery(selectQuery, null);
		// 3. Sammle alle hinterlegten Werte und schreibe sie in customer
		if (cursor.moveToFirst()) {
			do {

				standortList.add(new Standort(cursor.getString(1), cursor
						.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5), cursor
								.getString(6), cursor.getString(7), cursor
								.getString(8), cursor.getString(9), cursor
								.getString(10), cursor.getString(11), cursor
								.getString(12), cursor.getString(13)));
				Log.w("Datenbank",
						"Beziehe Daten: Standort: " + cursor.getString(1)
								+ "  " + cursor.getString(2) + " "
								+ cursor.getString(3) + " , "
								+ cursor.getString(4) + " , "
								+ cursor.getString(5) + " , "
								+ cursor.getString(6) + " , "
								+ cursor.getString(7) + " , "
								+ cursor.getString(8) + " , "
								+ cursor.getString(9) + " , "
								+ cursor.getString(10) + " , "
								+ cursor.getString(11) + " , "
								+ cursor.getString(12) + " , "
								+ cursor.getString(13));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return standortList;
	}

	public boolean addAuftrag(Auftrag auftrag) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		if (auftrag != null) {
			ContentValues values = new ContentValues();
			values.put("vertragid", auftrag.getVertragid());
			values.put("auftragsnummer", auftrag.getAuftragsnummer());
			values.put("buchungstext", auftrag.getBuchungstext());
			values.put("standortid", auftrag.getStandortid());
			values.put("wfpath", auftrag.getWfpath());
			values.put("sip",auftrag.getSip());
			values.put("assignment",auftrag.getAssignment());

			// (String vertragid, String auftragsnummer,
			// String buchungstext, String standortid, String wfpath)

			// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
			try {
				if (db.insertWithOnConflict("auftrag", null, values,
						SQLiteDatabase.CONFLICT_REPLACE) != -1) {
					Log.i("Datenbank",
							"Auftrag: " + auftrag.getAuftragsnummer()
									+ " hinzugef�gt        Standortid: "
									+ auftrag.getStandortid()+"         vertagid: "+auftrag.getVertragid());
					return true;
				} else {
					Log.e("Datenbank", "UNIQE Constrainterror");
					return false;
				}
			} catch (Exception e) {
				Log.e("Datenbank",e.getMessage());
				Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
				return false;
			}
		}
		return false;
	}

	public ArrayList<Auftrag> getAuftrag(String vertragid) {

		// Notwendigen Parameter
		ArrayList<Auftrag> auftragList = new ArrayList<Auftrag>();
		String selectQuery = "SELECT * FROM auftrag where vertragid="
				+ vertragid;

		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Cursor zur auf selectQuery basierend
		Cursor cursor = db.rawQuery(selectQuery, null);
		Log.e("Auftrag", "cursor: "+cursor.getColumnCount());
		// 3. Sammle alle hinterlegten Werte und schreibe sie in customer
		
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Log.w("cursorINT"," "+cursor.getCount());
				auftragList.add(new Auftrag(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), cursor.getString(6), cursor.getString(7)));
				Log.w("Datenbank",
						"Beziehe Daten: Auftrag: " + cursor.getString(1) + ",  "
								+ cursor.getString(2) + ", "
								+ cursor.getString(3) + ", "
								+ cursor.getString(4) + ", "
								+ cursor.getString(5) + ", "
								+ cursor.getString(6) + ", "
								+ cursor.getString(7));
			} while (cursor.moveToNext());
		}
		

		Log.e("Auftrag", ""+auftragList.size());
		cursor.close();
		return auftragList;

	}

	public Standort getStandort(String kundennummer) {
		Standort standort = null;
		String selectQuery = " SELECT DISTINCT s.* FROM vertrag v JOIN auftrag a USING (vertragid) JOIN standort s USING (standortid) WHERE kundennummer ="
				+ kundennummer;

		// SELECT DISTINCT s.* FROM vertrag v JOIN auftrag a USING (vertragid)
		// JOIN standort s USING (standortid) WHERE vertragid = bla;

		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		Cursor cursor = db.rawQuery(selectQuery, null);
		// 3. Sammle alle hinterlegten Werte und schreibe sie in customer
		if (cursor.moveToFirst()) {
			do {
				standort = new Standort(cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						cursor.getString(8), cursor.getString(9),
						cursor.getString(10), cursor.getString(11),
						cursor.getString(12), cursor.getString(13));
				Log.w("Datenbank",
						"Beziehe Daten: Standort: " + cursor.getString(1)
								+ "  " + cursor.getString(2) + " "
								+ cursor.getString(3) + " , "
								+ cursor.getString(4) + " , "
								+ cursor.getString(5) + " , "
								+ cursor.getString(6) + " , "
								+ cursor.getString(7) + " , "
								+ cursor.getString(8) + " , "
								+ cursor.getString(9) + " , "
								+ cursor.getString(10) + " , "
								+ cursor.getString(11) + " , "
								+ cursor.getString(12) + " , "
								+ cursor.getString(13));
				return standort;
			} while (cursor.moveToNext());
		}
		cursor.close();
		return standort;
		// (String standortid, String lage, String strasse, String bemerkungW,
		// String bemerkungG, String tk, String tkPlan, String plz, String ort,
		// String stockwerk, String hhnr, String raum, String hhnrzusatz)

	}

	public boolean addStandort(Standort standort) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.
		ContentValues values = new ContentValues();
		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		if (standort != null) {
			values.put("standortid", standort.getStandortid());
			values.put("lage", standort.getLage());
			values.put("strasse", standort.getStrasse());
			values.put("bemerkungW", standort.getBemerkungW());
			values.put("bemerkungG", standort.getBemerkungG());
			values.put("tk", standort.getTk());
			values.put("tkPlan", standort.getTkPlan());
			values.put("plz", standort.getPlz());
			values.put("ort", standort.getOrt());
			values.put("stockwerk", standort.getStockwerk());
			values.put("hhnr", standort.getHhnr());
			values.put("raum", standort.getRaum());
			values.put("hhnrzusatz", standort.getHhnrzusatz());
		}
		// (String standortid, String lage, String strasse, String bemerkungW,
		// String bemerkungG, String tk, String tkPlan, String plz, String ort,
		// String stockwerk, String hhnr, String raum, String hhnrzusatz)

		// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
		try {
			if (db.insertWithOnConflict("standort", null, values,
					SQLiteDatabase.CONFLICT_REPLACE) != -1) {
				Log.i("Datenbank", "Standort: " + standort.getStandortid()
						+ " hinzugef�gt");
				return true;
			} else {
				Log.e("Datenbank", "UNIQE Constrainterror");
				return false;
			}
		} catch (Exception e) {
			Log.e("Datenbank",e.getMessage());
			Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
			return false;
		}
	}

	public boolean addSip(Sip sip) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		if (sip != null) {
			ContentValues values = new ContentValues();
			values.put("sipid", sip.getSipId());
			values.put("sipuser", sip.getSipuser());
			values.put("von", sip.getVon());
			values.put("bis", sip.getBis());
			values.put("pass", sip.getPass());
			values.put("rufnummer", sip.getRufnummer());
			values.put("realm", sip.getRealm());

			// (String vertragid, String auftragsnummer,
			// String buchungstext, String standortid, String wfpath)

			// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
			try {
				if (db.insertWithOnConflict("sip", null, values,
						SQLiteDatabase.CONFLICT_REPLACE) != -1) {
					Log.i("Datenbank",
							"Sip: " + sip.getSipId());
					return true;
				} else {
					Log.e("Datenbank", "UNIQE Constrainterror");
					return false;
				}
			} catch (Exception e) {
				Log.e("Datenbank",e.getMessage());
				Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
				return false;
			}
		}
		return false;
	}

	public Sip getSip(String sipid) {
		if(!sipid.equals("")) {
			Sip sip = null;
			String selectQuery = " SELECT * from sip WHERE sipid="
					+ sipid;

			// 1. Verbindung zur SQLCipher-Datenbank
			// SQLiteDatabase db = getReadableDatabase(PWD);
			// bzw. db=getReadableDatabase(PWD);
			// --> F�llt durch die Implementierung als Singleton weg!
			// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
			// erfolgt.

			// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
			Cursor cursor = db.rawQuery(selectQuery, null);
			// 3. Sammle alle hinterlegten Werte und schreibe sie in sip
			if (cursor.moveToFirst()) {
				do {
					sip = new Sip(cursor.getString(1),
							cursor.getString(2), cursor.getString(3),
							cursor.getString(4), cursor.getString(5),
							cursor.getString(6), cursor.getString(7));
					Log.w("Datenbank",
							"Beziehe Daten: SIP: " + cursor.getString(1)
									+ "  " + cursor.getString(2) + " "
									+ cursor.getString(3) + " , "
									+ cursor.getString(4) + " , "
									+ cursor.getString(5) + " , "
									+ cursor.getString(6) + " , "
									+ cursor.getString(7));
					return sip;
				} while (cursor.moveToNext());
			}
			cursor.close();
			return sip;
		}else{

			return null;
		}

		//String sipid;	String sipuser;	String von;	String bis;	String art;	String pass; String rufnummer; String realm;

	}





	public boolean addAssignment(Assignment assignment) {
		// 1. Verbindung zur SQLCipher-Datenbank
		// SQLiteDatabase db = getReadableDatabase(PWD);
		// bzw. db=getReadableDatabase(PWD);
		// --> F�llt durch die Implementierung als Singleton weg!
		// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
		// erfolgt.

		// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
		if (assignment != null) {
			ContentValues values = new ContentValues();
			values.put("assignmentid", assignment.getAssignmentid());
			values.put("cwmp", assignment.getCwmp());
			values.put("gid", assignment.getGid());
			values.put("cid", assignment.getCid());
			values.put("wid", assignment.getWid());
			values.put("valid_from", assignment.getValid_from());
			values.put("valid_until", assignment.getValid_until());




			// (String vertragid, String auftragsnummer,
			// String buchungstext, String standortid, String wfpath)

			// 3. Insert vom Customer-Object in die SQLCipher-Datenbank
			try {
				if (db.insertWithOnConflict("assignment", null, values,
						SQLiteDatabase.CONFLICT_REPLACE) != -1) {
					Log.i("Datenbank",
							"Assignment: " + assignment.getAssignmentid());
					return true;
				} else {
					Log.e("Datenbank", "UNIQE Constrainterror");
					return false;
				}
			} catch (Exception e) {
				Log.e("Datenbank",e.getMessage());
				Log.e("Datenbank:", "UNIQUE CONSTRAINT FEHLER");
				return false;
			}
		}
		return false;
	}

	public Assignment getAssignment(String assignmentid) {
		if(!assignmentid.equals("")) {
			Assignment assignment = null;
			String selectQuery = " SELECT * from assignment WHERE assignmentid="
					+ assignmentid;

			// 1. Verbindung zur SQLCipher-Datenbank
			// SQLiteDatabase db = getReadableDatabase(PWD);
			// bzw. db=getReadableDatabase(PWD);
			// --> F�llt durch die Implementierung als Singleton weg!
			// Hatte extreme Performanceeinbu�en zur Folge wenn der Zugriff so
			// erfolgt.

			// 2. Erzeugung von ContentValue mit entsprechenden KEY/VALUE-Paaren
			Cursor cursor = db.rawQuery(selectQuery, null);
			// 3. Sammle alle hinterlegten Werte und schreibe sie in sip
			if (cursor.moveToFirst()) {
				do {
					assignment = new Assignment(cursor.getString(1),
							cursor.getString(2), cursor.getString(3),
							cursor.getString(4), cursor.getString(5),
							cursor.getString(6), cursor.getString(7));
					Log.w("Datenbank",
							"Beziehe Daten: Assignment: " + cursor.getString(1)
									+ "  " + cursor.getString(2) + " "
									+ cursor.getString(3) + " , "
									+ cursor.getString(4) + " , "
									+ cursor.getString(5) + " , "
									+ cursor.getString(6) + " , "
									+ cursor.getString(7));
					return assignment;
				} while (cursor.moveToNext());
			}
			cursor.close();
			return assignment;
		}else{
			return null;
		}



	}






}
