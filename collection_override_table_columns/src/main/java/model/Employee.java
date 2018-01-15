package model;

import java.util.Collection;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;

    // Using a targetClass instead of generics
    @ElementCollection(targetClass = VacationEntry.class)
    @CollectionTable(name = "VACATION",
            joinColumns = @JoinColumn(name = "EMP_ID"))
    @AttributeOverride(name = "daysTaken",
            column = @Column(name = "DAYS_ABS"))
    private Collection vacationBookings;

    // Using generics in place of a targetClass
    @ElementCollection
    @Column(name = "NICKNAME")
    private Set<String> nickNames;

    public Collection getVacationBookings() {
        return vacationBookings;
    }

    public void setVacationBookings(Collection vacationBookings) {
        this.vacationBookings = vacationBookings;
    }

    public Set<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(Set<String> nickNames) {
        this.nickNames = nickNames;
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

    public String toString() {
        return "Employee id: " + getId() + " name: " + getName() +
                " salary: " + getSalary() +
                " nickNames: " + nickNames;
    }
}
