package model;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
public class PrintQueue {

    @Id
    private String name;

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL)
    @OrderColumn(name = "PRINT_ORDER")
    private List<PrintJob> jobs;

    public PrintQueue() {
        jobs = new ArrayList<PrintJob>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrintJob> getJobs() {
        return jobs;
    }

    public void addJob(PrintJob job) {
        this.jobs.add(job);
        job.setQueue(this);
    }

    public String toString() {
        return "PrintQueue: " + name;
    }
}
