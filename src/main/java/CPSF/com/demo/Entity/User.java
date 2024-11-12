package CPSF.com.demo.Entity;

import io.micrometer.core.instrument.binder.netty4.NettyAllocatorMetrics;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
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
   @JoinColumn(name = "role_id")
   @ManyToOne(cascade = {
           CascadeType.PERSIST,
           CascadeType.MERGE,
           CascadeType.DETACH,
           CascadeType.REFRESH,
   })
    private Role role;
   @OneToOne(mappedBy = "user",fetch = FetchType.LAZY,cascade = {
           CascadeType.DETACH,
           CascadeType.MERGE,
           CascadeType.PERSIST,
           CascadeType.REFRESH
   })
   @JoinColumn(name = "id")
   private Reservation reservation;

   @OneToOne
   private CamperPlace camperPlace;

}
