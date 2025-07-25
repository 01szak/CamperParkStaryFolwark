package CPSF.com.demo.entity;

import CPSF.com.demo.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;

import static CPSF.com.demo.enums.ReservationStatus.*;

@Entity
@Builder
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Reservation extends DbObject {

    @Column(name = "checkin")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-in date is required")
    private LocalDate checkin;

    @Column(name = "checkout")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-out date is required")
    private LocalDate checkout;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camper_place_id")
    @Lazy
    @JsonBackReference("camperPlaceIndex-reservations")
    private CamperPlace camperPlace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Lazy
    @JsonBackReference("user-reservations")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ReservationStatus reservationStatus = COMING;
    @Column(name = "is_paid")
    private Boolean paid = false;

}
