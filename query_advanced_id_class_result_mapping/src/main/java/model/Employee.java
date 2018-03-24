package model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;


@Entity
@Table(name = "EMP")
@SqlResultSetMapping(
        name = "EmployeeAndManager",
        entities = {
                @EntityResult(entityClass = Employee.class),
                @EntityResult(
                        entityClass = Employee.class,
                        fields = {
                                @FieldResult(name = "country", column = "MGR_COUNTRY"),
                                @FieldResult(name = "id", column = "MGR_ID"),
                                @FieldResult(name = "name", column = "MGR_NAME"),
                                @FieldResult(name = "manager.country", column = "MGR_MGR_COUNTRY"),
                                @FieldResult(name = "manager.id", column = "MGR_MGR_ID")
                        }
                )
        }
)
public class Employee {
    private String country;
    @Id
    private int id;
    private String name;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "MGR_ID", referencedColumnName = "ID")
    })
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Collection<Employee> directs = new ArrayList<Employee>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Employee> getDirects() {
        return directs;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

}
