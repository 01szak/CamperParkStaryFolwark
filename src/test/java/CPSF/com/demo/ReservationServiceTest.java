package CPSF.com.demo;

import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    ReservationService reservationService;

    @Test
    public void  isFiltering() {

        ReservationDto[] testList =  new ReservationDto[]{
                new ReservationDto(1,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.EXPIRED.toString(),1,"expiredUser","",""),
                new ReservationDto(2,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.EXPIRED.toString(),2,"expiredUser","",""),
                new ReservationDto(3,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.EXPIRED.toString(),3,"expiredUser","",""),
                new ReservationDto(4,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.ACTIVE.toString(),4,"activeUser","",""),
                new ReservationDto(5,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.ACTIVE.toString(),5,"activeUser","",""),
                new ReservationDto(6,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.COMING.toString(),5,"comingUser","",""),
                new ReservationDto(7,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.COMING.toString(),6,"comingUser","",""),
                new ReservationDto(8,LocalDate.now(),LocalDate.now().plusDays(5),ReservationStatus.COMING.toString(),7,"comingUser","",""),


        };
        String value = "Expired";
        when(reservationService.findAllReservationsDto()).thenReturn(Arrays.stream(testList).toList());

        List<ReservationDto> filteredList = reservationService.getFilteredData("Expired");

        filteredList.forEach(reservationDto -> {
            Assertions.assertEquals(reservationDto.getReservationStatus(), value.toLowerCase());
        });

    }
}
