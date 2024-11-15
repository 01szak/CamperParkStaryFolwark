package CPSF.com.demo.Entity;

import CPSF.com.demo.Enum.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "camperPlace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CamperPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Max(19)
    private int id;

    @NotNull
    @Column(name = "is_Occupied")
    private int isOccupied;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "price")
    private double price;

    @OneToMany(mappedBy = "camperPlace",fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private List<Reservation> reservations;


}
