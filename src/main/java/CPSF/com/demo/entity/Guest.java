package CPSF.com.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "guest")
@Getter
@Setter
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Guest extends DbObject  {

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "car_registration")
    private String carRegistration;


    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY,cascade = {
           CascadeType.DETACH,
           CascadeType.MERGE,
           CascadeType.PERSIST,
           CascadeType.REFRESH,
            CascadeType.REMOVE
   })
    @JsonManagedReference("guest-reservations")
    private List<Reservation> reservations;

    @Override
    public String toString() {
        String n = getFirstName() == null || getFirstName().isEmpty()  ? "" :  getFirstName();
        String l = getLastName() == null || getLastName().isEmpty()  ? "" :  getLastName();
        return   n + " " + l;
    }


}
