package CPSF.com.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Camper_place")
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
    @Column(name = "is_occupied")
    private int isOccupied;
    @OneToOne(mappedBy = "camperPlace")
    private Reservation reservation;
}
