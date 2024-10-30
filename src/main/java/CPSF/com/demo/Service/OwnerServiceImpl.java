package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OwnerServiceImpl implements OwnerService {

    private OwnerRepository ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerRepository theOwnerRepository) {
        ownerRepository = theOwnerRepository;
    }


    @Override
    public List<Guest> findAllGuests() {

        return ownerRepository.findAll();
    }


    @Override
    public Guest getInfoByFirstNameOrLastName() {
        return null;
    }

    @Override
    public List<Guest> getGuests() {
        List<Guest> guests = ownerRepository.findAll();
        return guests;
    }

    @Override
    public Guest findGuestByOccupiedPlace( int occupiedPlace) {
        return ownerRepository.findGuestByOccupiedPlace(occupiedPlace);
    }

}
