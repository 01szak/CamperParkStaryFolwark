package CPSF.com.demo.service;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface CamperPlaceService extends CRUDService<CamperPlace, CamperPlaceDTO> {


    List<CamperPlace> findCamperPlacesByIds(List<Integer> ids);

    Page<CamperPlace> findAll();

    void setIsCamperPlaceOccupied(CamperPlace camperPlace);


    boolean checkIsCamperPlaceOccupied(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, int reservationId);
}

