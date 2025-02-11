package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserService userService;
    @Autowired
    CamperPlaceService camperPlaceService;
    @Autowired
    Mapper mapper;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @Transactional
    public void createReservation(LocalDate checkin, LocalDate checkout, CamperPlace camperPlace, User user) {
        userService.create(user);

        System.out.println(
                "You have successfully made a reservation: \nid: "
                        + camperPlace.getId()
                        + "\ndate: " + checkin + "/" + checkout);

        reservationRepository.save(Reservation.builder()
                .checkin(checkin)
                .checkout(checkout)
                .camperPlace(camperPlace)
                .user(user)
                .build());

    }

    public List<ReservationDto> findAllReservationsDto() {
        return reservationRepository.findAll().stream().map(mapper::toReservationDto).toList();
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation findReservationById(int id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public List<Reservation> findByUserId(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> findAllReservationsByCamperPlace(int camperPlaceId) {
        return reservationRepository.findAllByCamperPlaceId(camperPlaceId);
    }


    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }





    public List<Reservation> findAllUserReservations(int id) {
        return reservationRepository.findByUserId(id);
    }

    public List<ReservationDto> getFilteredData(String value) {

        List<ReservationDto> allReservationsDto = findAllReservationsDto();
        if (value.equals("empty") || value.isEmpty() || value.equals(" ")) {
            return allReservationsDto;
        }

        List<ReservationDto> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allReservationsDto.forEach(reservationDto -> {
            if (reservationDto.getCheckin().toString().contains(filterValue) ||
                    reservationDto.getCheckout().toString().contains(filterValue) ||
                    reservationDto.getUserFirstName().toLowerCase().contains(filterValue) ||
                    reservationDto.getUserLastName().toLowerCase().contains(filterValue) ||
                    reservationDto.getReservationStatus().toLowerCase().contains(filterValue) ||
                    (isNumber(value) && reservationDto.getCamperPlaceNumber() == Integer.parseInt(value))

            ) {
                filteredList.add(reservationDto);
            }

        });
        return filteredList;
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public List<ReservationDto> getSortedReservations(String sortedHeader, int isAsc) {
        if (isAsc == 1) {
            return sortAsc(sortedHeader);
        }
        return sortDesc(sortedHeader);


    }

    private List<ReservationDto> sortAsc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberAsc().stream().map(mapper::toReservationDto).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    private List<ReservationDto> sortDesc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberDesc().stream().map(mapper::toReservationDto).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    @Transactional
    public void updateReservation(int id, ReservationRequest request) {
        Reservation reservation = findReservationById(id);

        Optional.ofNullable(request.checkin()).ifPresent(reservation::setCheckin);
        Optional.ofNullable(request.checkout()).ifPresent(reservation::setCheckout);
        Optional.ofNullable(request.camperPlace()).filter(
                        camperPlace -> !camperPlace.getIsOccupied())
                .ifPresent(reservation::setCamperPlace);
        reservationRepository.save(reservation);
    }
}

