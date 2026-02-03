package CPSF.com.demo.util;

import CPSF.com.demo.DTO.*;
import CPSF.com.demo.entity.*;

import CPSF.com.demo.enums.CamperPlaceType;
import org.hibernate.WrongClassException;

import java.time.format.DateTimeFormatter;

public class Mapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private Mapper() {
    }

    public static DTO toDTO(DbObject dbObject) {
        if (dbObject instanceof Guest) {
            return toUserDTO((Guest) dbObject);
        } else if (dbObject instanceof Reservation) {
            return toReservationDTO((Reservation) dbObject);
        } else if (dbObject instanceof CamperPlace) {
            return toCamperPlaceDTO((CamperPlace) dbObject);
        } else if (dbObject instanceof User) {
            return toEmployeeDTO((User) dbObject);
        } else  {
            throw new WrongClassException("No mapper for this class!", dbObject.getClass(), dbObject.toString());
        }
    }

    public static DTO toEmployeeDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getEmployeeRole().getAuthority())
                .build();
    }

    public static GuestDTO toUserDTO(Guest guest) {
        return GuestDTO.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .phoneNumber(guest.getPhoneNumber())
                .carRegistration(guest.getCarRegistration())
                .build();
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .checkin(reservation.getCheckin().format(formatter))
                .checkout(reservation.getCheckout().format(formatter))
                .camperPlaceIndex(reservation.getCamperPlace().getIndex())
                .user(toUserDTO(reservation.getGuest()))
                .reservationStatus(String.valueOf(reservation.getReservationStatus()))
                .paid(reservation.getPaid())
                .build();
    }

    public static CamperPlaceDTO toCamperPlaceDTO(CamperPlace camperPlace) {
        return CamperPlaceDTO.builder()
                .id(camperPlace.getId())
                .index(camperPlace.getIndex())
                .price(camperPlace.getPrice().doubleValue())
                .camperPlaceType(camperPlace.getCamperPlaceType())
                .reservations(camperPlace.getReservations().stream().map(r -> toReservationDTO(r)).toList())
                .build();
    }
    public static CamperPlace_DTO toCamperPlace_DTO(CamperPlace camperPlace) {
        return new CamperPlace_DTO(
                camperPlace.getId(),
                camperPlace.getIndex(),
                camperPlace.getCamperPlaceType().toString(),
                camperPlace.getPrice()
        );
    }

    public static CamperPlace toCamperPlace(CamperPlace_DTO camperPlace) {
        return CamperPlace.builder()
                .id(camperPlace.id())
                .camperPlaceType(CamperPlaceType.valueOf(camperPlace.type().toUpperCase()))
                .index(camperPlace.index())
                .price(camperPlace.price())
//                .isOccupied()
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