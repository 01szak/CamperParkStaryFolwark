package CPSF.com.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "camper_place")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CamperPlace extends DbObject {

    public enum CamperPlaceType {
        STANDARD,
        VIP,
        PLUS,
    }

    @Column(name = "number")
    @NotNull
    private String index;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private CamperPlaceType camperPlaceType;

    @Positive
    @NotNull
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
