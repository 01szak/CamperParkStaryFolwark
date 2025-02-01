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
public void deleteCamperPlace(int camperPlaceNumber){
        camperPlaceRepository.delete(findCamperPlaceById(camperPlaceNumber));
}
    @Transactional
    public void createCamperPlace(Type type, double price) {
        if(Stream.of(Type.values()).noneMatch(type::equals)) {
            throw new IllegalArgumentException("Invalid type");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        camperPlaceRepository.save(new CamperPlace(false,type, price));
    }


    public List<CamperPlace> findAllCamperPlaces() {
        return camperPlaceRepository.findAll();
    }

    public CamperPlace findCamperPlaceById(int camperPlaceId) {
        return camperPlaceRepository.findById(camperPlaceId).orElseThrow();
    }

    public Boolean isCamperPlaceOccupied(int camperPlaceNumber) {
        CamperPlace camperPlace = findCamperPlaceById(camperPlaceNumber);

        if (camperPlace.getIsOccupied().equals(true)) {
            return true;
        }

        return false;
    }


}

