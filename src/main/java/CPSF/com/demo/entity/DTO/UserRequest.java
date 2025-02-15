package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.Reservation;

import java.util.List;

public record  UserRequest(String firstName, String lastName, String email, String phoneNumber, List<Reservation> reservations, String carRegistration,String country,String city, String streetAddress) {
}
