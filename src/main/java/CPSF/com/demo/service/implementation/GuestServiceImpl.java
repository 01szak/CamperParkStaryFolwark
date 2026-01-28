package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.GuestDTO;
import CPSF.com.demo.request.UserRequest;
import CPSF.com.demo.service.GuestService;
import CPSF.com.demo.util.Mapper;
import CPSF.com.demo.entity.Guest;
import CPSF.com.demo.repository.GuestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static CPSF.com.demo.exception.ClientInputExceptionUtil.ensure;

@Service
public class GuestServiceImpl extends CRUDServiceImpl<Guest, GuestDTO> implements GuestService {

    private final GuestRepository repository;

    public GuestServiceImpl(GuestRepository repository) {
        super(repository);
        this.repository = repository;
    }

//    public List<UserDTO> findAllUsersDto() {
//        return repository.findAll().stream()
//                .map(Mapper::toUserDTO)
//                .toList();
//    }

    @Override
    @Transactional
    public void create(UserRequest user) {
        checkIsDataCorrect(user);

        super.create(Guest.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .carRegistration(user.carRegistration())
                .phoneNumber(user.phoneNumber())
                .createdAt(new Date())
                .build()
        );
    }

    public Page<GuestDTO> findDTOBy(Pageable pageable, String by, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
            if (by.equals("fullName")) {
                return repository.findAllByFullName(pageable, value).map(Mapper::toUserDTO);
            }
            return super.findDTOBy(pageable, by, value);
    }
    @Override
    @Transactional
    public void create(Guest guest) {
        super.create(guest);
    }

    @Override
    @Transactional
    public void update(int id, UserRequest request) {

        Guest guest = findById(id);

        if (!request.email().isBlank()) {
            Guest u = repository.findByEmail(request.email()).orElse(null);
            ensure(u != null && !u.equals(guest), "Ten email jest już używany");
        }

        ensure(
                guest.getFirstName().equals(request.firstName())
                        && guest.getLastName().equals(request.lastName())
                        && guest.getEmail().equals(request.email())
                        && guest.getCarRegistration().equals(request.carRegistration())
                        && guest.getPhoneNumber().equals(request.phoneNumber()),
                "Nie podano żadnych zmian"
        );

        Optional.ofNullable(request.firstName()).ifPresent(guest::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(guest::setLastName);
        Optional.ofNullable(request.email()).ifPresent(guest::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(guest::setPhoneNumber);
        Optional.ofNullable(request.carRegistration()).ifPresent(guest::setCarRegistration);

        super.update(guest);
    }

    @Override
    public void update( Guest guest) {
        super.update(guest);
    }


    @Override
    public Page<Guest> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Override
    public Page<Guest> findAll() {
        return super.findAll();
    }

    @Override
    public Page<GuestDTO> findAllDTO(Pageable pageable) {
        return super.findAllDTO(pageable);
    }

//    public List<UserDTO> getFilteredUsers(String value) {
//
//        if (value == null) {
//            return findAllUsersDto();
//        }
//
//        List<UserDTO> allUsersDto = findAllUsersDto();
//        List<UserDTO> filteredList = new ArrayList<>();
//        String filterValue = value.toLowerCase();
//        allUsersDto.forEach(userDto -> {
//            if (
//                ((userDto.getFirstName() != null) && userDto.getFirstName().toLowerCase().contains(filterValue)) ||
//                ((userDto.getLastName() != null) && userDto.getLastName().toLowerCase().contains(filterValue)) ||
//                ((userDto.getEmail() != null) && userDto.getEmail().toLowerCase().contains(filterValue)) ||
//                ((userDto.getPhoneNumber() != null) && userDto.getPhoneNumber().toLowerCase().contains(filterValue) ||
//                ((userDto.getCarRegistration() != null) && (userDto.getCarRegistration()).toLowerCase().contains(filterValue)) ||
//                ((userDto.getCountry() != null) && (userDto.getCountry()).toLowerCase().contains(filterValue)) ||
//                ((userDto.getCity() != null) && (userDto.getCity()).toLowerCase().contains(filterValue)) ||
//                ((userDto.getStreetAddress() != null) && (userDto.getStreetAddress()).toLowerCase().contains(filterValue))
//                        )) {
//                filteredList.add(userDto);
//            }
//
//        });
//        return filteredList;
//    }



//    public UserDTO findUserDtoById(int id) {
//        return userRepository.findById(id).map(Mapper::toUserDTO).orElseThrow();
//    }

//    public void createUserIfDontExist(User user) {
//
//        User u = user.getId() <= 0 || findUserById(user.getId()) == null ? create(user) : user;
//        return u;
//    }

    @Override
    @Transactional
    public void delete(Guest guest) {
        super.delete(guest);
    }

    @Override
    public void delete(int id) {
        super.delete(findById(id));
    }

    @Override
    public Guest findById(int id) {
        return super.findById(id);
    }
    
    @Override
    public GuestDTO findDTOById(int id) {
        return Mapper.toUserDTO(findById(id));
    }


    private void checkIsDataCorrect(UserRequest user) {
        ensure(
                user.email().isBlank()
                        && user.firstName().isBlank()
                        && user.lastName().isBlank()
                        && user.carRegistration().isBlank()
                        && user.phoneNumber().isBlank(),
                "Formularz jest pusty!"
        );
        ensure(!user.email().isBlank() && repository.findByEmail(user.email()).isPresent(),
                "Ten email jest już używany");
    }
}

