package CPSF.com.demo.service;

import CPSF.com.demo.DAO.GuestRepository;
import CPSF.com.demo.Entity.Guest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GuestServiceImpl implements GuestService {

    private GuestRepository guestRepository;

    @Autowired
    public GuestServiceImpl(GuestRepository theGuestRepository){
        guestRepository = theGuestRepository;
    }


    @Override
    public void save(Guest guest) {

        guestRepository.save(guest);
    }


}
