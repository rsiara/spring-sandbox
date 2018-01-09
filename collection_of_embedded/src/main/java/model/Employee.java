package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {
  @Id private int id;
  private String name;
  private long salary;

  @ElementCollection
  @CollectionTable(name = "USER_ADDRESS", joinColumns = @JoinColumn(name = "USER_ID"))
  @AttributeOverrides({
    @AttributeOverride(name = "state", column = @Column(name = "PROVINCE")), // pole "state" ma lądować w kolumnie "PROVINCE"
    @AttributeOverride(name = "zip", column = @Column(name = "POSTAL_CODE")) // pole "zip" ma lądować w kolumnie "POSTAL_CODE"
  })
  private List<Address> addresses = new ArrayList<Address>();

  public Employee() {}

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


  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public String toString() {
    return "Employee id: " + getId() + " name: " + getName() +
      " salary: " + getSalary() + " addresses: " + addresses.toString();
  }
}