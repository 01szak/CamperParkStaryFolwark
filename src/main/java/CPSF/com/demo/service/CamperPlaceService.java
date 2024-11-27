package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CamperPlaceService {

    private final CamperPlaceRepository camperPlaceRepository;

    public CamperPlaceService(CamperPlaceRepository camperPlaceRepository) {
        this.camperPlaceRepository = camperPlaceRepository;
    }

    @Transactional
    public void createCamperPlace(Type type, BigDecimal price) {
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (price == null || price.intValue() == 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        setCamperPlace(type, price);
    }

        private void setCamperPlace(Type type, BigDecimal price) {
        CamperPlace camperPlace = new CamperPlace();
        if(camperPlace.getIsOccupied() == null)camperPlace.setIsOccupied(false);
        camperPlace.setType(type);
        camperPlace.setPrice(price);
        camperPlaceRepository.save(camperPlace);
    }

    public List<CamperPlace> findAllCamperPlaces() {
        List<CamperPlace> camperPlaces = camperPlaceRepository.findAll();
        return camperPlaces;
    }

    public CamperPlace findCamperPlaceById(int camperPlaceId) {
        CamperPlace camperPlace = camperPlaceRepository.findById(camperPlaceId).orElseThrow();
        return camperPlace;
    }

    public Boolean isCamperPlaceOccupied(int camperPlaceNumber) {
        CamperPlace camperPlace = findCamperPlaceById(camperPlaceNumber);

        if (camperPlace.getIsOccupied().equals(true)) {
            return true;
        }

        return false;
    }

    @Transactional
    public void setIsOccupiedAndReservationStatusDependingOnReservationDay(Reservation reservation) {
        CamperPlace camperPlace = reservation.getCamperPlace();
        Stream<LocalDate> daysBetweenEnterAndCheckout = reservation.getCheckin().datesUntil(reservation.getCheckout());
        if (daysBetweenEnterAndCheckout.anyMatch(date -> date.equals(LocalDate.now()))) {
            camperPlace.setIsOccupied(true);
            reservation.setReservationStatus(ReservationStatus.ACTIVE);
        } else if (daysBetweenEnterAndCheckout.anyMatch(date -> date.isAfter(LocalDate.now()))) {
            reservation.setReservationStatus(ReservationStatus.EXPIRED);
        } else {
            reservation.setReservationStatus(ReservationStatus.COMING);
            camperPlace.setIsOccupied(false);

        }


    }
}

