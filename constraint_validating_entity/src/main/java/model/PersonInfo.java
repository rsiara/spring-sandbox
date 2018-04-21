package model;

import javax.persistence.Embeddable;

@Embeddable
public class PersonInfo {
    public int notUsed;

    public int getNotUsed() {
        return notUsed;
    }

    public void setNotUsed(int notUsed) {
        this.notUsed = notUsed;
    }
}