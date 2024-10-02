package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Entity.Owner;
import CPSF.com.demo.OccupiedPlace;
import CPSF.com.demo.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OwnerServiceImpl implements OwnerService{

    private OwnerRepository ownerRepository;

@Autowired
    public OwnerServiceImpl(OwnerRepository theOwnerRepository){ownerRepository = theOwnerRepository;}


    @Override
    public List<Guest>findAllGuests(){

        return ownerRepository.findAllGuests();
    }

//    @Override
//    public Guest findGuestByOccupiedPlace(OccupiedPlace occupiedPlace) {
//
//        return ownerRepository.findGuestByOccupiedPlace(occupiedPlace);
//    }
//
//    @Override
//    public List<Guest> findAllByOccupiedPlaceDesc() {
//        return ownerRepository.findAllByOccupiedPlaceDesc();
//    }
//
//    @Override
//    public List<Guest> findAllByOccupiedPlaceAsc() {
//        return ownerRepository.findAllByOccupiedPlaceAsc();
//    }

    @Override
    public List<Guest> getSortedTable(Sort.Direction direction) {
        return ownerRepository.getSortedTable();
    }

}
