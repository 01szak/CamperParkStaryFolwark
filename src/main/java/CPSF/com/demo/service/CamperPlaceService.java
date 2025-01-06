package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CamperPlaceService {

    private final CamperPlaceRepository camperPlaceRepository;

    public CamperPlaceService(CamperPlaceRepository camperPlaceRepository) {
        this.camperPlaceRepository = camperPlaceRepository;
    }

    @Transactional
    public void createCamperPlace(String type, double price) {
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        setCamperPlace(type, price);
    }
        @Transactional
        public void setCamperPlace(String type, double price) {
        CamperPlace camperPlace = new CamperPlace();
        if(camperPlace.getIsOccupied() == null)camperPlace.setIsOccupied(false);
        camperPlace.setType(Type.valueOf(type));
        camperPlace.setPrice(BigDecimal.valueOf(price));
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


}

