package Controller;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by L335A15 on 4/29/2015.
 */
public class Grade_Child  implements Serializable {

    @DatabaseField (generatedId=true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String child_Id;
    @DatabaseField
    private float qns1;
    @DatabaseField
    private float qns2;
    @DatabaseField
    private float qns3;
    @DatabaseField
    private float qns4;
    @DatabaseField
    private float qns5;

    public Grade_Child(){}

    public Grade_Child(int id) {
        this.id = id;
    }

    public Grade_Child(int id, float qns5, float qns4, float qns3, String name, String childId, float qns1, float qns2) {
        this.id = id;
        this.qns5 = qns5;
        this.qns4 = qns4;
        this.qns3 = qns3;
        this.name = name;
        this.child_Id = childId;
        this.qns1 = qns1;
        this.qns2 = qns2;
    }

    public Grade_Child(String name, String childId, float qns1, float qns2, float qns3, float qns4, float qns5) {
        this.name = name;
        this.child_Id = childId;
        this.qns1 = qns1;
        this.qns2 = qns2;
        this.qns3 = qns3;
        this.qns4 = qns4;
        this.qns5 = qns5;
    }

    public Grade_Child(String name, float qns1, float qns2, float qns3, float qns4, float qns5) {
        this.name = name;
        this.qns1 = qns1;
        this.qns2 = qns2;
        this.qns3 = qns3;
        this.qns4 = qns4;
        this.qns5 = qns5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildId() {
        return child_Id;
    }

    public void setChildId(String childId) {
        this.child_Id = childId;
    }

    public float getQns1() {
        return qns1;
    }

    public void setQns1(float qns1) {
        this.qns1 = qns1;
    }

    public float getQns2() {
        return qns2;
    }

    public void setQns2(float qns2) {
        this.qns2 = qns2;
    }

    public float getQns3() {
        return qns3;
    }

    public void setQns3(float qns3) {
        this.qns3 = qns3;
    }

    public float getQns4() {
        return qns4;
    }

    public void setQns4(float qns4) {
        this.qns4 = qns4;
    }

    public float getQns5() {
        return qns5;
    }

    public void setQns5(float qns5) {
        this.qns5 = qns5;
    }
}


