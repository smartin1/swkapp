package swk.application.database;

/**
 * Created by endres on 12.06.2017.
 */

public class CPE {

    int id;
    String cpeid;
    String oui;
    String mac_mta;
    String cwmp;
    String modelname;
    String productclass;
    String active;
    String serial;
    String mac_cm;
    String manufacturer;

    public CPE(String cpeid, String oui,String mac_mta, String cwmp,
                      String modelname, String productclass, String active, String serial, String mac_cm, String manufacturer) {
        this.cpeid=cpeid;
        this.oui=oui;
        this.mac_mta=mac_mta;
        this.cwmp = cwmp;
        this.modelname = modelname;
        this.productclass = productclass;
        this.active = active;
        this.serial = serial;
        this.mac_cm = mac_cm;
        this.manufacturer= manufacturer;
    }

    public String getCpeid() {
        return cpeid;
    }

    public void setCpeid(String cpeid) {
        this.cpeid = cpeid;
    }

    public String getOui() {
        return oui;
    }

    public void setOui(String oui) {
        this.oui = oui;
    }

    public String getMac_mta() {
        return mac_mta;
    }

    public void setMac_mta(String mac_mta) {
        this.mac_mta = mac_mta;
    }

    public String getCwmp() {
        return cwmp;
    }

    public void setCwmp(String cwmp) {
        this.cwmp = cwmp;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getProductclass() {
        return productclass;
    }

    public void setProductclass(String productclass) {
        this.productclass = productclass;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMac_cm() {
        return mac_cm;
    }

    public void setMac_cm(String mac_cm) {
        this.mac_cm = mac_cm;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
