package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMPLOYEE_STAGE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EMP_TYPE")
@SqlResultSetMapping(
        name = "EmployeeStageMapping",
        entities =
        @EntityResult(
                entityClass = Employee.class,
                discriminatorColumn = "EMP_TYPE",
                fields = {
                        @FieldResult(name = "startDate", column = "START_DATE"),
                        @FieldResult(name = "dailyRate", column = "DAILY_RATE"),
                        @FieldResult(name = "hourlyRate", column = "HOURLY_RATE")
                }
        )
)
public abstract class Employee extends CachedEntity {
    @Id
    private int id;
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

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
