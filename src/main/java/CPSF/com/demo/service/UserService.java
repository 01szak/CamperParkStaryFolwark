package CPSF.com.demo.service;

import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.UserDto;
import CPSF.com.demo.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CamperPlaceService camperPlaceService;
    private final ReservationService reservationService;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, CamperPlaceService camperPlaceService, ReservationService reservationService, Mapper mapper) {
        this.userRepository = userRepository;
        this.camperPlaceService = camperPlaceService;
        this.reservationService = reservationService;
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
    }




}
