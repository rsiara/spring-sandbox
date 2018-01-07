package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Employee {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  private String name;
  private long salary;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name="EMP_PHONE",
    joinColumns=@JoinColumn(name="EMP_ID"),
    inverseJoinColumns=@JoinColumn(name="PHONE_ID"))
  private Collection<Phone> phones;

  public Employee() {
    phones = new ArrayList<Phone>();
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

  public void setName(String name) {
    this.name = name;
  }

  public long getSalary() {
    return salary;
  }

  public void setSalary(long salary) {
    this.salary = salary;
  }

  public void addPhone(Phone phone) {
    if (!getPhones().contains(phone)) {
      getPhones().add(phone);
    }
  }

  public Collection<Phone> getPhones() {
    return phones;
  }

  public String toString() {
    return "Employee id: " + getId() + " name: " + getName() +
      " with " + getPhones().size() + " phones";
  }
}