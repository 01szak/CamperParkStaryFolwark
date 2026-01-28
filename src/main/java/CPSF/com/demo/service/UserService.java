package CPSF.com.demo.service;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService, CRUDService<User, UserDTO> {

    UserDTO getUserDTO(String username);
}
