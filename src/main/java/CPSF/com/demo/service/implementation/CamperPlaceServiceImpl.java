package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceRepository;
import CPSF.com.demo.service.CRUDService;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CamperPlaceServiceImpl extends CRUDService<CamperPlace, CamperPlaceDTO> implements CamperPlaceService {

    private final CamperPlaceRepository repository  ;

    @Autowired
    public CamperPlaceServiceImpl(CamperPlaceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void create(Type type, double price) {
        if (Stream.of(Type.values()).noneMatch(type::equals)) {
            throw new IllegalArgumentException("Invalid type");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        create(CamperPlace.builder()
                .type(type)
                .price(price)
                .createdAt(new Date())
                .build()
        );
    }

    @Override
    @Transactional
    public void update(CamperPlace camperPlace) {
        //TODO
//        super.update(id, camperPlace);
    }

    @Override
    @Transactional
    public void delete(CamperPlace camperPlace) {
        super.delete(camperPlace);
    }

    @Override
    @Transactional
    public void delete(String idnex) {
        CamperPlace cp = findByIndex(idnex);
        delete(cp);
    }

    public Page<CamperPlaceDTO> findAllDTO(Pageable pageable) {
        return super.findAllDTO(pageable);
    }

    public Page<CamperPlaceDTO> findAllDTO() {
        return super.findAllDTO();
    }
//    @Override
//    public void setIsCamperPlaceOccupied(CamperPlace camperPlace) {
//        camperPlace.setIsOccupied(!camperPlace.getIsOccupied());
//    }

    @Override
    public CamperPlace findByIndex(String index) {
        return repository.findByIndex(index);
    }

    @Override
    public Page<CamperPlace> findAll() {
        return super.findAll();
    }

    @Override
    public CamperPlace findById(int id) {
        return super.findById(id);
    }

    @Override
    public boolean checkIsCamperPlaceOccupied(
            CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId) {
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
//    public Boolean isCamperPlaceOccupied(String index) {
//        CamperPlace camperPlace = findCamperPlaceByIndex(index);
//
//        return camperPlace.getIsOccupied();
//    }
//
}

