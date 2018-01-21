package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Employee {
    @Id
    private int id;
    private String name;
    private long salary;
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @ManyToOne
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Collection<Employee> directs;

    @ManyToOne
    private Department department;

    @ManyToMany
    private Collection<Project> projects;

    public Employee() {
        projects = new ArrayList<Project>();
        directs = new ArrayList<Employee>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSalary() {
        return salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Department getDepartment() {
        return department;
    }

    public Collection<Employee> getDirects() {
        return directs;
    }

    public Employee getManager() {
        return manager;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setDirects(Collection<Employee> directs) {
        this.directs = directs;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    public String toString() {
        return "Employee " + getId() +
                ": name: " + getName() +
                ", salary: " + getSalary() +
                ", dept: " + ((getDepartment() == null) ? null : getDepartment().getName());
    }
}
