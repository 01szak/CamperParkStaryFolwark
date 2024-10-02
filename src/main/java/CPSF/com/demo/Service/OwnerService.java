package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Entity.Owner;
import CPSF.com.demo.OccupiedPlace;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OwnerService {

     List<Guest> findAllGuests();
//     Guest findGuestByOccupiedPlace(OccupiedPlace occupiedPlace);
//     List<Guest> findAllByOccupiedPlaceDesc();
//     List<Guest> findAllByOccupiedPlaceAsc();
     List<Guest> getSortedTable(Sort.Direction direction);





}
