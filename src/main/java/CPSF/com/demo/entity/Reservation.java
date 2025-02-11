package CPSF.com.demo.entity;

import CPSF.com.demo.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDate;

import static CPSF.com.demo.enums.ReservationStatus.*;

@Entity
@Builder
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "checkin")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-in date is required")
    private LocalDate checkin;

    @Column(name = "checkout")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-out date is required")
    private LocalDate checkout;

    @ManyToOne
    @JoinColumn(name = "camper_place_id")
    @Lazy
    @JsonBackReference("camperPlace-reservations")
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


    public int daysDifference() {
        return checkin.until(checkout).getDays();
    }

    public double calculateFinalPrice() {
        return camperPlace.getPrice() * daysDifference();
    }

    @AssertTrue(message = "Check-out date must be after check-in date")
    private boolean isCheckoutAfterCheckin() {
        return checkout.isAfter(checkin);
    }
}
