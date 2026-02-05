package CPSF.com.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "guest")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Guest extends DbObject  {

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Email
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
        String n = getFirstname() == null || getFirstname().isEmpty()  ? "" :  getFirstname();
        String l = getLastname() == null || getLastname().isEmpty()  ? "" :  getLastname();
        return   n + " " + l;
    }

}
