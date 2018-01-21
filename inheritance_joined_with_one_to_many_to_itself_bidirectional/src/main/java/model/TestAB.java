package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class TestAB extends Node {

    @Column(name = "TEST_AB_START_DATE")
    private Date testABstartDate;

    @Column(name = "TEST_AB_END_DATE")
    private Date testABendDate;

    public Date getTestABstartDate() {
        return testABstartDate;
    }

    public void setTestABstartDate(Date testABstartDate) {
        this.testABstartDate = testABstartDate;
    }

    public Date getTestABendDate() {
        return testABendDate;
    }

    public void setTestABendDate(Date testABendDate) {
        this.testABendDate = testABendDate;
    }
}
