package model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Collection<Employee> employees;

    public Department() {
        employees = new ArrayList<Employee>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }

    public String toString() {
        return "Department no: " + getId() +
                ", name: " + getName();
    }

    public void addEmployee(Employee employee) {
        if (!getEmployees().contains(employee)) {
            getEmployees().add(employee);
            employee.setDepartment(this);
        }
    }
}
