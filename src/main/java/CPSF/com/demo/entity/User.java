package CPSF.com.demo.entity;

import CPSF.com.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

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

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "password_hash")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade = {
           CascadeType.DETACH,
           CascadeType.MERGE,
           CascadeType.PERSIST,
           CascadeType.REFRESH
   })
    private List<Reservation> reservations;

    public User(String firstName, String lastName, String email, String phoneNumber, String carRegistration, String country, String city, String streetAddress, String password, Role role, List<Reservation> reservations) {
        this.firstName = firstName;
        lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.carRegistration = carRegistration;
        this.country = country;
        this.city = city;
        this.streetAddress = streetAddress;
        this.password = password;
        this.role = role;
        this.reservations = reservations;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
