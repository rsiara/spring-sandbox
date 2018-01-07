package com.springstart.Model.Entity.DatabaseEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FinanceUserFullName {

  @Column(name="first_name", insertable=false, updatable=false)
  private String first_name;
  @Column(name="last_name", insertable=false, updatable=false)
  private String last_name;

  public FinanceUserFullName() {
  }

  public FinanceUserFullName(String first_name, String last_name) {
    this.first_name = first_name;
    this.last_name = last_name;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }
}
