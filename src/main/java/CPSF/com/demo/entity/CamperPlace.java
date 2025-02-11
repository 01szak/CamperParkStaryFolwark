package CPSF.com.demo.entity;

import CPSF.com.demo.enums.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "camper_places")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CamperPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    @NonNull
    private int number;

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
            CascadeType.REFRESH
    })
    @JsonManagedReference("camperPlace-reservations")
    private List<Reservation> reservations;


}
