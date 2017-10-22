package com.springstart.Model.Entity.DatabaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "finances_user")
public class FinanceUser {

  @Id
  int user_id;
  String first_name;
  String last_name;
  @Temporal(TemporalType.DATE)
  Date birth_date;
  String email_address;
  String last_updated_by;
  @Temporal(TemporalType.TIMESTAMP)
  Date last_updated_date;
  String created_by;
  @Temporal(TemporalType.TIMESTAMP)
  Date created_date;
  String user_address_line_1;
  String user_address_line_2;
  String city;
  String state;
  String zip_code;

  public FinanceUser() {
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
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

  public Date getBirth_date() {
    return birth_date;
  }

  public void setBirth_date(Date birth_date) {
    this.birth_date = birth_date;
  }

  public String getEmail_address() {
    return email_address;
  }

  public void setEmail_address(String email_address) {
    this.email_address = email_address;
  }

  public String getLast_updated_by() {
    return last_updated_by;
  }

  public void setLast_updated_by(String last_updated_by) {
    this.last_updated_by = last_updated_by;
  }

  public Date getLast_updated_date() {
    return last_updated_date;
  }

  public void setLast_updated_date(Date last_updated_date) {
    this.last_updated_date = last_updated_date;
  }

  public String getCreated_by() {
    return created_by;
  }

  public void setCreated_by(String created_by) {
    this.created_by = created_by;
  }

  public Date getCreated_date() {
    return created_date;
  }

  public void setCreated_date(Date created_date) {
    this.created_date = created_date;
  }

  public String getUser_address_line_1() {
    return user_address_line_1;
  }

  public void setUser_address_line_1(String user_address_line_1) {
    this.user_address_line_1 = user_address_line_1;
  }

  public String getUser_address_line_2() {
    return user_address_line_2;
  }

  public void setUser_address_line_2(String user_address_line_2) {
    this.user_address_line_2 = user_address_line_2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip_code() {
    return zip_code;
  }

  public void setZip_code(String zip_code) {
    this.zip_code = zip_code;
  }

  @Override
  public String toString() {
    return "FinanceUser{" +
      "user_id=" + user_id +
      ", first_name='" + first_name + '\'' +
      ", last_name='" + last_name + '\'' +
      ", birth_date=" + birth_date +
      ", email_address='" + email_address + '\'' +
      ", last_updated_by='" + last_updated_by + '\'' +
      ", last_updated_date=" + last_updated_date +
      ", created_by='" + created_by + '\'' +
      ", created_date=" + created_date +
      ", user_address_line_1='" + user_address_line_1 + '\'' +
      ", user_address_line_2='" + user_address_line_2 + '\'' +
      ", city='" + city + '\'' +
      ", state='" + state + '\'' +
      ", zip_code='" + zip_code + '\'' +
      '}';
  }

//  <-- JSON -->

/*  {
    "user_id": 1,
    "first_name": "Said",
    "last_name": "Bandito",
    "birth_date": "Computer Science",
    "email_address": "Said",
    "last_updated_by": "Computer Science",
    "last_updated_date": "Computer Science",
    "created_by": "Computer Science",
    "created_date": "Said",
    "user_address_line_1": "Said",
    "user_address_line_2": "Computer Science",
    "city": "Said",
    "state": "Computer Science",
    "zip_code": "Said"
  }*/
}
