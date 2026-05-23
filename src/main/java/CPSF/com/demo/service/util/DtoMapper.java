package CPSF.com.demo.service.util;

import CPSF.com.demo.model.dto.*;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.User;
import jakarta.validation.constraints.NotNull;

public class DtoMapper {

    public static UserDTO getUserDTO(User u) {
        return new UserDTO(
                u.getUsername(),
                u.getEmail(),
                u.getUserRole().toString()
        );
    }

    public static CamperPlace_DTO getCamperPlaceDto(@NotNull CamperPlace c) {
        return new CamperPlace_DTO(
                c.getId(),
                c.getIndex(),
                getCamperPlaceTypeDTO(c.getCamperPlaceType()),
                c.getPrice()
        );
    }

    public static Reservation_DTO getReservationDto(@NotNull Reservation r) {
        return new Reservation_DTO(
                r.getId(),
                r.getCheckin(),
                r.getCheckout(),
                getGuestDTO(r.getGuest()),
                getCamperPlaceDto(r.getCamperPlace()),
                r.getPaid(),
                r.getReservationStatus()
        );
    }

    public static GuestDTO getGuestDTO(@NotNull Guest g) {
        return new GuestDTO(
                g.getId(),
                g.getFirstname(),
                g.getLastname(),
                g.getEmail(),
                g.getPhoneNumber(),
                g.getCarRegistration()
        );
    }

    public static CamperPlaceTypeDTO getCamperPlaceTypeDTO(@NotNull CamperPlaceType cpt) {
        return new CamperPlaceTypeDTO(
            cpt.getId(),
            cpt.getTypeName(),
            cpt.getPrice()
        );
    }

    public static Guest getGuest(@NotNull GuestDTO guestDTO) {
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