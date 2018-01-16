package model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
    - Wazne zeby nie uzywac mixed access type, albo PROPERTY albo FIELD.
    - Nalezy pamietac zeby notacja @Id takze znajdowala sie przy metodzie getId(), jezeli bedzie znajdowala sie
    przy polu to jej nie znajdzie
 */

@Entity
@Access(AccessType.PROPERTY)
public class Employee {

    private int id;
    private String name;
    private long salary;

    @Id
    public int getId() {
        System.out.println("getId()");
        return id;
    }

    public void setId(int id) {
        System.out.println("setId(" + id + ")");
        this.id = id;
    }

    public String getName() {
        System.out.println("getName()");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName(" + name + ")");
        this.name = name;
    }

    public long getSalary() {
        System.out.println("getSalary()");
        return salary;
    }

    public void setSalary(long salary) {
        System.out.println("setSalary(" + salary + ")");
        this.salary = salary;
    }

    public String toString() {
        return "Employee id: " + getId() + " name: " + getName() + " salary: " + getSalary();
    }
}
