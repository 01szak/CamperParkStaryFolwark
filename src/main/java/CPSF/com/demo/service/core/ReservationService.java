package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.Reservation_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.service.util.ReservationCalculator;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import CPSF.com.demo.service.core.StatisticsService.StatisticsModel;

import java.time.LocalDate;
import java.util.*;

import static CPSF.com.demo.exception.ClientInputException.checkClientInput;
import static CPSF.com.demo.model.entity.Reservation.ReservationStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class ReservationService extends CRUDServiceImpl<Reservation> {

    private final ReservationRepository reservationRepository;
    private final GuestService guestService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationCalculator calculator;


    public void create(Reservation_DTO reservationDto) {
        var camperPlace = camperPlaceService.findById(reservationDto.camperPlace().id());
        var checkin = reservationDto.checkin();
        var checkout = reservationDto.checkout();

        validateDates(checkout, checkin, camperPlace);

        Guest guest;

        if (reservationDto.guest().id() == null) {
            guest = guestService.create(reservationDto.guest());
        } else {
            guest = guestService.update(reservationDto.guest());
        }

        var r = Reservation.builder()
                .checkin(checkin)
                .checkout(checkout)
                .camperPlace(camperPlace)
                .guest(guest)
                .price(calculator.calculateFinalReservationCost(checkin, checkout, camperPlace.getPrice()))
                .paid(reservationDto.paid());

        if (isActive(checkin, checkout)) {
            r.reservationStatus(ACTIVE);
        }

        super.create(r.build());
    }

    public void update(Reservation_DTO reservationDto) {
        var camperPlace = camperPlaceService.findById(reservationDto.camperPlace().id());
        var checkin = reservationDto.checkin();
        var checkout = reservationDto.checkout();

        var r = findById(reservationDto.id());

        var datesOrCpChanged = !r.getCheckin().equals(checkin)
                ||  !r.getCheckout().equals(checkout)
                || !r.getCamperPlace().getIndex().equals(reservationDto.camperPlace());

    //it is checked because of the constraints and reservations overlapping
        if (datesOrCpChanged) {
            validateDates(checkout, checkin, camperPlace, r.getId());
            r.setCheckin(checkin);
            r.setCheckout(checkout);
            r.setCamperPlace(camperPlace);
            r.setPrice(calculator.calculateFinalReservationCost(checkin, checkout, camperPlace.getPrice()));
            if (isActive(checkin, checkout)) {
                r.setReservationStatus(ACTIVE);
            }
        }

        var guest = guestService.update(reservationDto.guest());

        r.setGuest(guest);
        r.setPaid(reservationDto.paid());

        super.update(r);
    }


    public List<StatisticsModel.Revenue> countRevenueOfAllCamperPlaces(boolean isPaid, int month, int year) {
        return reservationRepository.countRevenueOfAllCamperPlaces(isPaid, month, year);
    }

    private boolean isActive(LocalDate checkin, LocalDate checkout) {
        LocalDate currentDate = LocalDate.now();
        return checkin.isBefore(currentDate.plusDays(1)) && checkout.isAfter(currentDate.minusDays(1));
    }

    private void validateDates(LocalDate checkout, LocalDate checkin, CamperPlace camperPlace, Integer reservationId) {
        checkClientInput(checkout.isBefore(checkin), "Data wyjazdu nie może być przed datą wjazdu");
        checkClientInput(camperPlaceService.isOccupied(camperPlace, checkin, checkout, reservationId),
                "Parcela jest już zajęta!");
    }

    private void validateDates(LocalDate checkout, LocalDate checkin, CamperPlace camperPlace) {
        validateDates(checkout, checkin, camperPlace, null);
    }

    @Override
    protected CRUDRepository<Reservation> getRepository() {
        return reservationRepository;
    }

}

