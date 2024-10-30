package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Guest,Integer> {
Guest findGuestByOccupiedPlace(int occupiedPlace);

}
