package CPSF.com.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statistics")
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "camper_place_id")
    private CamperPlace camperPlace;
    @Column(name = "revenue")
    private double revenue;
    @Column(name = "reservation_count")
    private long reservationCount;
    @Column(name = "month")
    private int month;
    @Column(name = "year")
    private int year;

}
