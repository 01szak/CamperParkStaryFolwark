//package CPSF.com.demo.configuration;
//
//import CPSF.com.demo.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@RequiredArgsConstructor
//public class AppConfiguration {
//
//    @Autowired
//    private final UserService userService;
//
//
//
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
////        return configuration.getAuthenticationManager();
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
