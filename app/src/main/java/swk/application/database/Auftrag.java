package swk.application.database;
/**
 * Datenbanktabelle Auftrag
 * @author endres
 *
 */
public class Auftrag {

	int id;
	String vertragid;
	String auftragsnummer;
	String buchungstext;
	String standortid;
	String wfpath;
	String sip;
	String assignment;

	public Auftrag(String vertragid, String auftragsnummer,
			String buchungstext, String standortid, String wfpath, String sip, String assignment) {
		this.vertragid = vertragid;
		this.auftragsnummer = auftragsnummer;
		this.buchungstext = buchungstext;
		this.standortid = standortid;
		this.wfpath = wfpath;
		this.sip=sip;
		this.assignment=assignment;

	}

	public String getVertragid() {
		return vertragid;
	}

	public void setVertragid(String vertragid) {
		this.vertragid = vertragid;
	}

	public String getAuftragsnummer() {
		return auftragsnummer;
	}

	public void setAuftragsnummer(String auftragsnummer) {
		this.auftragsnummer = auftragsnummer;
	}

	public String getBuchungstext() {
		return buchungstext;
	}

	public void setBuchungstext(String buchungstext) {
		this.buchungstext = buchungstext;
	}

	public String getStandortid() {
		return standortid;
	}

	public void setStandortid(String standortid) {
		this.standortid = standortid;
	}

	public String getWfpath() {
		return wfpath;
	}

	public void setWfpath(String wfpath) {
		this.wfpath = wfpath;
	}

	public void setSip(String sip) { this.sip=sip; }

	public String getSip() { return sip;}

	public void setAssignment(String assignment) { this.assignment=assignment; }

	public String getAssignment() { return assignment;}
}
