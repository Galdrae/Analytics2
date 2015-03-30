package Controller;

/**
 * Created by Deo's Friend on 3/14/2015.
 */
public class Child {

    private String id;
    private String childName;
    private String age;
    private int icon;

    public Child(){}

    public Child(int icon) {
        this.icon = icon;
    }

    public Child(String childName, String age, int icon) {
        this.childName = childName;
        this.age = age;
        this.icon = icon;
    }

    public Child(String id, String childName, String age, int icon) {
        this.id = id;
        this.childName = childName;
        this.age = age;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
