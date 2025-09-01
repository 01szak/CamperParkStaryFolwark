package CPSF.com.demo.service;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, CRUDService<User, UserDTO> {

    void create(UserRequest user);


    void update(int id, UserRequest request);

    void update(User user);

    Page<User> findAll(Pageable pageable);

    Page<User> findAll();

    void delete(User user);

    void delete(int id);

    UserDTO findDTOById(int id);
}
