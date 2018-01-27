package model;

import javax.persistence.*;


@NamedQuery(name = "findEmployeeById",
        query = "SELECT e FROM Employee e WHERE e.id = ?1",
        hints = {@QueryHint(name = "javax.persistence.query.timeout", value = "30")})
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSalary() {
        return salary;
    }

    public String toString() {
        return "model.Employee " + getId() +
                ": name: " + getName() +
                ", salary: " + getSalary();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
