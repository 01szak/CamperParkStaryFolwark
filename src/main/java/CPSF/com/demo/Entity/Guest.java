package CPSF.com.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String LastName;

    private String phoneNumber;
    private String idNumber;
    private String emailAddress;
    private String carRegistration;
    private int occupiedPlace;

    public Guest() {
    }

    public Guest(String firstName, String lastName, String phoneNumber, String idNumber, String emailAddress, String carRegistration, int occupiedPlace) {
        this.firstName = firstName;
        LastName = lastName;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.emailAddress = emailAddress;
        this.carRegistration = carRegistration;
        this.occupiedPlace = occupiedPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOccupiedPlace() {
        return occupiedPlace;
    }

    public void setOccupiedPlace(int occupiedPlace) {
        this.occupiedPlace = occupiedPlace;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCarRegistration() {
        return carRegistration;
    }

    public void setCarRegistration(String carRegistration) {
        this.carRegistration = carRegistration;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", carRegistration='" + carRegistration + '\'' +
                ", occupiedPlace=" + occupiedPlace +
                '}';
    }
}
