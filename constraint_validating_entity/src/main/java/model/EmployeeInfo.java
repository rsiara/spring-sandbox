package model;


import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import java.util.Date;

@Embeddable
public class EmployeeInfo {
    @Past
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Embedded
    private PersonInfo spouse;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public PersonInfo getSpouse() {
        return spouse;
    }

    public void setSpouse(PersonInfo spouse) {
        this.spouse = spouse;
    }
}