package model;

import java.util.Map;
import java.util.HashMap;

import javax.persistence.*;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    /*Element collections of basic types are simple to convert. They are annotated the same way as basic attributes that
    are not collections. Thus, if we had an attribute that was of type List<Boolean>, we would annotate it just as we did
    our bonded attribute above, and all of the boolean values in the collection would be converted:
    @ElementCollection
    @Convert(converter=BooleanToIntegerConverter.class)
    private List<Boolean> securityClearances;*/

    /*If an element collection is a Map with values that are of a basic type, then the values of the Map will be converted.
    To perform conversion on the keys, the attributeName element should be used with a special value of “key” to
    indicate that the keys of the Map are to be converted instead of the values.*/

    /*Using the domain model in Listing 5-17, if we wanted to convert
    the employee last name to be stored as uppercase characters (assuming we have defined the corresponding converter
    class), we would annotate the attribute as shown in Listing 10-4:*/

    @ManyToMany
    @MapKey(name = "empName")
    @Convert(converter = UpperCaseConverter.class, attributeName = "key.lastName")
    private Map<EmployeeName, Employee> employees;

    public Department() {
        employees = new HashMap<EmployeeName, Employee>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<EmployeeName, Employee> getEmployees() {
        return employees;
    }

    public String toString() {
        String emps = "{ ";
        for (EmployeeName empName : getEmployees().keySet()) {
            emps += "(" + empName.toString() + ") ";
        }
        emps += "}";
        return "Department no: " + getId() +
                ", name: " + getName() +
                ", employees: " + emps;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(Map<EmployeeName, Employee> employees) {
        this.employees = employees;
    }
}
