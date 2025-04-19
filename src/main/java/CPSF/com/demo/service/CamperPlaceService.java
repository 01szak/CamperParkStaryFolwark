package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CamperPlaceService {


    CamperPlaceRepository camperPlaceRepository;


    @Autowired
    public CamperPlaceService(CamperPlaceRepository camperPlaceRepository) {
        this.camperPlaceRepository = camperPlaceRepository;
    }

    @Transactional
    public void deleteCamperPlace(String index) {

        camperPlaceRepository.delete(camperPlaceRepository.findCamperPlaceByIndex(index));


    }

    @Transactional
    public void createCamperPlace(Type type, double price) {
        if (Stream.of(Type.values()).noneMatch(type::equals)) {
            throw new IllegalArgumentException("Invalid type");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        camperPlaceRepository.save(CamperPlace.builder()
                        .type(type)
                        .price(price)
                        .build()
        );
    }


    public List<CamperPlace> findAllCamperPlaces() {
        return camperPlaceRepository.findAll();
    }

    public CamperPlace findCamperPlaceByIndex(String index) {
        return camperPlaceRepository.findCamperPlaceByIndex(index);
    }

    public Boolean isCamperPlaceOccupied(String index) {
        CamperPlace camperPlace = findCamperPlaceByIndex(index);

        return camperPlace.getIsOccupied();
    }

    @Transactional
    public void setIsCamperPlaceOccupied(CamperPlace camperPlace) {

        boolean isOccupied = camperPlace.getReservations().stream().anyMatch(reservation ->
                reservation.getReservationStatus().equals(ReservationStatus.ACTIVE));

        camperPlace.setIsOccupied(isOccupied);
        camperPlaceRepository.save(camperPlace);
    }


    public CamperPlace findById(int id) {
        return camperPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException("CamperPlace not found!"));
    }

    public boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId) {
        return camperPlace.getReservations().stream().filter(reservation -> reservation.getId() != reservationId)
                .anyMatch(reservation -> ((
                        (!checkin.isBefore(reservation.getCheckin()) && !checkin.isAfter(reservation.getCheckout()) && !checkin.equals(reservation.getCheckout()))
                                || (checkin.isBefore(reservation.getCheckin()) && checkout.isAfter(reservation.getCheckout())) &&
                                (!checkout.isBefore(reservation.getCheckin()) && !checkout.isEqual(reservation.getCheckin()))
                                || checkout.isEqual(reservation.getCheckout())))
                );
    }
}

