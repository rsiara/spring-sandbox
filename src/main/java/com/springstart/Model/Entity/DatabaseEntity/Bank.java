package com.springstart.Model.Entity.DatabaseEntity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "BANK")
public class Bank {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "BANK_ID")
  private Long bankId;

  @Column(name = "NAME")
  private String name;

  @Embedded
  private Address address = new Address();

  @Column(name = "IS_INTERNATIONAL")
  private boolean international;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATED_DATE")
  private Date lastUpdatedDate;

  @Column(name = "LAST_UPDATED_BY")
  private String lastUpdatedBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_BY")
  private String createdBy;

  @ElementCollection
  @CollectionTable(name = "BANK_CONTACT", joinColumns = @JoinColumn(name = "BANK_ID"))
  @Column(name = "NAME")
  @OrderColumn(name="ORDER_CONTACT")
  private List<String> contacts = new ArrayList<String>();


  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
    name="USER_BANK",
    joinColumns = @JoinColumn(name="BANK", referencedColumnName = "BANK_ID"),
    inverseJoinColumns = @JoinColumn(name="USER", referencedColumnName = "USER_ID")
  )
  private Map<FinanceUserFullName, FinanceUser> usersBySkingColor;

  public Map<FinanceUserFullName, FinanceUser> getUsersBySkingColor() {
    return usersBySkingColor;
  }

  public void setUsersBySkingColor(Map<FinanceUserFullName, FinanceUser> usersBySkingColor) {
    this.usersBySkingColor = usersBySkingColor;
  }

  public Long getBankId() {
    return bankId;
  }

  public void setBankId(Long bankId) {
    this.bankId = bankId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public boolean isInternational() {
    return international;
  }

  public void setInternational(boolean international) {
    this.international = international;
  }

  public Date getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public List<String> getContacts() {
    return contacts;
  }

  public void setContacts(List<String> contacts) {
    this.contacts = contacts;
  }
}
