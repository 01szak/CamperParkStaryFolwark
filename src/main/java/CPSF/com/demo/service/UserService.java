package CPSF.com.demo.service;

import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.UserDto;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email).orElseThrow();

    }

    public User findUserByEmailForAuthenticationPurpose(String email) {

        return userRepository.findByEmail(email).orElseThrow();
    }


    public void save(User user) {
        userRepository.save(user);
    }


}
