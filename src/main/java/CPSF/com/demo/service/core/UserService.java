package CPSF.com.demo.service.core;

import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserService extends CRUDServiceImpl<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user =  userRepository.getUsersByLogin(login);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Użytkownik z podanymi danymi nie istnieje");
        }
        return user.get(0);
    }

}
