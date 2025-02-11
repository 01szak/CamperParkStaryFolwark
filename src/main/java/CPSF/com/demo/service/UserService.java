package CPSF.com.demo.service;

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

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toUserDto)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email).orElseThrow();

    }

    public User findUserByEmailForAuthenticationPurpose(String email) {

        return userRepository.findByEmail(email).orElseThrow();
    }


    public void create(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.GUEST);
        }
        if (user.getReservations() == null) {
            user.setReservations(new ArrayList<>());
        }
        userRepository.save(user);
    }


}
