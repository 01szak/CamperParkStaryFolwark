package CPSF.com.demo.Service;

import CPSF.com.demo.Repository.GuestRepository;
import CPSF.com.demo.Entity.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
