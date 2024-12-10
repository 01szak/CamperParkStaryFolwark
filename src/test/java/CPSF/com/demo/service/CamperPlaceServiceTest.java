package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.repository.CamperPlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CamperPlaceServiceTest {
    @Mock
    private CamperPlaceRepository camperPlaceRepository;
    private CamperPlaceService underTest;
    @BeforeEach
    void setUp() {
        underTest = new CamperPlaceService(camperPlaceRepository);
    }


    @Test
    void isreateCamperPlace() {
    }

    @Test
    void findAllCamperPlaces() {
    }

    @Test
    void findCamperPlaceById() {
    }

    @Test
    void isCamperPlaceOccupied() {
        //given
        CamperPlace camperPlace = new CamperPlace(1,false, Type.STANDARD, BigDecimal.valueOf(150.00),new ArrayList<>());
        //then
        assertFalse(camperPlace.getIsOccupied());
    }

//    @Test
//    void setIsOccupiedAndReservationStatusDependingOnReservationDay() {
//        //given
//        int id = 1;
//        Reservation reservation1 = new Reservation(id, LocalDate.of(2023,10,1),LocalDate.of(2023,10,10),new CamperPlace(),new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getCarRegistration(), request.getCountry(), request.getCity(), request.getStreetAddress(), request.getPassword(), Role.GUEST, reservations));
//        Reservation reservation2= new Reservation(id, LocalDate.of(2024,11,25),LocalDate.of(2024,11,30),new CamperPlace(),new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getCarRegistration(), request.getCountry(), request.getCity(), request.getStreetAddress(), request.getPassword(), Role.GUEST, reservations));
//        Reservation reservation3 = new Reservation(id, LocalDate.of(2025,10,1),LocalDate.of(2025,10,10),new CamperPlace(),new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getCarRegistration(), request.getCountry(), request.getCity(), request.getStreetAddress(), request.getPassword(), Role.GUEST, reservations));
//        List<Reservation> reservationList = new ArrayList<>();
//        reservationList.add(reservation1);
//        reservationList.add(reservation2);
//        reservationList.add(reservation3);
//        for (Reservation reservation : reservationList){
//            underTest.setIsOccupiedAndReservationStatusDependingOnReservationDay(reservation);
//        }
//
//        //then
//
//        assertEquals(ReservationStatus.EXPIRED,reservation1.getReservationStatus());
//        assertEquals(ReservationStatus.ACTIVE,reservation2.getReservationStatus());
//        assertEquals(ReservationStatus.COMING,reservation3.getReservationStatus());
//
//        assertFalse(reservation1.getCamperPlace().getIsOccupied());
//        assertTrue(reservation2.getCamperPlace().getIsOccupied());
//        assertFalse(reservation3.getCamperPlace().getIsOccupied());
//
//    }
}