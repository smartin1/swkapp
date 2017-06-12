package swk.application.database;

/**
 * Datenbanktabelle Vertrag
 * @author endres
 *
 */
public class Vertrag {

	int id;
	String kundennummer;
	String vertragid;

	public Vertrag(String vertragid, String kundennummer) {
		this.kundennummer = kundennummer;
		this.vertragid = vertragid;
		this.id = (int) Math.random() * 1000000;
	}

	public String getKundennummer() {
		return kundennummer;
	}

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	public String getVertragid() {
		return vertragid;
	}

	public void setVertragid(String vertragid) {
		this.vertragid = vertragid;
	}

}
