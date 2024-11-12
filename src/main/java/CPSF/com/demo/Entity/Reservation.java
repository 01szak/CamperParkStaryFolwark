package CPSF.com.demo.Entity;

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
    @Column(name = "date_enter")
    private LocalDate dateEnter;
    @Column(name = "date_checkout")
    private LocalDate dateCheckout;
    @ManyToOne(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "camper_place_id")
    private CamperPlace camperPlace;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public int daysDifference(){
        int daysDifference = dateEnter.until(dateCheckout).getDays();
        return daysDifference;
    }

    public double calculateFinalPrice(){
        double price = camperPlace.getPrice();
        double finalPrice = price * daysDifference();
        return finalPrice;
    }
}
