package com.springstart.Model.Entity.DatabaseEntity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "finances_user")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "FinanceUser.findAll", query = "SELECT fu FROM FinanceUser fu")
})
public class FinanceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
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

    @Embedded
    @AttributeOverrides({
      @AttributeOverride(name="addressLine1", column = @Column(name="USER_ADDRESS_LINE_1")),
      @AttributeOverride(name = "addressLine2", column = @Column(name="USER_ADDRESS_LINE_2")),
      @AttributeOverride(name = "city", column = @Column(name = "CITY"))})
    Address address;

    @ElementCollection
    @CollectionTable(name="USER_ADDRESS", joinColumns=@JoinColumn(name="USER_ID"))
    @AttributeOverrides({@AttributeOverride(name="addressLine1", column=@Column(name="USER_ADDRESS_LINE_1")),
      @AttributeOverride(name="addressLine2", column=@Column(name="USER_ADDRESS_LINE_2"))})
    private List<Address> loverAddresses = new ArrayList<Address>();


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PSPACE_ID")
    ParkingPlace parking_space;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PROJECT_has_finances_user",
            joinColumns = @JoinColumn(name = "finances_user_USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROJECT_ID"))
    private Collection<Project> projects;


    public FinanceUser() {
        address = new Address();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Access(AccessType.PROPERTY)
    public String getFirst_name() {
        //System.out.println("FIRST NAME GET");
        return first_name;
    }

    public void setFirst_name(String first_name) {
        // System.out.println("FIRST NAME SET");
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setParking_space(ParkingPlace parking_space) {
        this.parking_space = parking_space;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    public List<Address> getLoverAddresses() {
        return loverAddresses;
    }

    public void setLoverAddresses(List<Address> loverAddresses) {
        this.loverAddresses = loverAddresses;
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
      ", address=" + address +
      ", loverAddresses=" + loverAddresses +
      ", parking_space=" + parking_space +
      ", projects=" + projects +
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
