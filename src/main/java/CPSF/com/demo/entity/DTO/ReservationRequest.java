package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDate;
public record ReservationRequest(LocalDate checkin, LocalDate checkout, User user, CamperPlace camperPlace) {
}
