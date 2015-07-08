package Controller;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Aw on 30/4/2015.
 */
public class Interval implements Serializable {
    @DatabaseField (generatedId=true)
    private int id;
    @DatabaseField
    private String child_id;
    @DatabaseField
    private String interval;
    @DatabaseField
    private String engagement;
    @DatabaseField
    private String physcial;
    @DatabaseField
    private String adults;
    @DatabaseField
    private String peers;
    @DatabaseField
    private String materials;
    @DatabaseField
    private String noneOthers;
    @DatabaseField
    private String childName;
    @DatabaseField
    private String sessionNo;
    @DatabaseField
    private String flag;

    public Interval(){}

    public Interval (int id){this.id = id;}

    public Interval(int id, String child_id, String interval, String engagement, String physcial, String adults, String peers, String materials, String noneOthers, String childName, String sessionNo, String flag) {
        this.id = id;
        this.child_id = child_id;
        this.interval = interval;
        this.engagement = engagement;
        this.physcial = physcial;
        this.adults = adults;
        this.peers = peers;
        this.materials = materials;
        this.noneOthers = noneOthers;
        this.childName = childName;
        this.sessionNo = sessionNo;
        this.flag = flag;
    }

    public Interval(String child_id, String interval, String engagement, String physcial, String adults, String peers, String materials, String noneOthers, String childName, String sessionNo, String flag) {
        this.child_id = child_id;
        this.interval = interval;
        this.engagement = engagement;
        this.physcial = physcial;
        this.adults = adults;
        this.peers = peers;
        this.materials = materials;
        this.noneOthers = noneOthers;
        this.childName = childName;
        this.sessionNo = sessionNo;
        this.flag = flag;
    }

    public Interval(String interval, String engagement, String physcial, String adults, String peers, String materials, String noneOthers, String childName, String sessionNo, String flag) {
        this.interval = interval;
        this.engagement = engagement;
        this.physcial = physcial;
        this.adults = adults;
        this.peers = peers;
        this.materials = materials;
        this.noneOthers = noneOthers;
        this.childName = childName;
        this.sessionNo = sessionNo;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }

    public String getPhyscial() {
        return physcial;
    }

    public void setPhyscial(String physcial) {
        this.physcial = physcial;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getPeers() {
        return peers;
    }

    public void setPeers(String peers) {
        this.peers = peers;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getNoneOthers() {
        return noneOthers;
    }

    public void setNoneOthers(String noneOthers) {
        this.noneOthers = noneOthers;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getSessionNo() {
        return sessionNo;
    }

    public void setSessionNo(String sessionNo) {
        this.sessionNo = sessionNo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

