package CPSF.com.demo.service;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    void create(UserRequest user);

    void create(User user);

    void update(int id, UserRequest request);

    Page<User> findAll(Pageable pageable);

    Page<User> findAll();

    void delete(User user);

    void delete(int id);

    User findById(int id);

    UserDTO findDTOById(int id);
}
