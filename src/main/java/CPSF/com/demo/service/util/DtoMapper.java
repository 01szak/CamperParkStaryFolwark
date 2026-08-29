package CPSF.com.demo.service.util;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.dto.camperPlaceDTO;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.dto.UserDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.User;
import jakarta.validation.constraints.NotNull;

public class DtoMapper {

    public static UserDTO getUserDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getUserRole().toString()
        );
    }

    public static camperPlaceDTO getCamperPlaceDto(@NotNull CamperPlace c) {
        return new camperPlaceDTO(
                c.getId(),
                c.getIndex(),
                getCamperPlaceTypeDTO(c.getCamperPlaceType()),
                c.getPrice()
        );
    }

    public static ReservationDTO getReservationDto(@NotNull Reservation r) {
        return new ReservationDTO(
                r.getId(),
                r.getCheckin(),
                r.getCheckout(),
                getGuestDTO(r.getGuest()),
                getCamperPlaceDto(r.getCamperPlace()),
                r.getPaid(),
                r.getReservationStatus(),
                r.getCreator() != null ? getUserDTO(r.getCreator()) : null
        );
    }

    public static GuestDTO getGuestDTO(@NotNull Guest g) {
        return new GuestDTO(
                g.getId(),
                g.getFirstname(),
                g.getLastname(),
                g.getEmail(),
                g.getPhoneNumber(),
                g.getCarRegistration(),
                g.getCountry()
        );
    }

    public static CamperPlaceTypeDTO getCamperPlaceTypeDTO(@NotNull CamperPlaceType cpt) {
        return new CamperPlaceTypeDTO(
            cpt.getId(),
            cpt.getTypeName(),
            cpt.getPrice()
        );
    }

}