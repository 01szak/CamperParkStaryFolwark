package CPSF.com.demo.service;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CamperPlaceService  {

    CamperPlace findByIndex(String index);

    Page<CamperPlace> findAll();

    CamperPlace findById(int id);

    boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId);

    void create(Type type, double price);

    void delete(CamperPlace camperPlace);

    void delete(String index);

    Page<CamperPlaceDTO> findAllDTO();

    Page<CamperPlaceDTO> findAllDTO(Pageable pageable);
}

