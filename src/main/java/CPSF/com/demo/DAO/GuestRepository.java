package CPSF.com.demo.DAO;

import CPSF.com.demo.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository  extends JpaRepository<Guest, Integer> {

}
