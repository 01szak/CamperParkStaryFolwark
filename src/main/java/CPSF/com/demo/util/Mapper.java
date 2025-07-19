package CPSF.com.demo.util;

import CPSF.com.demo.DTO.*;
import CPSF.com.demo.entity.CamperPlace;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;

public class Mapper {

    private Mapper() {

    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .carRegistration(user.getCarRegistration())
                .country(user.getCountry())
                .city(user.getCity())
                .streetAddress(user.getStreetAddress())
                .build();

    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .checkin(reservation.getCheckin())
                .checkout(reservation.getCheckout())
                .camperPlaceIndex(reservation.getCamperPlace().getIndex())
                .user(toUserDTO(reservation.getUser()))
                .reservationStatus(String.valueOf(reservation.getReservationStatus()))
                .paid(reservation.getPaid())
                .build();
    }

    public static CamperPlaceDTO toCamperPlaceDTO(CamperPlace camperPlace) {
        return CamperPlaceDTO.builder()
                .id(camperPlace.getId())
                .index(camperPlace.getIndex())
                .price(camperPlace.getPrice())
                .isOccupied(camperPlace.getIsOccupied())
                .type(camperPlace.getType())
                .reservations(camperPlace.getReservations().stream().map(r -> toReservationDTO(r)).toList())
                .build();
    }

    public static ReservationMetadataDTO toReservationMetadataDTO(ReservationReservedCheckinCheckoutDTO reservationMetadata) {
        return ReservationMetadataDTO.builder()
                .reserved(reservationMetadata.getReserved())
                .checkin(reservationMetadata.getCheckin())
                .checkout(reservationMetadata.getCheckout())
                .build();
    }

}