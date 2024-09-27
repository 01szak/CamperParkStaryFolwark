package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Entity.Owner;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OwnerService {

     void  add(Owner owner);
     List<Guest> findAllGuests();




}
