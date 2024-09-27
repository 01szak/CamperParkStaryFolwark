package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository  extends JpaRepository<Guest, Integer> {

}
