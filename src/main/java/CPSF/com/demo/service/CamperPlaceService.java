package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (price == null || price.intValue() == 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        CamperPlace camperPlace = new CamperPlace();
        camperPlace.setType(type);
        camperPlace.setPrice(price);
        camperPlaceRepository.save(camperPlace);
    }

    public CamperPlace findCamperPlaceById(int camperPlaceId) {
        CamperPlace camperPlace = camperPlaceRepository.findById(camperPlaceId).orElseThrow();
        return camperPlace;
    }

    public Boolean isCamperPlaceOccupied(int camperPlaceNumber) {
        CamperPlace camperPlace = findCamperPlaceById(camperPlaceNumber);

        if (camperPlace.getIsOccupied() == 1) {
            return true;
        }

        return false;
    }

    public void setIsOccupiedIfReservationContinuesAtTheMoment(Reservation reservation) {
        CamperPlace camperPlace = reservation.getCamperPlace();
        Stream<LocalDate> daysBetweenEnterAndCheckout = reservation.getCheckin().datesUntil(reservation.getCheckout());
        if (daysBetweenEnterAndCheckout.anyMatch(date -> date.equals(LocalDate.now()))) {
            camperPlace.setIsOccupied(1);
        } else {
            camperPlace.setIsOccupied(0);
        }


    }
}

