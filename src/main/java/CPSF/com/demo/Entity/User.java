package CPSF.com.demo.Entity;

import CPSF.com.demo.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String LastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "car_registration")
    private String carRegistration;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street_address")
    private String streetAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = {
           CascadeType.DETACH,
           CascadeType.MERGE,
           CascadeType.PERSIST,
           CascadeType.REFRESH
   })
    private List<Reservation> reservations;


}
