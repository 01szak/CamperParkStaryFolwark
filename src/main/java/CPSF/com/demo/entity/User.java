package CPSF.com.demo.entity;

import CPSF.com.demo.enums.EmployeeRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends DbObject  {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "car_registration")
    private String carRegistration;


    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = {
           CascadeType.DETACH,
           CascadeType.MERGE,
           CascadeType.PERSIST,
           CascadeType.REFRESH,
            CascadeType.REMOVE
   })
    @JsonManagedReference("user-reservations")
    private List<Reservation> reservations;

    @Override
    public String toString() {
        String n = getFirstName() == null || getFirstName().isEmpty()  ? "" :  getFirstName();
        String l = getLastName() == null || getLastName().isEmpty()  ? "" :  getLastName();
        return   n + " " + l;
    }


}
