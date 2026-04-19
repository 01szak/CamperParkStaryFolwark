package CPSF.com.demo.service.core;

import CPSF.com.demo.exception.ClientInputException;
import CPSF.com.demo.model.dto.CamperPlace_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CamperPlaceService extends CRUDServiceImpl<CamperPlace> {

    private static final int DUPLICATE_ENTRY = 1062;

    private final CamperPlaceRepository camperPlaceRepository;
    private final CamperPlaceTypeService camperPlaceTypeService;

    public void create(CamperPlace_DTO camperPlaceDto) {
        var camperPlaceType = camperPlaceTypeService.findById(camperPlaceDto.type().id());

        if (camperPlaceType == null) {
            throw new IllegalArgumentException("Invalid type");
        }
        var cpIndex = camperPlaceDto.index() != null ? camperPlaceDto.index() : generateNextIndex();
        validateIndex(cpIndex);

        try {
            create(CamperPlace.builder()
                    .index(cpIndex)
                    .camperPlaceType(camperPlaceType)
                    .build()
            );
        } catch (DataIntegrityViolationException e) {
            var cause =  e.getMostSpecificCause();
            if (cause instanceof SQLIntegrityConstraintViolationException sqlEx && DUPLICATE_ENTRY == sqlEx.getErrorCode()) {
                throw new ClientInputException("Parcela z podanym indexem już istnieje");
            }
            throw new IllegalStateException();
        }
    }

    private void validateIndex(String cpIndex) {
        if (cpIndex == null || cpIndex.isBlank()) {
            throw new ClientInputException("Indedx nie może byc pusty");
        }
        var firstEl = String.valueOf(cpIndex.charAt(0));
        try {
            Integer.parseInt(firstEl);
        } catch (NumberFormatException e) {
            throw new ClientInputException("Index musi zaczynać sie od cyfry");
        }
    }

    private String generateNextIndex() {
        var maxIndex = camperPlaceRepository.getCamperplaceMaxIndex();
        return String.valueOf(Integer.parseInt(maxIndex) + 1);
    }

    public List<CamperPlace> updateCamperPlaces(List<CamperPlace_DTO> camperPlaceDtos) {
        try {
            var cpToUpdate = new ArrayList<CamperPlace>();
            camperPlaceDtos.forEach(dto -> {
                validateIndex(dto.index());
                var cp = mapToCamperPlace(dto);
                cpToUpdate.add(cp);
            });
            return super.update(cpToUpdate);
        } catch (DataIntegrityViolationException e) {
            var cause =  e.getMostSpecificCause();
            if (cause instanceof SQLIntegrityConstraintViolationException sqlEx && DUPLICATE_ENTRY == sqlEx.getErrorCode()) {
                throw new ClientInputException("Parcela z podanym indexem już istnieje");
            }
            throw new IllegalStateException();
        }
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

