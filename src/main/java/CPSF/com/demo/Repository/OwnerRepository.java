package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.OccupiedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Guest,Integer> {

//    @Query("SELECT g from Guest g")
//    List<Guest> findAllGuests();
//
//    @Query("SELECT g from Guest g where g.occupiedPlace = :occupiedPlace")
//    Guest findGuestByOccupiedPlace(@Param("occupiedPlace")OccupiedPlace occupiedPlace);
//
//
//    @Query("SELECT g from Guest g ORDER BY g.occupiedPlace asc ")
//    List<Guest> findAllByOccupiedPlaceAsc();
//
//    @Query("SELECT g from Guest g  ORDER BY g.occupiedPlace desc ")
//    List<Guest> findAllByOccupiedPlaceDesc();

//   @Query ("SELECT g from Guest g order by g.occupiedPlace :order")
//    List<Guest> getSortedTable(String sortDir);
}
