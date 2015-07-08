package Controller;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Aw on 30/4/2015.
 */
public class Session implements Serializable  {
    @DatabaseField (generatedId=true)
    private int id;
    @DatabaseField
    private String center_id;
    @DatabaseField
    private String child_Id;
    @DatabaseField
    private String observer;
    @DatabaseField
    private String sessionCount;
    @DatabaseField
    private String date;
    @DatabaseField
    private String startTime;
    @DatabaseField
    private String endTime;
    @DatabaseField
    private String noInterval;
    @DatabaseField
    private String noFlags;
    @DatabaseField
    private String sessionChildName;
    @DatabaseField
    private String sessionStatus;

    public Session (){}

    public Session (int id){this.id = id;}

    public Session(int id, String center_id, String child_Id, String observer, String sessionCount, String date, String startTime, String endTime, String noInterval, String noFlags, String sessionChildName, String sessionStatus) {
        this.id = id;
        this.center_id = center_id;
        this.child_Id = child_Id;
        this.observer = observer;
        this.sessionCount = sessionCount;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noInterval = noInterval;
        this.noFlags = noFlags;
        this.sessionChildName = sessionChildName;
        this.sessionStatus = sessionStatus;
    }

    public Session(String center_id, String child_Id, String observer, String sessionCount, String date, String startTime, String endTime, String noInterval, String noFlags, String sessionChildName, String sessionStatus) {
        this.center_id = center_id;
        this.child_Id = child_Id;
        this.observer = observer;
        this.sessionCount = sessionCount;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noInterval = noInterval;
        this.noFlags = noFlags;
        this.sessionChildName = sessionChildName;
        this.sessionStatus = sessionStatus;
    }

    public Session(int id, String endTime, String noInterval, String noFlags, String sessionStatus) {
        this.id = id;
        this.endTime = endTime;
        this.noInterval = noInterval;
        this.noFlags = noFlags;
        this.sessionStatus = sessionStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getChild_Id() {
        return child_Id;
    }

    public void setChild_Id(String child_Id) {
        this.child_Id = child_Id;
    }

    public String getObserver() {
        return observer;
    }

    public void setObserver(String observer) {
        this.observer = observer;
    }

    public String getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(String sessionCount) {
        this.sessionCount = sessionCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNoInterval() {
        return noInterval;
    }

    public void setNoInterval(String noInterval) {
        this.noInterval = noInterval;
    }

    public String getNoFlags() {
        return noFlags;
    }

    public void setNoFlags(String noFlags) {
        this.noFlags = noFlags;
    }

    public String getSessionChildName() {
        return sessionChildName;
    }

    public void setSessionChildName(String sessionChildName) {
        this.sessionChildName = sessionChildName;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
