package CPSF.com.demo.service.core;

import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlace.CamperPlaceType;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CamperPlaceService extends CRUDServiceImpl<CamperPlace> {

    @Autowired
    private CamperPlaceRepository camperPlaceRepository;

    public void create(CamperPlaceType camperPlaceType, BigDecimal price) {
        if (Stream.of(CamperPlaceType.values()).noneMatch(camperPlaceType::equals)) {
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

    public boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout) {
        for (Reservation res : camperPlace.getReservations()) {
            if (!res.getCheckout().isBefore(checkin.plusDays(1))
                    && !res.getCheckin().isAfter(checkout.minusDays(1))) {
                return true;
            }
        }
        return false;
    }
}

