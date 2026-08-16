package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.service.core.StatisticsService.StatisticsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static CPSF.com.demo.exception.UserInputException.checkClientInput;
import static CPSF.com.demo.model.constant.ReservationStatus.ACTIVE;
import static CPSF.com.demo.model.constant.ReservationStatus.COMING;
import static CPSF.com.demo.model.constant.ReservationStatus.EXPIRED;

@Service
@RequiredArgsConstructor
public class ReservationService extends CRUDServiceImpl<Reservation> {

    private final ReservationRepository reservationRepository;
    private final GuestService guestService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationCalculatorService calculator;
    private final UserService userService;

    public Reservation create(ReservationDTO reservationDto) {
        final var camperPlace = camperPlaceService.findById(reservationDto.camperPlace().id());
        final var checkin = reservationDto.checkin();
        final var checkout = reservationDto.checkout();
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        Objects.requireNonNull(authentication, "Unauthorized call detected");

        final var creator = (User) userService.loadUserByUsername(authentication.getPrincipal().toString());

        validateDates(checkout, checkin, camperPlace);

        final var guest = Optional.ofNullable(reservationDto.guest().id()).isPresent()
                ? guestService.update(reservationDto.guest())
                : guestService.create(reservationDto.guest());

        final var status = Optional.ofNullable(reservationDto.reservationStatus())
                .orElse(getReservationStatus(checkin, checkout));

        final var r = Reservation.builder()
                .checkin(checkin)
                .checkout(checkout)
                .camperPlace(camperPlace)
                .guest(guest)
                .price(calculator.calculate(camperPlace.getPrice(), checkin.datesUntil(checkout).count())) //TODO it can be passed in request
                .paid(reservationDto.paid())
                .reservationStatus(status)
                .creator(creator);

       return super.create(r.build());
    }

    private ReservationStatus getReservationStatus(LocalDate checkin, LocalDate checkout) {
        if (isActive(checkin, checkout)) {
            return ACTIVE;
        } else if (isExpired(checkout)) {
            return EXPIRED;
        } else {
            return COMING;
        }
    }

    private boolean isExpired(LocalDate checkout) {
        return LocalDate.now().isAfter(checkout);
    }

    public void update(ReservationDTO reservationDto) {
        var camperPlace = camperPlaceService.findById(reservationDto.camperPlace().id());
        var checkin = reservationDto.checkin();
        var checkout = reservationDto.checkout();

        var r = findById(reservationDto.id());

        var datesOrCpChanged = !r.getCheckin().equals(checkin)
                || !r.getCheckout().equals(checkout)
                || !r.getCamperPlace().getIndex().equals(reservationDto.camperPlace().index());

    //it is checked because of the constraints and reservations overlapping
        if (datesOrCpChanged) {
            validateDates(checkout, checkin, camperPlace);
            r.setCheckin(checkin);
            r.setCheckout(checkout);
            r.setCamperPlace(camperPlace);
            r.setPrice(calculator.calculate(camperPlace.getPrice(), checkin.datesUntil(checkout).count()));
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

    private void validateDates(LocalDate checkout, LocalDate checkin, CamperPlace camperPlace) {
        final var occupiedDates = camperPlaceService.getOccupiedDates(camperPlace.getId());

        checkClientInput(checkout.isBefore(checkin), "Data wyjazdu nie może być przed datą wjazdu");
        checkClientInput(checkout.equals(checkin), "Czas trwania rezerwacji musi wynosić minimum 1 dobę");
        checkClientInput(occupiedDates.contains(checkin) || occupiedDates.contains(checkout), "Parcela jest już zajęta!");
    }

    @Override
    protected CRUDRepository<Reservation> getRepository() {
        return reservationRepository;
    }

}

