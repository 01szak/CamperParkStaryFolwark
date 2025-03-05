package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.User;

import java.time.LocalDate;
public record ReservationRequest(LocalDate checkin, LocalDate checkout, User user, CamperPlace camperPlace,Boolean paid) {
}
