package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.request.UserRequest;
import CPSF.com.demo.service.CRUDService;
import CPSF.com.demo.service.UserService;
import CPSF.com.demo.util.Mapper;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static exception.ClientInputExceptionUtil.ensure;

@Service
public class UserServiceImpl  extends CRUDService<User, UserDTO> implements UserService{

    private final UserRepository repository;

    public UserServiceImpl (UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

//    public List<UserDTO> findAllUsersDto() {
//        return repository.findAll().stream()
//                .map(Mapper::toUserDTO)
//                .toList();
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow();
    }

    @Override
    @Transactional
    public void create(UserRequest user) {
        checkIsDataCorrect(user);

        super.create(User.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .carRegistration(user.carRegistration())
                .phoneNumber(user.phoneNumber())
                .createdAt(new Date())
                .build()
        );
    }

    @Override
    @Transactional
    public void create(User user) {
        super.create(user);
    }

    @Override
    @Transactional
    public void update(int id, UserRequest request) {
        User user = super.findById(id);

        if (!request.email().isEmpty()) {
            User u = repository.findByEmail(request.email()).orElse(null);
            ensure(u != null && !u.equals(user), "Ten email jest już używany");
        }

//        user.setUpdatedAt(new Date());

        Optional.ofNullable(request.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.email()).ifPresent(user::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.carRegistration()).ifPresent(user::setCarRegistration);

        super.update(user);
    }

    @Override
    public void update( User user) {
        super.update(user);
    }


    @Override
    public Page<User> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Override
    public Page<User> findAll() {
        return super.findAll();
    }

    @Override
    public Page<UserDTO> findAllDTO(Pageable pageable) {
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
    public void delete(User user) {
        super.delete(user);
    }

    @Override
    public void delete(int id) {
        super.delete(findById(id));
    }

    @Override
    public User findById(int id) {
        return super.findById(id);
    }
    
    @Override
    public UserDTO findDTOById(int id) {
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

