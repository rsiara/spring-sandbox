package com.springstart.Model.Entity.DatabaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "INVESTOR")
    private String investor;

    @ManyToMany(mappedBy = "projects")
    private Collection<FinanceUser> financeUsers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    public Collection<FinanceUser> getFinanceUsers() {
        return financeUsers;
    }

    public void setFinanceUsers(Collection<FinanceUser> financeUsers) {
        this.financeUsers = financeUsers;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", investor='" + investor + '\'' +
                '}';
    }
}
