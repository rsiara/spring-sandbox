package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DateWithTemporalSandbox {
    @Id
    private int id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATETIME_COLUMN")
    private Date datetimeColumn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP_COLUMN")
    private Date timestampColumn;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_COLUMN")
    private Date dateColumn;

    @Temporal(TemporalType.TIME)
    @Column(name = "TIME_COLUMN")
    private Date timeColumn;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatetimeColumn() {
        return datetimeColumn;
    }

    public void setDatetimeColumn(Date datetimeColumn) {
        this.datetimeColumn = datetimeColumn;
    }

    public Date getTimestampColumn() {
        return timestampColumn;
    }

    public void setTimestampColumn(Date timestampColumn) {
        this.timestampColumn = timestampColumn;
    }

    public Date getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(Date dateColumn) {
        this.dateColumn = dateColumn;
    }

    public Date getTimeColumn() {
        return timeColumn;
    }

    public void setTimeColumn(Date timeColumn) {
        this.timeColumn = timeColumn;
    }
}
