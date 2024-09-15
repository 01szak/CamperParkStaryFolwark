package CPSF.com.demo.Entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "guest")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String LastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "car_registration")
    private String carRegistration;
    @Column(name = "occupied_place")
    private int occupiedPlace;

    public Guest() {
    }

    public Guest(String firstName, String lastName, String phoneNumber, String emailAddress, String carRegistration, int occupiedPlace) {
        this.firstName = firstName;
        LastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
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



    public String getEmailAddress() {
        return email;
    }

    public void setEmailAddress(String emailAddress) {
        this.email = emailAddress;
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

                ", emailAddress='" + email + '\'' +
                ", carRegistration='" + carRegistration + '\'' +
                ", occupiedPlace=" + occupiedPlace +
                '}';
    }
}
