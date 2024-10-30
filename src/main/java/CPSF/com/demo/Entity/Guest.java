package CPSF.com.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Repository;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guests")
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
    @Column(name = "occupied_place", insertable = false, updatable = false)
    private int occupiedPlace;
    @Column(name = "role")
    private String role = "guest";
    @OneToOne
    @JoinColumn(name = "occupied_place", referencedColumnName = "place_number")
    private CampingPlace campingPlace;



}
