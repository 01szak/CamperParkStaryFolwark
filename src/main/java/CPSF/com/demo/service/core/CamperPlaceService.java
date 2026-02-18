package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class CamperPlaceService extends CRUDServiceImpl<CamperPlace> {

    @Autowired
    private CamperPlaceRepository camperPlaceRepository;

    @Autowired
    private CamperPlaceTypeService camperPlaceTypeService;

    public void create(CamperPlaceTypeDTO camperPlaceTypeDTO, BigDecimal price) {
        var camperPlaceType = camperPlaceTypeService.findById(camperPlaceTypeDTO.id());

        if (camperPlaceType == null) {
            throw new IllegalArgumentException("Invalid type");
        }

        if (price.intValue() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        create(CamperPlace.builder()
                .camperPlaceType(camperPlaceType)
                .price(price)
                .createdAt(new Date())
                .build()
        );
    }

    public List<CamperPlace> findAllOrderByIndex() {
        return camperPlaceRepository.findAllOrderByIndex();
    }

    public boolean isCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, Integer reservationIdToExclude) {
        var reservations = Optional.ofNullable(reservationIdToExclude)
                .map(id -> camperPlace.getReservations().stream()
                        .filter(r -> !r.getId().equals(id))
                        .toList()
                )
                .orElse(camperPlace.getReservations());

        for (Reservation res : reservations) {
            if (!res.getCheckout().isBefore(checkin.plusDays(1))
                    && !res.getCheckin().isAfter(checkout.minusDays(1))) {
                return true;
            }
        }
        return false;
    }

}

