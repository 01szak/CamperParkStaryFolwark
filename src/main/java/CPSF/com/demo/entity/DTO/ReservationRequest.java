package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.User;

public record ReservationRequest(String checkin, String checkout, User user, String camperPlaceIndex, Boolean paid) {
}
