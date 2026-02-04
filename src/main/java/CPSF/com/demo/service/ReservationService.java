package CPSF.com.demo.service;

import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.DTO.Reservation_DTO;
import CPSF.com.demo.util.DtoMapper;
import CPSF.com.demo.util.ReservationCalculator;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static CPSF.com.demo.enums.ReservationStatus.ACTIVE;
import static CPSF.com.demo.exception.ClientInputExceptionUtil.ensure;

@Service
@NoArgsConstructor
public class ReservationService extends CRUDServiceImpl<Reservation> {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GuestService guestService;

    @Autowired
    private CamperPlaceService camperPlaceService;

    @Autowired
    private ReservationMetadataMapper reservationMetadataMapper;

    @Autowired
    private ReservationCalculator calculator;

//    TODO refactor
    @Transactional
    public void create(String checkin, String checkout, String camperPlaceIndex, Guest guest) {
        CamperPlace camperPlace = camperPlaceService.findByIndex(camperPlaceIndex);

        ensureDataIsCorrect(checkin, checkout, camperPlace, guest);

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);

        if (guest.getId() <= 0) {
            guestService.create(guest);
        } else {
          guest.setUpdatedAt(new Date());
          guestService.update(guest);
        }

        var toCreate = Reservation.builder()
                .checkin(checkinDate)
                .checkout(checkoutDate)
                .camperPlace(camperPlace)
                .guest(guest)
                .price(calculator.calculateFinalReservationCost(checkinDate, checkoutDate, camperPlace.getPrice()))
                .paid(false);

        if (isActive(checkinDate, checkoutDate)) {
            toCreate.reservationStatus(ACTIVE);
        }

        super.create(toCreate.build());
    }
//TODO refactor
    @Transactional
    public void update(int id, Reservation_DTO reservationDto) {

        CamperPlace camperPlace = camperPlaceService.findByIndex(reservationDto.camperPlaceIndex());
        LocalDate checkin = reservationDto.checkin() == null ? null : LocalDate.parse(reservationDto.checkin());
        LocalDate checkout = reservationDto.checkout() == null ? null : LocalDate.parse(reservationDto.checkout());

        if (camperPlace == null) {
           throw new RuntimeException("Coś poszło nie tak");
        }

        ensure (camperPlaceService.checkIsCamperPlaceOccupied(
                camperPlace,
                checkin,
                checkout,
                id),
        "Parcela jest już zajęta!");

        ensure (checkin != null
                    && checkout != null
                    && checkout.isBefore(checkin),
            "Data wyjazdu nie może być przed datą wjazdu");

        Reservation reservationToUpdate = findById(id);
        Guest guest = reservationToUpdate.getGuest();

        if (!guest.equals(reservationDto.guest())) {
            guestService.update(guestService.findById(reservationDto.guest().id()));
        }

        reservationToUpdate.setUpdatedAt(new Date());
        Optional.ofNullable(checkin).ifPresent(reservationToUpdate::setCheckin);
        Optional.ofNullable(checkout).ifPresent(reservationToUpdate::setCheckout);
        Optional.ofNullable(camperPlace).ifPresent(reservationToUpdate::setCamperPlace);
        Optional.ofNullable(reservationDto.paid()).ifPresent(reservationToUpdate::setPaid);

        super.update(reservationToUpdate);
    }

    public Page<Reservation_DTO> findDTOBy(Pageable pageable, String fieldName, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (fieldName.equals("camperPlaceIndex")) {
            return reservationRepository.findAllByCamperPlace_Index(pageable, value).map(DtoMapper::getReservationDto);
        } else if (fieldName.equals("stringUser")) {
            return reservationRepository.findAllByUserFullName(pageable, value).map(DtoMapper::getReservationDto);
        }
        return super.findBy(pageable, fieldName, value).map(DtoMapper::getReservationDto);
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

    private void ensureDataIsCorrect(String checkin, String checkout, CamperPlace camperPlace, Guest guest) {

        ensure(checkin.isEmpty(), "Podaj datę wjazdu");

        ensure(checkout.isEmpty(), "Podaj datę wyjazdu");

        ensure(LocalDate.parse(checkin).isAfter(LocalDate.parse(checkout))
                        || LocalDate.parse(checkin).isEqual(LocalDate.parse(checkout)),
                "Wprowadzone daty są nieprawidłowe");

        ensure(guest == null, "Pole gościa nie może być puste");

        ensure(setToStringIfNull(guest.getLastName()).isEmpty()
                        && setToStringIfNull(guest.getFirstName()).isEmpty()
                        && setToStringIfNull(guest.getPhoneNumber()).isEmpty(),
                "Podaj dane gościa");

        ensure(camperPlace == null, "Wybierz nr. parceli");

        ensure(camperPlaceService.checkIsCamperPlaceOccupied(
                        camperPlace, LocalDate.parse(checkin), LocalDate.parse(checkout), 0),
                "Parcela jest już zajęta!");
    }

    private String setToStringIfNull(String s) {
		return s == null ? "" : s;
	}

    private boolean isActive(LocalDate checkin, LocalDate checkout) {
        LocalDate currentDate = LocalDate.now();
        return checkin.isBefore(currentDate.plusDays(1)) && checkout.isAfter(currentDate.minusDays(1));
    }
}

