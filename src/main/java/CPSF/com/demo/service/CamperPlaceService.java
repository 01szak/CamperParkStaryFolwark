package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;

import java.util.List;

public interface CamperPlaceService {

    void deleteCamperPlace(String index);

    void createCamperPlace(Type type, double price);

    List<CamperPlace> findCamperPlacesByIds(List<Integer> ids);

    List<CamperPlace> getAll();

    void setIsCamperPlaceOccupied(CamperPlace camperPlace);
}

