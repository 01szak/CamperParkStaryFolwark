package CPSF.com.demo.util;

import CPSF.com.demo.DTO.*;
import CPSF.com.demo.entity.*;


import java.time.format.DateTimeFormatter;

public class DtoMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

//    public static Record mapToDto(DbObject dbObject) {
//        if (dbObject instanceof Guest g) {
//            return getGuestDTO(g);
//        } else if (dbObject instanceof Reservation r) {
//            return getReservationDto(r);
//        } else if (dbObject instanceof CamperPlace c) {
//            return getCamperPlaceDto(c);
//        } else if (dbObject instanceof User u) {
//            return getUserDTO(u);
//        } else  {
//            throw new WrongClassException("No mapper for this class!", dbObject.getClass(), dbObject.toString());
//        }
//    }

    public static UserDTO getUserDTO(User u) {
        return new UserDTO(
                u.getUsername(),
                u.getEmail(),
                u.getEmployeeRole().toString()
        );
    }

    public static CamperPlace_DTO getCamperPlaceDto(CamperPlace c) {
        return new CamperPlace_DTO(
                c.getId(),
                c.getIndex(),
                c.getCamperPlaceType().toString(),
                c.getPrice(),
                c.getReservations().stream().map(DtoMapper::getReservationDto).toList()
        );
    }

    public static Reservation_DTO getReservationDto(Reservation r) {
        return new Reservation_DTO(
                r.getId(),
                r.getCheckin().toString(),
                r.getCheckout().toString(),
                getGuestDTO(r.getGuest()),
                r.getCamperPlace().getIndex(),
                r.getPaid(),
                r.getReservationStatus().toString()
        );
    }

    public static GuestDTO getGuestDTO(Guest g) {
        return new GuestDTO(
                g.getId(),
                g.getFirstName(),
                g.getLastName(),
                g.getEmail(),
                g.getPhoneNumber(),
                g.getCarRegistration()
        );
    }

    public static ReservationMetadataDTO toReservationMetadataDTO(ReservationReservedCheckinCheckoutDTO reservationMetadata) {
        return ReservationMetadataDTO.builder()
                .reserved(reservationMetadata.getReserved())
                .checkin(reservationMetadata.getCheckin())
                .checkout(reservationMetadata.getCheckout())
                .build();
    }

}