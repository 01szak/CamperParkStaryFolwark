package CPSF.com.demo.entity;

import CPSF.com.demo.enums.CamperPlaceType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "camper_place")
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

    @Column(name = "is_occupied")
    @NonNull
    private Boolean isOccupied;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NonNull
    private CamperPlaceType camperPlaceType;

    @Column(name = "price")
    private BigDecimal price;

    @OneToMany(mappedBy = "camperPlace", fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JsonManagedReference("camperPlaceIndex-reservations")
    private List<Reservation> reservations;

}
