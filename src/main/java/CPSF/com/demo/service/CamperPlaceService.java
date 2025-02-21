package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CamperPlaceService {

   CamperPlaceRepository camperPlaceRepository;


    @Autowired
    public CamperPlaceService(CamperPlaceRepository camperPlaceRepository) {
        this.camperPlaceRepository = camperPlaceRepository;
    }

    @Transactional
    public void deleteCamperPlace(int camperPlaceNumber) {

        camperPlaceRepository.delete(camperPlaceRepository.findCamperPlaceByNumber(camperPlaceNumber));

        camperPlaceRepository.findAll().forEach(camperPlace -> {
            if (camperPlace.getNumber() > camperPlaceNumber) {
                camperPlace.setNumber(camperPlace.getNumber() - 1);
            }
        });
    }

    @Transactional
    public void createCamperPlace(Type type, double price) {
        if (Stream.of(Type.values()).noneMatch(type::equals)) {
            throw new IllegalArgumentException("Invalid type");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        int id = camperPlaceRepository.findMaxNumber() + 1;
        camperPlaceRepository.save(new CamperPlace(id, false, type, price));
    }


    public List<CamperPlace> findAllCamperPlaces() {
        return camperPlaceRepository.findAll();
    }

    public CamperPlace findCamperPlaceByNumber(int number) {
        return camperPlaceRepository.findCamperPlaceByNumber(number);
    }

    public Boolean isCamperPlaceOccupied(int number) {
        CamperPlace camperPlace = findCamperPlaceByNumber(number);

        return camperPlace.getIsOccupied();
    }

    @Transactional
    public void setIsCamperPlaceOccupied(CamperPlace camperPlace) {

        boolean isOccupied = camperPlace.getReservations().stream().anyMatch(reservation ->
                reservation.getReservationStatus().equals(ReservationStatus.ACTIVE));

        camperPlace.setIsOccupied(isOccupied);
        camperPlaceRepository.save(camperPlace);
    }



    public boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin,LocalDate checkout) {
        return camperPlace.getReservations().stream()
                .anyMatch(reservation -> ((
                        (!checkin.isBefore(reservation.getCheckin())  && !checkin.isAfter(reservation.getCheckout()) && !checkin.equals(reservation.getCheckout()))
                                || (checkin.isBefore(reservation.getCheckin()) && checkout.isAfter(reservation.getCheckout())) &&
                                (!checkout.isBefore(reservation.getCheckin()) && !checkout.isEqual(reservation.getCheckin()))
                                || checkout.isEqual(reservation.getCheckout())))
                );
    }
public CamperPlace findById(int id){
        return camperPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException("CamperPlace not found!"));
}
}

