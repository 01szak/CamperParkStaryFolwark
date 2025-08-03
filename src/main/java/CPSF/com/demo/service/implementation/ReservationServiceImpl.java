package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.request.ReservationRequest;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.util.Mapper;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static exception.ClientInputExceptionUtil.ensure;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserServiceImpl userService;
    private final CamperPlaceServiceImpl camperPlaceService;
    private final ReservationMetadataMapper reservationMetadataMapper;

    @Override
    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void create(String checkin, String checkout, String camperPlaceIndex, User user) {
        CamperPlace camperPlace = camperPlaceService.findByIndex(camperPlaceIndex);

        ensureDataIsCorrect(checkin, checkout, camperPlace, user);

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);

        if (user.getId() <= 0) {
            userService.create(user);
        }

        create(
            reservationRepository.save(
                    Reservation.builder()
                            .checkin(checkinDate)
                            .checkout(checkoutDate)
                            .camperPlace(camperPlace)
                            .user(user)
                            .paid(false)
                            .createdAt(new Date())
                            .build()
            )
        );
    }


    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public Page<ReservationDTO> findAllDTO(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(Mapper::toReservationDTO);
    }

    @Override
    public Reservation findById(int id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(int id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
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

        ensure (
                camperPlaceService.checkIsCamperPlaceOccupied(
                    camperPlace,
                    checkin,
                    checkout,
                    id
                ),
                "Parcela jest już zajęta!"
        );

        ensure (
                checkin != null
                        && checkout != null
                        && checkout.isBefore(checkin),
                "Data wyjazdu nie może być przed datą wjazdu"
        );

        Reservation reservationToUpdate = findById(id);
        reservationToUpdate.setUpdatedAt(new Date());
        Optional.ofNullable(checkin).ifPresent(reservationToUpdate::setCheckin);
        Optional.ofNullable(checkout).ifPresent(reservationToUpdate::setCheckout);
        Optional.ofNullable(camperPlace).ifPresent(reservationToUpdate::setCamperPlace);
        Optional.ofNullable(request.paid()).ifPresent(reservationToUpdate::setPaid);

        update(id, reservationToUpdate);
    }

    @Override
    public void update(int id, Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public void updateReservationStatus(Reservation reservation) {

        if (isLocalDateInBetweenCheckinAndCheckout(reservation.getCheckin(), reservation.getCheckout())) {
            reservation.setReservationStatus(ReservationStatus.ACTIVE);
        } else if (!isLocalDateInBetweenCheckinAndCheckout(reservation.getCheckin(), reservation.getCheckout()) && LocalDate.now().isBefore(reservation.getCheckin())) {
            reservation.setReservationStatus(ReservationStatus.COMING);
        } else {
            reservation.setReservationStatus(ReservationStatus.EXPIRED);
        }
        reservationRepository.save(reservation);
    }

    public  List<Reservation> findByMonthYearAndCamperPlaceId(int month, int year, int camperPlaceId) {
        return reservationRepository.findByMonthYearAndCamperPlaceId(month, year, camperPlaceId);
    }

    public Map<String, ReservationMetadataDTO> getReservationMetadataDTO() {
        return reservationMetadataMapper.getReservationMetaDataDTO();
    }

    public List<ReservationDTO> findAllReservationsDto() {
        return reservationRepository.findAll().stream().map(Mapper::toReservationDTO).toList();
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByCamperPlaceId(int camperPlaceId) {
        return reservationRepository.findByCamperPlace_Id(camperPlaceId);
    }

    public List<Reservation> findByYearAndCamperPlaceId(int year, int camperPlaceId) {
        return reservationRepository.findByYearAndCamperPlaceId(year, camperPlaceId);
    }

    public List<ReservationDTO> getFilteredData(String value) {
        if (value == null) {
            return findAllReservationsDto();
        }
        List<ReservationDTO> allReservationsDto = findAllReservationsDto();

        List<ReservationDTO> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allReservationsDto.forEach(reservationDto -> {
            if (reservationDto.getCheckin().toString().contains(filterValue) ||
                    reservationDto.getCheckout().toString().contains(filterValue) ||
                    reservationDto.getUser().getFirstName().toLowerCase().contains(filterValue) ||
                    reservationDto.getUser().getLastName().toLowerCase().contains(filterValue) ||
                    reservationDto.getReservationStatus().toLowerCase().contains(filterValue) ||
                    (isNumber(value) && reservationDto.getCamperPlaceIndex().equals(value))

            ) {
                filteredList.add(reservationDto);
            }

        });
        return filteredList;
    }

    public List<ReservationDTO> getSortedReservations(String sortedHeader, int isAsc) {
        if (isAsc == 1) {
            return sortAsc(sortedHeader);
        }
        return sortDesc(sortedHeader);
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isLocalDateInBetweenCheckinAndCheckout(LocalDate checkin, LocalDate checkout) {
        return (LocalDate.now().isEqual(checkin) || LocalDate.now().isAfter(checkin))
                && (LocalDate.now().isEqual(checkout) || LocalDate.now().isBefore(checkout));
    }

    private List<ReservationDTO> sortAsc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    private List<ReservationDTO> sortDesc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    private void ensureDataIsCorrect(String checkin, String checkout, CamperPlace camperPlace, User user) {

        ensure(checkin.isEmpty(), "Podaj datę wjazdu");
        ensure(checkout.isEmpty(), "Podaj datę wyjazdu");
        ensure(
                LocalDate.parse(checkin).isAfter(LocalDate.parse(checkout))
                        || LocalDate.parse(checkin).isEqual(LocalDate.parse(checkout)),
                "Wprowadzone daty są nieprawidłowe"
        );
        ensure(user == null, "Pole gościa nie może być puste");
        ensure(
                setToStringIfNull(user.getLastName()).isEmpty()
                        && setToStringIfNull(user.getFirstName()).isEmpty()
                        && setToStringIfNull(user.getPhoneNumber()).isEmpty(),
                "Podaj dane gościa"
        );
        ensure(
                camperPlace == null,
                "Wybierz nr. parceli"
        );
        ensure(
                camperPlaceService.checkIsCamperPlaceOccupied(
                        camperPlace,
                        LocalDate.parse(checkin),
                        LocalDate.parse(checkout),
                        0
                ),
                "Parcela jest już zajęta!"
        );
    }

    private String setToStringIfNull(String s) {
		return s == null ? "" : s;
	}

}

