package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.request.ReservationRequest;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.UserService;
import CPSF.com.demo.util.Mapper;
import CPSF.com.demo.util.ReservationCalculator;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static CPSF.com.demo.enums.ReservationStatus.ACTIVE;
import static exception.ClientInputExceptionUtil.ensure;

@Service
public class ReservationServiceImpl extends CRUDServiceImpl<Reservation, ReservationDTO> implements ReservationService {

    private final ReservationRepository repository;
    private final UserService userService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationMetadataMapper reservationMetadataMapper;
    private final ReservationCalculator calculator;

    public ReservationServiceImpl(
            ReservationRepository repository,
            CamperPlaceService camperPlaceService,
            UserService userService,
            ReservationMetadataMapper mapper, ReservationCalculator calculator
    ) {
        super(repository);
        this.repository = repository;
        this.camperPlaceService = camperPlaceService;
        this.userService = userService;
        this.reservationMetadataMapper = mapper;
        this.calculator = calculator;
    }


    @Transactional
    public void create(String checkin, String checkout, String camperPlaceIndex, User user) {
        CamperPlace camperPlace = camperPlaceService.findByIndex(camperPlaceIndex);

        ensureDataIsCorrect(checkin, checkout, camperPlace, user);

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);

        if (user.getId() <= 0) {
            userService.create(user);
        } else {
          user.setUpdatedAt(new Date());
          userService.update(user);
        }

        var toCreate = Reservation.builder()
                .checkin(checkinDate)
                .checkout(checkoutDate)
                .camperPlace(camperPlace)
                .user(user)
                .price(calculator.calculateFinalReservationCost(checkinDate, checkoutDate, camperPlace.getPrice()))
                .paid(false);

        if (isActive(checkinDate, checkoutDate)) {
            toCreate.reservationStatus(ACTIVE);
        }

        super.create(toCreate.build());
    }

    @Override
    @Transactional
    public void delete(int id) {
        Reservation reservation = findById(id);
        super.delete(reservation);
    }

    @Override
    @Transactional
    public void update(int id, ReservationRequest request) {

        CamperPlace camperPlace = camperPlaceService.findByIndex(request.camperPlaceIndex());
        LocalDate checkin = request.checkin() == null ? null : LocalDate.parse(request.checkin());
        LocalDate checkout = request.checkout() == null ? null : LocalDate.parse(request.checkout());

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
        User user = reservationToUpdate.getUser();

        if (!user.equals(request.user())) {
            userService.update(request.user());
        }

        reservationToUpdate.setUpdatedAt(new Date());
        Optional.ofNullable(checkin).ifPresent(reservationToUpdate::setCheckin);
        Optional.ofNullable(checkout).ifPresent(reservationToUpdate::setCheckout);
        Optional.ofNullable(camperPlace).ifPresent(reservationToUpdate::setCamperPlace);
        Optional.ofNullable(request.paid()).ifPresent(reservationToUpdate::setPaid);

        super.update(reservationToUpdate);
    }

    public Page<ReservationDTO> findDTOBy(Pageable pageable, String fieldName, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (fieldName.equals("camperPlaceIndex")) {
            return repository.findAllByCamperPlace_Index(pageable, value).map(Mapper::toReservationDTO);
        } else if (fieldName.equals("stringUser")) {
            return repository.findAllByUserFullName(pageable, value).map(Mapper::toReservationDTO);
        }
        return super.findDTOBy(pageable, fieldName, value);
    }

    @Override
    public List<Reservation> findByCamperPlaceIdIfPaid(int id) {
        return repository.findByCamperPlace_IdIfPaid(id);
    }

    @Override
    public List<Reservation> findByYearAndCamperPlaceIdIfPaid(int year, int id) {
        return repository.findAllByYearAndCamperPlaceIdIfPaid(year, id);
    }

    @Override
    public  List<Reservation> findByMonthYearAndCamperPlaceIdIfPaid(int month, int year, int camperPlaceId) {
        return repository.findAllByMonthYearAndCamperPlaceIdIfPaid(month, year, camperPlaceId);
    }

    @Override
    public List<Reservation> findByCamperPlaceIdIfStatusNotComing(int id) {
        return repository.findAllByCamperPlaceIdIfStatusNotComing(id);
    }

    @Override
    public List<Reservation> findByYearAndCamperPlaceIdIfStatusNotComing(int year, int id) {
        return repository.findAllByYearAndCamperPlace_IdIfStatusNotComing(year, id);
    }

    @Override
    public List<Reservation> findByMonthYearAndCamperPlaceIdIfStatusNotComing(int month, int year, int id) {
        return repository.findAllByMonthYearAndCamperPlace_IdIfStatusNotComing(month, year, id);
    }

    @Override
    public Map<String, ReservationMetadataDTO> getReservationMetadataDTO() {
        return reservationMetadataMapper.getReservationMetaDataDTO();
    }


    private void ensureDataIsCorrect(String checkin, String checkout, CamperPlace camperPlace, User user) {

        ensure(checkin.isEmpty(), "Podaj datę wjazdu");

        ensure(checkout.isEmpty(), "Podaj datę wyjazdu");

        ensure(LocalDate.parse(checkin).isAfter(LocalDate.parse(checkout))
                        || LocalDate.parse(checkin).isEqual(LocalDate.parse(checkout)),
                "Wprowadzone daty są nieprawidłowe");

        ensure(user == null, "Pole gościa nie może być puste");

        ensure(setToStringIfNull(user.getLastName()).isEmpty()
                        && setToStringIfNull(user.getFirstName()).isEmpty()
                        && setToStringIfNull(user.getPhoneNumber()).isEmpty(),
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

