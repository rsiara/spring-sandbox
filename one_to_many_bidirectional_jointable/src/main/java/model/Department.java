package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Department {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  private String name;
  @OneToMany
  @JoinTable
    (
      name="EMP_DEP",
      joinColumns={ @JoinColumn(name="EMP_ID", referencedColumnName="ID") },
      inverseJoinColumns={ @JoinColumn(name="DEP_ID", referencedColumnName="ID", unique=true)}
    )
  private Collection<Employee> employees;

  public Department() {
    employees = new ArrayList<Employee>();
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

  public void setName(String deptName) {
    this.name = deptName;
  }

  public void addEmployee(Employee employee) {
    if (!getEmployees().contains(employee)) {
      getEmployees().add(employee);
      if (employee.getDepartment() != null) {
        employee.getDepartment().getEmployees().remove(employee);
      }
      employee.setDepartment(this);
    }
  }

  public Collection<Employee> getEmployees() {
    return employees;
  }

  public String toString() {
    return "Department id: " + getId() +
      ", name: " + getName();
  }
}