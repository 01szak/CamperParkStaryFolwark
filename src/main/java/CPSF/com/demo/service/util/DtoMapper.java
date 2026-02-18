package CPSF.com.demo.service.util;

import CPSF.com.demo.model.dto.*;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.User;
import jakarta.validation.Valid;


import java.time.format.DateTimeFormatter;

public class DtoMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static UserDTO getUserDTO(User u) {
        return new UserDTO(
                u.getUsername(),
                u.getEmail(),
                u.getUserRole().toString()
        );
    }

    public static CamperPlace_DTO getCamperPlaceDto(CamperPlace c) {
        return new CamperPlace_DTO(
                c.getId(),
                c.getIndex(),
                getCamperPlaceTypeDTO(c.getCamperPlaceType()),
                c.getPrice(),
                c.getReservations().stream().map(DtoMapper::getReservationDto).toList()
        );
    }

    public static Reservation_DTO getReservationDto(Reservation r) {
        return new Reservation_DTO(
                r.getId(),
                formatter.format(r.getCheckin()),
                formatter.format(r.getCheckout()),
                getGuestDTO(r.getGuest()),
                r.getCamperPlace().getIndex(),
                r.getPaid(),
                r.getReservationStatus().toString()
        );
    }

    public static GuestDTO getGuestDTO(Guest g) {
        return new GuestDTO(
                g.getId(),
                g.getFirstname(),
                g.getLastname(),
                g.getEmail(),
                g.getPhoneNumber(),
                g.getCarRegistration()
        );
    }

    public static CamperPlaceTypeDTO getCamperPlaceTypeDTO(CamperPlaceType cpt) {
        return new CamperPlaceTypeDTO(
            cpt.getId(),
            cpt.getTypeName(),
            cpt.getPrice()
        );
    }

    public static ReservationMetadataDTO toReservationMetadataDTO(ReservationReservedCheckinCheckoutDTO reservationMetadata) {
        return ReservationMetadataDTO.builder()
                .reserved(reservationMetadata.getReserved())
                .checkin(reservationMetadata.getCheckin())
                .checkout(reservationMetadata.getCheckout())
                .build();
    }

    public static Guest getGuest(@Valid GuestDTO guestDTO) {
        return Guest.builder()
                .id(guestDTO.id())
                .firstname(guestDTO.firstname())
                .lastname(guestDTO.lastname())
                .email(guestDTO.email())
                .carRegistration(guestDTO.carRegistration())
                .phoneNumber(guestDTO.phoneNumber())
                .build();
    }
}