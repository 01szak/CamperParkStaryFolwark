package CPSF.com.demo.entity;

import CPSF.com.demo.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "camper_places")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CamperPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "is_Occupied")
    @NotNull
    private Boolean isOccupied;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private Type type;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @OneToMany(mappedBy = "camperPlace",fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private List<Reservation> reservations;


}
