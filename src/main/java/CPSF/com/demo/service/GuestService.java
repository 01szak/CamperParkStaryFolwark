package CPSF.com.demo.service;

import CPSF.com.demo.Entity.Employee;
import CPSF.com.demo.Entity.Guest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GuestService {

    List<Guest> findAll();

    Guest findByPlace(int place);

    Guest save (Guest theGuest);
     void deleteByPlace(int place);

}
