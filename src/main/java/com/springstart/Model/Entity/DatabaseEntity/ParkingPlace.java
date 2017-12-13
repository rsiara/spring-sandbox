package com.springstart.Model.Entity.DatabaseEntity;


import javax.persistence.*;

@Entity
@Table(name = "PARKING_PLACE")
@Access(AccessType.FIELD)
public class ParkingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(name = "PLACE_NUMBER")
    String placeNumber;

    @OneToOne(mappedBy = "parking_space")
    FinanceUser financeUser;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public FinanceUser getFinanceUser() {
        return financeUser;
    }

    public void setFinanceUser(FinanceUser financeUser) {
        this.financeUser = financeUser;
    }

    @Override
    public String toString() {
        return "ParkingPlace{" +
                "id=" + id +
                ", placeNumber='" + placeNumber + '\'' +
                '}';
    }
}
