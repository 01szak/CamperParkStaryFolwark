package CPSF.com.demo.entity;

import CPSF.com.demo.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Table(name = "reservation")
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
    private CamperPlace camperPlace;

    @ManyToOne
    @JoinColumn(name =  "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name =  "status")
    private ReservationStatus reservationStatus;

    public Reservation(int id, LocalDate checkin, LocalDate checkout, CamperPlace camperPlace, User user) {
        this.id = id;
        this.checkin = checkin;
        this.checkout = checkout;
        this.camperPlace = camperPlace;
        this.user = user;

    }


    public int daysDifference(){
        int daysDifference = checkin.until(checkout).getDays();
        return daysDifference;
    }

    public BigDecimal calculateFinalPrice(){
        BigDecimal price = camperPlace.getPrice();
        BigDecimal finalPrice = price.multiply(BigDecimal.valueOf(daysDifference())) ;
        return finalPrice;
    }
    @AssertTrue(message = "Check-out date must be after check-in date")
    private boolean sCheckoutAfterCheckin() {
        return checkout.isAfter(checkin);
    }
}
