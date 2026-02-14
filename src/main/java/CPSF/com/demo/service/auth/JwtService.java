package CPSF.com.demo.service.auth;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateToken(Authentication authentication);

}
