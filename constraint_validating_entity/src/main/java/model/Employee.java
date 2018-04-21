package model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;


@Entity
public class Employee {

    @NotNull
    @Id
    private Integer id;

    @NotNull
    @Size(max = 40)
    private String name;

    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "S_DATE")
    private Date startDate;
    /*

        !IMPORTANT!

        The Bean Validation API does not only allow to validate single class instances but also complete object graphs.
        To do so, just annotate a field or property representing a reference to another object with @Valid.
        If the parent object is validated, all referenced objects annotated with
        @Valid will be validated as well (as will be their children etc.).

    */
    @Embedded
    @Valid
    private EmployeeInfo info;

    public EmployeeInfo getInfo() {
        return info;
    }

    public void setInfo(EmployeeInfo info) {
        this.info = info;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String toString() {
        return "Employee id: " + getId() + " name: " + getName();
    }
}
