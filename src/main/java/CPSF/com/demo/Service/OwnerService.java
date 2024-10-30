package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Entity.Owner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface OwnerService {

     List<Guest> findAllGuests();
//     Guest findGuestByOccupiedPlace(OccupiedPlace occupiedPlace);
//     List<Guest> findAllByOccupiedPlaceDesc();
//     List<Guest> findAllByOccupiedPlaceAsc();
     Guest getInfoByFirstNameOrLastName();

     List <Guest> getGuests();

     Guest findGuestByOccupiedPlace(int occupiedPlace);



}
