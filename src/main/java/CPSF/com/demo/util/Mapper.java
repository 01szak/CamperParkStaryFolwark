package CPSF.com.demo.util;

import CPSF.com.demo.DTO.*;
import CPSF.com.demo.entity.CamperPlace;

import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import org.hibernate.WrongClassException;

import java.time.format.DateTimeFormatter;

public class Mapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private Mapper() {
    }

    public static DTO toDTO(DbObject dbObject) {
        if (dbObject instanceof User) {
            return toUserDTO((User) dbObject);
        } else if (dbObject instanceof Reservation) {
            return toReservationDTO((Reservation) dbObject);
        } else if (dbObject instanceof CamperPlace) {
            return toCamperPlaceDTO((CamperPlace) dbObject);
        } else  {
            throw new WrongClassException("No mapper for this class!", dbObject.getClass(), dbObject.toString());
        }
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
                .checkin(reservation.getCheckin().format(formatter))
                .checkout(reservation.getCheckout().format(formatter))
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