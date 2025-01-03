package CPSF.com.demo.service;

import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.UserDto;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserService(UserRepository userRepository,Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        return mapper.toDto(user);
    }    public User findUserByEmailForAuthenticationPurpose(String email) {

        return userRepository.findByEmail(email).orElseThrow();
    }


    public void save(User user) {
        userRepository.save(user);
    }
}
