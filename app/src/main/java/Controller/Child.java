package Controller;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;
/**
 * Created by Deo's Friend on 3/14/2015.
 */
public class Child implements Serializable {
    @DatabaseField (generatedId=true)
    private int id;
    @DatabaseField
    private String childName;
    @DatabaseField
    private String PriDi;
    @DatabaseField
    private String SecDi;
    @DatabaseField
    private String remarks;
    @DatabaseField
    private String inspecter;
    @DatabaseField
    private String venue;
    @DatabaseField
    private String activity;
    @DatabaseField
    private String noAdults;
    @DatabaseField
    private String noChildren;
    @DatabaseField
    private String gender;
    @DatabaseField
    private int image;
    @DatabaseField
    private String status;
    @DatabaseField
    private String sessionNo;

    public Child(){}

    public Child(int id, String childName, String priDi, String secDi, String remarks, String inspecter, String venue, String activity, String noAdults, String noChildren, String gender, int image, String status, String sessionNo) {
        this.id = id;
        this.childName = childName;
        PriDi = priDi;
        SecDi = secDi;
        this.remarks = remarks;
        this.inspecter = inspecter;
        this.venue = venue;
        this.activity = activity;
        this.noAdults = noAdults;
        this.noChildren = noChildren;
        this.gender = gender;
        this.image = image;
        this.status = status;
        this.sessionNo = sessionNo;
    }

    public Child(String childName, String gender, String secDi, String priDi, String remarks, String inspecter, String venue, String activity, String noAdults, String noChildren, int image, String status, String sessionNo) {
        this.childName = childName;
        this.gender = gender;
        SecDi = secDi;
        PriDi = priDi;
        this.remarks = remarks;
        this.inspecter = inspecter;
        this.venue = venue;
        this.activity = activity;
        this.noAdults = noAdults;
        this.noChildren = noChildren;
        this.image = image;
        this.status = status;
        this.sessionNo = sessionNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getPriDi() {
        return PriDi;
    }

    public void setPriDi(String priDi) {
        PriDi = priDi;
    }

    public String getSecDi() {
        return SecDi;
    }

    public void setSecDi(String secDi) {
        SecDi = secDi;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInspecter() {
        return inspecter;
    }

    public void setInspecter(String inspecter) {
        this.inspecter = inspecter;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getNoAdults() {
        return noAdults;
    }

    public void setNoAdults(String noAdults) {
        this.noAdults = noAdults;
    }

    public String getNoChildren() {
        return noChildren;
    }

    public void setNoChildren(String noChildren) {
        this.noChildren = noChildren;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionNo() {
        return sessionNo;
    }

    public void setSessionNo(String sessionNo) {
        this.sessionNo = sessionNo;
    }
}
