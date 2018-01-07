package com.springstart.jpa.chapterfive.sharing_embeddable_key_mappings_with_values.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Entity(name = "Department_sharing_embeddable_key_mappings_with_values")
public class Department {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  private String name;

  @OneToMany(mappedBy="department", cascade = CascadeType.ALL)
  private Map<EmployeeName, Employee> employeesByName;


  public Department() {
    employeesByName = new HashMap<EmployeeName,Employee>();
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

  public Map<EmployeeName, Employee> getEmployees() {
    return employeesByName;
  }

  public void addEmployee(Employee employee) {
    EmployeeName empName = new EmployeeName();
    empName.setFirst_Name(employee.getFirstName());
    empName.setLast_Name(employee.getLastName());
    employeesByName.put(empName, employee);
    if (employee.getDepartment() != null) {
      employee.getDepartment().removeEmployee(employee);
    }
    employee.setDepartment(this);
  }

  public void removeEmployee(Employee employee) {
    Iterator iter = employeesByName.entrySet().iterator();
    while (iter.hasNext()) {
      Employee current = ((Map.Entry<EmployeeName,Employee>) iter.next()).getValue();
      if (current.getId() == employee.getId()) {
        iter.remove();
        current.setDepartment(null);
      }
    }
  }

  public String toString() {
    StringBuffer aBuffer = new StringBuffer("Department ");
    aBuffer.append(" id: ");
    aBuffer.append(id);
    aBuffer.append(" name: ");
    aBuffer.append(name);
    aBuffer.append(" employeeCount: ");
    aBuffer.append(employeesByName.size());
    return aBuffer.toString();
  }
}