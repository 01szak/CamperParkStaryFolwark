package CPSF.com.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @JoinColumn(name = "camperPlace_id")
    private CamperPlace camperPlace;

    @ManyToOne
    @JoinColumn(name =  "user_id")
    private User user;


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
    private boolean isCheckoutAfterCheckin() {
        if (checkin == null || checkout == null) {
            return true;
        }
        return checkout.isAfter(checkin);
    }
}
