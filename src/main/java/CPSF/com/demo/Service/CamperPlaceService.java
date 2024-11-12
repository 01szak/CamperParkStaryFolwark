package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Repository.CamperPlaceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class CamperPlaceService {

    private final CamperPlaceRepository camperPlaceRepository;

    public CamperPlaceService(CamperPlaceRepository camperPlaceRepository) {
        this.camperPlaceRepository = camperPlaceRepository;
    }

    public CamperPlace findCamperPlaceById(int camperPlaceNumber){
    CamperPlace camperPlace = camperPlaceRepository.findCamperPlaceById(camperPlaceNumber);
        return camperPlace;
    }
    public Boolean isCamperPlaceOccupied(int camperPlaceNumber){
        CamperPlace camperPlace = findCamperPlaceById(camperPlaceNumber);

        if (camperPlace.getIsOccupied() == 1){
            return true;
        };
        return false;
    }
    public void setIsOccupiedIfReservationContinuesAtTheMoment(Reservation reservation){
        CamperPlace camperPlace = reservation.getCamperPlace();
        Stream<LocalDate> daysBetweenEnterAndCheckout = reservation.getDateEnter().datesUntil(reservation.getDateCheckout());
        if(daysBetweenEnterAndCheckout.anyMatch(date -> date.equals(LocalDate.now()))){
            camperPlace.setIsOccupied(1);
        }else {
            camperPlace.setIsOccupied(0);
        }


    }
}

