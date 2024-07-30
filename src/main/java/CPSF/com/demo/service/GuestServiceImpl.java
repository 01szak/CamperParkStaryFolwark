package CPSF.com.demo.service;

import CPSF.com.demo.DAO.GuestRepository;
import CPSF.com.demo.Entity.Guest;
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
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    public Guest findByPlace(int place) {
        Optional<Guest> result = guestRepository.findById(place);
        Guest theGuest = null;

        if(result.isPresent()){
            theGuest = result.get();
        }else {
            throw new RuntimeException("Miejsce jest puste...");
        }
        return theGuest;
    }

    @Override
    public Guest save(Guest theGuest) {
        return null;
    }

    @Override
    public void deleteByPlace(int place) {

    }
}
