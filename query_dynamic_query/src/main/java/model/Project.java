package model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String name;
    @ManyToMany(mappedBy = "projects")
    private Collection<Employee> employees;

    public Project() {
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

    public String toString() {
        return "Project id: " + getId() + ", name: " + getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }
}
