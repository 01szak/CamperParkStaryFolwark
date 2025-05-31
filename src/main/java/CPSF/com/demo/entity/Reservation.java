package CPSF.com.demo.entity;

import CPSF.com.demo.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
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

    @ManyToOne(fetch = FetchType.EAGER)
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
    @Column(name = "is_paid")
    private Boolean paid = false;

    public int daysDifference() {
        if (this.paid) {
            return checkin.until(checkout.plusDays(1)).getDays();
        }else {
            return 0;
        }
    }

    public double calculateFinalPrice() {
        double firstDay = 1;
        double lastDay = 1;
        double daysUntilDiscount = 3;
        double finalNumberOfDays = daysDifference() - firstDay * 0.5 - lastDay * 0.5;
        double discount = (daysDifference() - daysUntilDiscount) * 10;

        if (daysDifference() > 0 || (daysDifference() - daysUntilDiscount) >= 0 )  {
            return camperPlace.getPrice() * finalNumberOfDays - discount;
        }else {
            return camperPlace.getPrice() * finalNumberOfDays;
        }
    }

    @AssertTrue(message = "Checkout date must be after checkin date")
    public boolean isCheckoutAfterCheckin() {
        return checkout.isAfter(checkin);
    }
}
