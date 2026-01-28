package CPSF.com.demo.service;

import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.Guest;
import CPSF.com.demo.request.ReservationRequest;

import java.util.List;
import java.util.Map;

public interface ReservationService extends CRUDService<Reservation, ReservationDTO> {

    void create(String checkin, String checkout, String camperPlaceIndex, Guest guest);

    void update(int id, ReservationRequest request);

    Map<String, ReservationMetadataDTO> getReservationMetadataDTO();

    List<Reservation> findByCamperPlaceIdIfPaid(int id);

    List<Reservation> findByYearAndCamperPlaceIdIfPaid(int year, int id);

    List<Reservation> findByMonthYearAndCamperPlaceIdIfPaid(int month, int year, int id);

    List<Reservation> findByCamperPlaceIdIfStatusNotComing(int id);

    List<Reservation> findByYearAndCamperPlaceIdIfStatusNotComing(int year, int id);

    List<Reservation> findByMonthYearAndCamperPlaceIdIfStatusNotComing(int month, int year, int id);
}
