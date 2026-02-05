package CPSF.com.demo.model.dto;

import lombok.*;

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
    private GuestDTO user;
    private boolean paid;
}
