package CPSF.com.demo.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO extends DTO {
    private int id;
    private String checkin;
    private String checkout;
    private String reservationStatus;
    private String camperPlaceIndex;
    private UserDTO user;
    private boolean paid;
}
