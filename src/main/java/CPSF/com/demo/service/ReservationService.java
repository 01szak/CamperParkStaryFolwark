package CPSF.com.demo.service;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.request.ReservationRequest;

import java.util.List;

public interface ReservationService extends CRUDService<Reservation>{

    void create(String checkin, String checkout, String camperPlaceIndex, User user);

    void update(int id, ReservationRequest request);

    void updateReservationStatus(Reservation reservation);

    List<Reservation> findByCamperPlaceId(int id);

    List<Reservation> findByYearAndCamperPlaceId(int year, int id);

    List<Reservation> findByMonthYearAndCamperPlaceId(int month, int year, int id);
}
