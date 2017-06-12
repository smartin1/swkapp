package swk.application.database;
/**
 * Datenbanktabelle Kunde
 * @author endres
 *
 */
public class Kunde {

	int id;
	String kundennummer;
	String timestamp;
	String name;
	String anrede;
	String titel;
	String art;
	String telefon;
	String telefax;
	String mobil;
	String mail;

	public Kunde(String kundennummer, String timestamp, String name, String anrede, String titel, String art,
			String telefon, String telefax, String mobil, String mail) {
		this.kundennummer = kundennummer;
		this.timestamp = timestamp;
		this.name = name;
		this.anrede = anrede;
		this.titel = titel;
		this.telefon = telefon;
		this.telefax = telefax;
		this.mobil = mobil;
		this.mail = mail;
		this.art=art;
		
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public String getKundennummer() {
		return kundennummer;
	}

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnrede() {
		return anrede;
	}

	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getTelefax() {
		return telefax;
	}

	public void setTelefax(String telefax) {
		this.telefax = telefax;
	}

	public String getMobil() {
		return mobil;
	}

	public void setMobil(String mobil) {
		this.mobil = mobil;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
