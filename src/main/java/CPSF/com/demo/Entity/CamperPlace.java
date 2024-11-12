package CPSF.com.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "camper_place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CamperPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "price")
    private double price;
    @NotNull
    @Column(name = "is_occupied")
    private int isOccupied;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "camperPlace")
    private List<Reservation> reservation;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

}
