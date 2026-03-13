package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.dto.CamperPlace_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CamperPlaceService extends CRUDServiceImpl<CamperPlace> {

    private final CamperPlaceRepository camperPlaceRepository;
    private final CamperPlaceTypeService camperPlaceTypeService;

    public void create(CamperPlaceTypeDTO camperPlaceTypeDTO, BigDecimal price) {
        var camperPlaceType = camperPlaceTypeService.findById(camperPlaceTypeDTO.id());

        if (camperPlaceType == null) {
            throw new IllegalArgumentException("Invalid type");
        }

        create(CamperPlace.builder()
                .camperPlaceType(camperPlaceType)
                .price(price)
                .build()
        );
    }

    public List<CamperPlace> updateCamperPlaces(List<CamperPlace_DTO> camperPlaceDtos) {
        return camperPlaceDtos.stream()
                .map(this::mapToCamperPlace)
                .map(super::update)
                .toList();
    }

    private @NonNull CamperPlace mapToCamperPlace(CamperPlace_DTO dto) {
        var cp = findById(dto.id());
        var cpt = camperPlaceTypeService.findById(dto.type().id());
        cp.setCamperPlaceType(cpt);
        cp.setIndex(dto.index());
        cp.setPrice(cpt.getPrice().compareTo(dto.price()) == 0 ? null : dto.price());
        return cp;
    }

    public List<CamperPlace> findAllOrderByIndex() {
        return camperPlaceRepository.findAllOrderByIndex();
    }

    public boolean isOccupied(CamperPlace cp, LocalDate checkin, LocalDate checkout, @Nullable Integer idToExclude) {
        return cp.getReservations().stream()
                .filter(r -> !r.getId().equals(idToExclude))
                .anyMatch(r -> checkin.isBefore(r.getCheckout()) && checkout.isAfter(r.getCheckin()));
    }

    public List<CamperPlace> findCamperPlaceByPriceNotNullAndCamperPlaceType_Id(Integer id) {
        return camperPlaceRepository.findCamperPlaceByPriceNotNullAndCamperPlaceType_Id(id);
    }


    @Override
    protected CRUDRepository<CamperPlace> getRepository() {
        return camperPlaceRepository;
    }
}

