package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CamperPlaceServiceImpl implements CamperPlaceService {

    private final CamperPlaceRepository camperPlaceRepository;

    @Override
    @Transactional
    public void create(CamperPlace camperPlace) {
        camperPlaceRepository.save(camperPlace);
    }

    public void create(Type type, double price) {
        if (Stream.of(Type.values()).noneMatch(type::equals)) {
            throw new IllegalArgumentException("Invalid type");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        camperPlaceRepository.save(
                CamperPlace.builder()
                    .type(type)
                    .price(price)
                    .build()
        );
    }



    @Override
    public Page<CamperPlace> findAll(Pageable pageable) {
        return camperPlaceRepository.findAll(
                pageable == null ? Pageable.unpaged() : pageable
        );
    }

    @Override
    public Page<CamperPlaceDTO> findAllDTO(Pageable pageable) {
        return findAll(pageable).map(Mapper::toCamperPlaceDTO);
    }

    public Page<CamperPlace> findAll() {
        return findAll(Pageable.unpaged());
    }

    @Override
    public CamperPlace findById(int id) {
        return null;
    }
    public CamperPlace findByIndex(String index) {
        return camperPlaceRepository.findByIndex(index);
    }

    @Override
    @Transactional
    public void update(int id, CamperPlace camperPlace) {
    }

    @Override
    @Transactional
    public void delete(int id) {
        camperPlaceRepository.delete(camperPlaceRepository.findById(id).orElseThrow());
    }

    @Override
    public List<CamperPlace> findCamperPlacesByIds(List<Integer> ids) {
        return List.of();
    }

    @Override
    public void setIsCamperPlaceOccupied(CamperPlace camperPlace) {
        camperPlace.setIsOccupied(!camperPlace.getIsOccupied());
    }

    public boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId) {
        for (Reservation res : camperPlace.getReservations()) {
            if (res.getId() == reservationId) continue;

            if (!res.getCheckout().isBefore(checkin.plusDays(1)) && !res.getCheckin().isAfter(checkout.minusDays(1))) {
                return true;
            }
        }
        return false;
    }

//    @Transactional
//    public void setIsCamperPlaceOccupied(CamperPlace camperPlace) {
//
//        boolean isOccupied = camperPlace.getReservations().stream().anyMatch(reservation ->
//                reservation.getReservationStatus().equals(ReservationStatus.ACTIVE));
//
//        camperPlace.setIsOccupied(isOccupied);
//        camperPlaceRepository.save(camperPlace);
//    }
//
//    public List<CamperPlaceDTO> findAllCamperPlacesDTO() {
//        return camperPlaceRepository.findAll().stream().map(Mapper::toCamperPlaceDTO).toList();
//    }

//    public List<CamperPlace> getAll() {
//        return camperPlaceRepository.findAll();
//    }

//
//    public Boolean isCamperPlaceOccupied(String index) {
//        CamperPlace camperPlace = findCamperPlaceByIndex(index);
//
//        return camperPlace.getIsOccupied();
//    }
//
//
//
//    public CamperPlace findById(int id) {
//        return camperPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException("CamperPlace not found!"));
//    }
//
//    public CamperPlace findCamperPlaceByIndex(String index) {
//        return camperPlaceRepository.findCamperPlaceByIndex(index);
//    }
//
//

//
//
//    public List<CamperPlace> findCamperPlacesByIds(List<Integer> ids) {
//        return camperPlaceRepository.findCamperPlaceByIdIn(ids);
//    }
}

