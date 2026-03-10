package CPSF.com.demo.service.core;

import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService extends CRUDServiceImpl<User> implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(login);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Użytkownik z podanymi danymi nie istnieje");
        }

        return user.get();
    }

    @Override
    protected CRUDRepository<User> getRepository() {
        return userRepository;
    }
}
