package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Guest,Integer> {

    @Query("SELECT g from Guest g")
    List<Guest> findAllGuests();

}
