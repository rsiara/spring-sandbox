package com.springstart.jpa.chapterfive.sharing_embeddable_key_mappings_with_values.model;
import javax.persistence.*;

@Entity(name = "Employee_sharing_embeddable_key_mappings_with_values")
public class Employee {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  @Column(name="F_NAME")
  private String firstName;
  @Column(name="L_NAME")
  private String lastName;
  private long salary;

  @ManyToOne
  private Department department;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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
    StringBuffer aBuffer = new StringBuffer("Employee ");
    aBuffer.append(" id: ");
    aBuffer.append(id);
    aBuffer.append(" name: ");
    aBuffer.append(lastName);
    aBuffer.append(", ");
    aBuffer.append(firstName);
    aBuffer.append(" with dept: ");
    if(null != department) {
      aBuffer.append(department.getName());
    }
    return aBuffer.toString();
  }
}