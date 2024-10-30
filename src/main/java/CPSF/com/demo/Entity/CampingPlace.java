package CPSF.com.demo.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "camping_place")
public class CampingPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "place_number" )
    private int placeNumber;
    @Column(name = "is_occupied")
    private boolean isOccupied;
    @Nullable
    @Column(name = "check_out_date")
    private Date checkOutDate;
    @OneToOne(mappedBy = "campingPlace",
            cascade = {
            CascadeType.PERSIST,
            CascadeType.REFRESH,
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private Guest guest;



}
