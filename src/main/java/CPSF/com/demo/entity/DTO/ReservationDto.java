package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDto {
    private int id;
    private LocalDate checkin;
    private LocalDate checkout;
    private String reservationStatus;
    private int camperPlaceNumber;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    public ReservationDto(Reservation reservation){
        this.id = reservation.getId();
        this.checkin = reservation.getCheckin();
        this.checkout = reservation.getCheckout();
        this.camperPlaceNumber = reservation.getCamperPlace().getNumber();
        this.userFirstName= reservation.getUser().getFirstName();
        this.userLastName= reservation.getUser().getLastName();
        this.userEmail = reservation.getUser().getLastName();
        this.reservationStatus = String.valueOf(reservation.getReservationStatus());
    }
}
