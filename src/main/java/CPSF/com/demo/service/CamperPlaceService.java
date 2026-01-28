package CPSF.com.demo.service;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.CamperPlaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CamperPlaceService extends CRUDService<CamperPlace, CamperPlaceDTO>  {

    CamperPlace findByIndex(String index);

    Page<CamperPlace> findAll();

    boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId);

    void create(CamperPlaceType camperPlaceType, BigDecimal price);

    void deleteByIndex(String index);

    Page<CamperPlaceDTO> findAllDTO();

    Page<CamperPlaceDTO> findAllDTO(Pageable pageable);
}

