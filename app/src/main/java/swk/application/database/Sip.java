package swk.application.database;

/**
 * Created by endres on 06.03.2017.
 */

public class Sip {


    int id;
    String sipid;
    String sipuser;
    String von;
    String bis;
    String pass;
    String rufnummer;
    String realm;

    public Sip(String sipid,String sipuser, String von,
                   String bis, String pass, String rufnummer, String realm) {
        this.sipid = sipid;
        this.sipuser = sipuser;
        this.von = von;
        this.bis = bis;
        this.pass = pass;
        this.rufnummer = rufnummer;
        this.realm = realm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSipId(){
        return sipid;
    }

    public void setSipid(String sipid){
        this.sipid = sipid;
    }

    public String getSipuser() {
        return sipuser;
    }

    public void setSipuser(String sipuser) {
        this.sipuser = sipuser;
    }

    public String getVon() {
        return von;
    }

    public void setVon(String von) {
        this.von = von;
    }

    public String getBis() {
        return bis;
    }

    public void setBis(String bis) {
        this.bis = bis;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRufnummer() {
        return rufnummer;
    }

    public void setRufnummer(String rufnummer) {
        this.rufnummer = rufnummer;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
