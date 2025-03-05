package CPSF.com.demo.entity.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private int id;
    private LocalDate checkin;
    private LocalDate checkout;
    private String reservationStatus;
    private int camperPlaceNumber;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private boolean paid;
}
