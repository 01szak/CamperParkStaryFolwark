package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.request.UserRequest;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public List<UserDTO> findAllUsersDto() {
        return userRepository.findAll()
                .stream()
                .map(Mapper::toUserDTO)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public void create(UserRequest user) {
        checkIsDataCorrect(user);
        create(User.builder()
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
    public void create(User user) {
        userRepository.save(user);
    }
    @Transactional
    public void update(int id, UserRequest request) {
        User user = findById(id);

        if (!request.email().isBlank()) {
            User u = userRepository.findByEmail(request.email()).orElse(null);
            ensure(u != null && !u.equals(user), "Ten email jest już używany");
        }

        ensure(
                user.getFirstName().equals(request.firstName())
                        && user.getLastName().equals(request.lastName())
                        && user.getEmail().equals(request.email())
                        && user.getCarRegistration().equals(request.carRegistration())
                        && user.getPhoneNumber().equals(request.phoneNumber()),
                "Nie podano żadnych zmian"
        );

        user.setUpdatedAt(new Date());
        Optional.ofNullable(request.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.email()).ifPresent(user::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.carRegistration()).ifPresent(user::setCarRegistration);
        Optional.ofNullable(request.country()).ifPresent(user::setCountry);
        Optional.ofNullable(request.city()).ifPresent(user::setCity);
        Optional.ofNullable(request.streetAddress()).ifPresent(user::setStreetAddress);


        update(user.getId(), user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<UserDTO> findAllDTO(Pageable pageable) {
        return findAll(pageable).map(Mapper::toUserDTO);
    }

    public List<UserDTO> getFilteredUsers(String value) {

        if (value == null) {
            return findAllUsersDto();
        }

        List<UserDTO> allUsersDto = findAllUsersDto();
        List<UserDTO> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allUsersDto.forEach(userDto -> {
            if (
                ((userDto.getFirstName() != null) && userDto.getFirstName().toLowerCase().contains(filterValue)) ||
                ((userDto.getLastName() != null) && userDto.getLastName().toLowerCase().contains(filterValue)) ||
                ((userDto.getEmail() != null) && userDto.getEmail().toLowerCase().contains(filterValue)) ||
                ((userDto.getPhoneNumber() != null) && userDto.getPhoneNumber().toLowerCase().contains(filterValue) ||
                ((userDto.getCarRegistration() != null) && (userDto.getCarRegistration()).toLowerCase().contains(filterValue)) ||
                ((userDto.getCountry() != null) && (userDto.getCountry()).toLowerCase().contains(filterValue)) ||
                ((userDto.getCity() != null) && (userDto.getCity()).toLowerCase().contains(filterValue)) ||
                ((userDto.getStreetAddress() != null) && (userDto.getStreetAddress()).toLowerCase().contains(filterValue))
                        )) {
                filteredList.add(userDto);
            }

        });
        return filteredList;
    }



    public UserDTO findUserDtoById(int id) {
        return userRepository.findById(id).map(Mapper::toUserDTO).orElseThrow();
    }

//    public void createUserIfDontExist(User user) {
//
//        User u = user.getId() <= 0 || findUserById(user.getId()) == null ? create(user) : user;
//        return u;
//    }

    @Transactional
    public void deleteUser(int id) {
        User userToDelete = findById(id);
        userRepository.delete(userToDelete);
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(int id, User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {

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
        ensure(!user.email().isBlank() && userRepository.findByEmail(user.email()).isPresent(), "Ten email jest już używany");
    }
}

