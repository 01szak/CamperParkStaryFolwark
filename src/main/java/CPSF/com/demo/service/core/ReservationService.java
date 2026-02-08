package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.ReservationMetadataDTO;
import CPSF.com.demo.model.dto.Reservation_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.service.util.DtoMapper;
import CPSF.com.demo.service.util.ReservationCalculator;
import CPSF.com.demo.service.util.ReservationMetadataMapper;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static CPSF.com.demo.exception.ClientInputException.checkClientInput;
import static CPSF.com.demo.model.entity.Reservation.ReservationStatus.ACTIVE;

@Service
@NoArgsConstructor
public class ReservationService extends CRUDServiceImpl<Reservation> {

    private static final String BY_CP_INDEX = "camperPlaceIndex";
    private static final String BY_GUEST_FULL_NAME = "stringUser";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GuestService guestService;

    @Autowired
    private CamperPlaceService camperPlaceService;

    @Autowired
    private ReservationMetadataMapper reservationMetadataMapper;

    private static final ReservationCalculator calculator = new ReservationCalculator();

    public void create(Reservation_DTO reservationDto) {
        var camperPlace = camperPlaceService.findBy("index", reservationDto.camperPlaceIndex()).toList().get(0);
        var checkin = LocalDate.parse(reservationDto.checkin());
        var checkout = LocalDate.parse(reservationDto.checkout());

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
                .paid(false);

        if (isActive(checkin, checkout)) {
            r.reservationStatus(ACTIVE);
        }

        super.create(r.build());
    }

    public void update(Reservation_DTO reservationDto) {
        var camperPlace = camperPlaceService.findBy("index", reservationDto.camperPlaceIndex()).toList().get(0);
        var checkin = LocalDate.parse(reservationDto.checkin());
        var checkout = LocalDate.parse(reservationDto.checkout());

        var r = findById(reservationDto.id());

        var datesOrCpChanged = !r.getCheckin().equals(checkin)
                ||  !r.getCheckout().equals(checkout)
                || !r.getCamperPlace().getIndex().equals(reservationDto.camperPlaceIndex());

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

    @Override
    public Page<Reservation> findBy(Pageable pageable, String fieldName, String value) {
        switch (fieldName) {
            case BY_CP_INDEX -> {
                return reservationRepository.findAllByCamperPlace_Index(pageable, value);
            }
            case BY_GUEST_FULL_NAME -> {
                return reservationRepository.findAllByUserFullName(pageable, value);
            }
            default -> {
                return super.findBy(pageable, fieldName, value);
            }
        }
    }

    public List<Reservation> findByCamperPlaceIdIfPaid(int id) {
        return reservationRepository.findByCamperPlace_IdIfPaid(id);
    }

    public List<Reservation> findByYearAndCamperPlaceIdIfPaid(int year, int id) {
        return reservationRepository.findAllByYearAndCamperPlaceIdIfPaid(year, id);
    }

    public  List<Reservation> findByMonthYearAndCamperPlaceIdIfPaid(int month, int year, int camperPlaceId) {
        return reservationRepository.findAllByMonthYearAndCamperPlaceIdIfPaid(month, year, camperPlaceId);
    }

    public List<Reservation> findByCamperPlaceIdIfStatusNotComing(int id) {
        return reservationRepository.findAllByCamperPlaceIdIfStatusNotComing(id);
    }

    public List<Reservation> findByYearAndCamperPlaceIdIfStatusNotComing(int year, int id) {
        return reservationRepository.findAllByYearAndCamperPlace_IdIfStatusNotComing(year, id);
    }

    public List<Reservation> findByMonthYearAndCamperPlaceIdIfStatusNotComing(int month, int year, int id) {
        return reservationRepository.findAllByMonthYearAndCamperPlace_IdIfStatusNotComing(month, year, id);
    }

    public Map<String, ReservationMetadataDTO> getReservationMetadataDTO() {
        return reservationMetadataMapper.getReservationMetaDataDTO();
    }

    private boolean isActive(LocalDate checkin, LocalDate checkout) {
        LocalDate currentDate = LocalDate.now();
        return checkin.isBefore(currentDate.plusDays(1)) && checkout.isAfter(currentDate.minusDays(1));
    }

    private void validateDates(LocalDate checkout, LocalDate checkin, CamperPlace camperPlace, Integer reservationId) {
        checkClientInput(checkout.isBefore(checkin), "Data wyjazdu nie może być przed datą wjazdu");
        checkClientInput(camperPlaceService.isCamperPlaceOccupied(camperPlace, checkin, checkout, reservationId),
                "Parcela jest już zajęta!");
    }

    private void validateDates(LocalDate checkout, LocalDate checkin, CamperPlace camperPlace) {
        validateDates(checkout, checkin, camperPlace, null);
    }

}

