package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Entity.Owner;
import CPSF.com.demo.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OwnerServiceImpl implements OwnerService{

    private OwnerRepository ownerRepository;

@Autowired
    public OwnerServiceImpl(OwnerRepository theOwnerRepository){ownerRepository = theOwnerRepository;}

    @Override
    public void add(Owner owner) {}
    @Override
    public List<Guest>findAllGuests(){

    return ownerRepository.findAllGuests();
    }

}
