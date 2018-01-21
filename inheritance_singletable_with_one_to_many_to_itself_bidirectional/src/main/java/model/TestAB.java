package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorColumn(name = "NODE_TYPE")
@DiscriminatorValue(value = "TEST_AB")
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
