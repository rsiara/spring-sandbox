package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "dept")
    private Set<Employee> employees = new HashSet<Employee>();

    public Department() {
    }

    public int getId() {
        return id;
    }

    public void setId(int deptNo) {
        this.id = deptNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String deptName) {
        this.name = deptName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public String toString() {
        return "Department no: " + getId() +
                ", name: " + getName();
    }
}
