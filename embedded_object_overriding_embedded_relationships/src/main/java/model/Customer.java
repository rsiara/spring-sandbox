package model;

import javax.persistence.*;


@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Embedded
    @AttributeOverride(name = "residence.zip", column = @Column(name = "ZIP"))
    @AssociationOverrides({
            @AssociationOverride(name = "primaryPhone", joinColumns = @JoinColumn(name = "EMERG_PHONE")),
            @AssociationOverride(name = "phones", joinTable = @JoinTable(name = "CUST_PHONE"))})
    private ContactInfo contactInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String toString() {
        return "Customer id: " + getId() + " contactInfo: " + getContactInfo();

    }

}