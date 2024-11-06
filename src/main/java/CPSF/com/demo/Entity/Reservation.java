package CPSF.com.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Table(name = "Reservation")
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
    @OneToOne
    @JoinColumn(name = "camperPlace_id")
    private CamperPlace camperPlace;

    public String convertDateEnterToString(){
        return dateEnter.toString();
    }
    public String convertDateCheckoutToString(){
       return dateCheckout.toString();
    }
}
