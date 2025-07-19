package CPSF.com.demo.entity;

import CPSF.com.demo.enums.Type;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "camper_places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CamperPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    @NonNull
    private String index;

    @Column(name = "is_Occupied")
    @NonNull
    private Boolean isOccupied;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NonNull
    private Type type;

    @Column(name = "price")
    @NonNull
    private double price;

    @OneToMany(mappedBy = "camperPlace", fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JsonManagedReference("camperPlaceIndex-reservations")
    private List<Reservation> reservations;

    @Override
    public String toString() {
        return String.format("CamperPlace(id: %s; index: %s; type: %s; price: %s)", id, index, type, price);
    }

}
