package model;

import java.net.URL;

import javax.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    @Convert(converter = UpperCaseConverter.class, attributeName = "lastName")
    private EmployeeName empName;

    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean fullTime;

    @Embedded
    @Convert(converter = BooleanToIntegerConverter.class, attributeName = "bonded")
    private SecurityInfo securityInfo;

    private URL homePage;

    public int getId() {
        return id;
    }

    public String toString() {
        return "Employee " + getId() +
                ": name: " + empName +
                ", fullTime: " + fullTime +
                ", homePage: " + homePage.getClass().getSimpleName() + "(" + homePage.toString() + ")" +
                ", secInfo: " + securityInfo.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public EmployeeName getEmpName() {
        return empName;
    }

    public void setEmpName(EmployeeName empName) {
        this.empName = empName;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public SecurityInfo getSecurityInfo() {
        return securityInfo;
    }

    public void setSecurityInfo(SecurityInfo securityInfo) {
        this.securityInfo = securityInfo;
    }

    public URL getHomePage() {
        return homePage;
    }

    public void setHomePage(URL homePage) {
        this.homePage = homePage;
    }
}
