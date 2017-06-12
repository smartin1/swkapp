package swk.application.database;

/**
 * Created by endres on 08.06.2017.
 */

public class Assignment {
    int id;
    String assignmentid;
    String cwmp;
    String gid;
    String cid;
    String wid;
    String valid_from;
    String valid_until;


    public Assignment(String assignmentid, String cwmp ,String gid, String cid,
               String wid, String valid_from, String valid_until) {
        this.assignmentid=assignmentid;
        this.cwmp = cwmp;
        this.gid = gid;
        this.cid = cid;
        this.wid = wid;
        this.valid_from = valid_from;
        this.valid_until= valid_until;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignmentid(){
        return assignmentid;
    }

    public void setAssignmentid(String assignmentid){
        this.assignmentid=assignmentid;
    }

    public String getCwmp() {
        return cwmp;
    }

    public void setCwmp(String cwmp) {
        this.cwmp = cwmp;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_until() {
        return valid_until;
    }

    public void setValid_until(String valid_until) {
        this.valid_until = valid_until;
    }
}
