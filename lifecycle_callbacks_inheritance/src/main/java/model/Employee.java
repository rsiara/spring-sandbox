package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMP")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "EMP_TYPE", discriminatorType = DiscriminatorType.INTEGER)
@EntityListeners(NameValidator.class)
public abstract class Employee implements NamedEntity {
    @Id
    private int id;
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "S_DATE")
    private Date startDate;
    @Transient
    private long syncTime;

    @PostPersist
    @PostUpdate
    @PostLoad
    private void resetSyncTime() {
        System.out.println("Employee.resetSyncTime called on employee: " + getId());
        syncTime = System.currentTimeMillis();
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
