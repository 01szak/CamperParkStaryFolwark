package CPSF.com.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Date;
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
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkin;

    @Column(name = "checkout")
    @JsonFormat(pattern = "dd-MM-yyyy")
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

    public double calculateFinalPrice(){
        double price = camperPlace.getPrice();
        double finalPrice = price * daysDifference();
        return finalPrice;
    }
}
