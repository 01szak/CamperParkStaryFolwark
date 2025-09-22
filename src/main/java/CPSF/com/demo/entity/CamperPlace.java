package CPSF.com.demo.entity;

import CPSF.com.demo.enums.Type;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Table(name = "camper_places")
@Getter
@Setter
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CamperPlace extends DbObject {

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
    private double price;

    @OneToMany(mappedBy = "camperPlace", fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JsonManagedReference("camperPlaceIndex-reservations")
    private List<Reservation> reservations;

}
