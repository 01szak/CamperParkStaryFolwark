package CPSF.com.demo.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private int id;
    private LocalDate checkin;
    private LocalDate checkout;
    private String reservationStatus;
    private String camperPlaceIndex;
    private UserDTO user;
    private boolean paid;
}
