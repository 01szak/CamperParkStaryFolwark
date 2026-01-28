package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.repository.
        UserRepository;
import CPSF.com.demo.service.UserService;
import CPSF.com.demo.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> findAll() {
        return null;
    }

    @Override
    public Page<UserDTO> findAllDTO(Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserDTO> findAllDTO() {
        return null;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void create(User user) {

    }

    @Override
    public Page<User> findBy(Pageable pageable, String fieldName, String value) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return null;
    }

    @Override
    public Page<UserDTO> findDTOBy(Pageable pageable, String fieldName, String value) throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.getUsersByLogin(login).get(0);
    }

    @Override
    public UserDTO getUserDTO(String username) {
        return (UserDTO) Mapper.toEmployeeDTO((User) this.loadUserByUsername(username));
    }
}
