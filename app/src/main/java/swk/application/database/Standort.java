package swk.application.database;

/**
 * Datenbanktabelle Standort
 * @author endres
 *
 */
public class Standort {
	int id;
	String standortid;
	String lage;
	String strasse;
	String bemerkungW;
	String bemerkungG;
	String tk;
	String tkPlan;
	String plz;
	String ort;
	String stockwerk;
	String hhnr;
	String raum;
	String hhnrzusatz;
	
	
	public Standort(String standortid, String lage, String strasse, String bemerkungW, String bemerkungG, String tk, String tkPlan, String plz, String ort, String stockwerk, String hhnr, String raum, String hhnrzusatz){
		this.standortid=standortid;
		this.lage=lage;
		this.strasse=strasse;
		this.bemerkungW=bemerkungW;
		this.bemerkungG=bemerkungG;
		this.tk=tk;
		this.tkPlan=tkPlan;
		this.plz=plz;
		this.ort=ort;
		this.stockwerk=stockwerk;
		this.hhnr=hhnr;
		this.raum=raum;
		this.hhnrzusatz=hhnrzusatz;
		this.id = (int) Math.random()*1000000;	
	}
	
	


	public String getStandortid() {
		return standortid;
	}


	public void setStandortid(String standortid) {
		this.standortid = standortid;
	}


	public String getLage() {
		return lage;
	}


	public void setLage(String lage) {
		this.lage = lage;
	}


	public String getStrasse() {
		return strasse;
	}


	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}


	public String getBemerkungW() {
		return bemerkungW;
	}


	public void setBemerkungW(String bemerkungW) {
		this.bemerkungW = bemerkungW;
	}


	public String getBemerkungG() {
		return bemerkungG;
	}


	public void setBemerkungG(String bemerkungG) {
		this.bemerkungG = bemerkungG;
	}


	public String getTk() {
		return tk;
	}


	public void setTk(String tk) {
		this.tk = tk;
	}


	public String getTkPlan() {
		return tkPlan;
	}


	public void setTkPlan(String tkPlan) {
		this.tkPlan = tkPlan;
	}


	public String getPlz() {
		return plz;
	}


	public void setPlz(String plz) {
		this.plz = plz;
	}


	public String getOrt() {
		return ort;
	}


	public void setOrt(String ort) {
		this.ort = ort;
	}


	public String getStockwerk() {
		return stockwerk;
	}


	public void setStockwerk(String stockwerk) {
		this.stockwerk = stockwerk;
	}


	public String getHhnr() {
		return hhnr;
	}


	public void setHhnr(String hhnr) {
		this.hhnr = hhnr;
	}


	public String getRaum() {
		return raum;
	}


	public void setRaum(String raum) {
		this.raum = raum;
	}


	public String getHhnrzusatz() {
		return hhnrzusatz;
	}


	public void setHhnrzusatz(String hhnrzusatz) {
		this.hhnrzusatz = hhnrzusatz;
	}
	
}
