package model;

import javax.persistence.*;

@Entity
public class Employee {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  private String name;
  private long salary;

  @ManyToOne
  @JoinTable
    (
      name="EMP_DEP",
      joinColumns={ @JoinColumn(name="EMP_ID", referencedColumnName="ID",
        insertable = false, updatable = false) },
      inverseJoinColumns={ @JoinColumn(name="DEP_ID", referencedColumnName="ID",
        unique=true, insertable =  false, updatable = false)}
    )
  private Department department;

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

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public String toString() {
    return "Employee id: " + getId() + " name: " + getName() +
      " with " + getDepartment();
  }
}