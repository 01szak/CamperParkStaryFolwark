package CPSF.com.demo.service;

import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.request.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ReservationService extends CRUDService<Reservation, ReservationDTO> {

    void create(String checkin, String checkout, String camperPlaceIndex, User user);

    Page<Reservation> findAll(Pageable pageable);

    Page<Reservation> findAll();

    Page<ReservationDTO> findAllDTO(Pageable pageable);

    void update(int id, ReservationRequest request);

    Map<String, ReservationMetadataDTO> getReservationMetadataDTO();

    List<Reservation> findByCamperPlaceId(int id);

    List<Reservation> findByYearAndCamperPlaceId(int year, int id);

    List<Reservation> findByMonthYearAndCamperPlaceId(int month, int year, int id);
}
