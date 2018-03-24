package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "EMP")
@SqlResultSetMapping(
        name = "EmployeeDetailMapping",
        classes = {
                @ConstructorResult(
                        targetClass = EmployeeDetails.class,
                        columns = {
                                @ColumnResult(name = "name"),
                                @ColumnResult(name = "salary", type = Long.class),
                                @ColumnResult(name = "deptName")
                        })})
public class Employee {
    @Id
    @Column(name = "EMP_ID")
    private int id;
    private String name;
    private long salary;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DEPT_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Collection<Employee> directs = new ArrayList<Employee>();

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

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Collection<Employee> getDirects() {
        return directs;
    }

    public void addDirect(Employee employee) {
        if (!getDirects().contains(employee)) {
            getDirects().add(employee);
            if (employee.getManager() != null) {
                employee.getManager().getDirects().remove(employee);
            }
            employee.setManager(this);
        }
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public String toString() {
        return "Employee id: " + getId() + " name: " + getName() +
                " with MgrId: " + (getManager() == null ? null : getManager().getId());
    }
}
