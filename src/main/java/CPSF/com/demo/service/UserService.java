package CPSF.com.demo.service;

import CPSF.com.demo.ClientInputException;
import CPSF.com.demo.entity.DTO.UserRequest;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.DTO.UserDto;
import CPSF.com.demo.enums.Role;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDto> findAllUsersDto() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toUserDto)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email).orElseThrow();

    }


    public void create(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.GUEST);
        }
        if (user.getReservations() == null) {
            user.setReservations(new ArrayList<>());
        }
        if(!user.getEmail().isBlank() && userRepository.findByEmail(user.getEmail()).isPresent()) {
           throw new ClientInputException("Guest with that email already exists");
        } else {
            userRepository.save(user);
        }
    }


    public List<UserDto> getFilteredUsers(String value) {

        if (value == null) {
            return findAllUsersDto();
        }

        List<UserDto> allUsersDto = findAllUsersDto();
        List<UserDto> filteredList = new ArrayList<>();
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
    @Transactional
    public void updateUser(int id, UserRequest request) {
        User user = findUserById(id);

        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new ClientInputException("Guest with that email already exists");
        }

        Optional.ofNullable(request.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.email()).ifPresent(user::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.carRegistration()).ifPresent(user::setCarRegistration);
        Optional.ofNullable(request.country()).ifPresent(user::setCountry);
        Optional.ofNullable(request.city()).ifPresent(user::setCity);
        Optional.ofNullable(request.streetAddress()).ifPresent(user::setStreetAddress);
        userRepository.save(user);
    }

    public UserDto findUserDtoById(int id) {
        return userRepository.findById(id).map(mapper::toUserDto).orElseThrow();
    }

    private User findUserById(int id) {
        return userRepository.findById(id).orElseThrow();
    }
    @Transactional
    public User createUserIfDontExist(User user) {
        if (userRepository.findById(user.getId()).isEmpty()) {
            create(user);
        }
        return user;
    }
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}

