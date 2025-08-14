//package CPSF.com.demo.aspect;
//
//import CPSF.com.demo.repository.CamperPlaceRepository;
//import CPSF.com.demo.repository.ReservationRepository;
//import CPSF.com.demo.repository.UserRepository;
//import CPSF.com.demo.service.CamperPlaceService;
//import CPSF.com.demo.service.ReservationService;
//import CPSF.com.demo.service.UserService;
//import lombok.AllArgsConstructor;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.ZoneId;
//import java.util.Date;
//
//@Aspect
//@Component
//@AllArgsConstructor
//public class Checker {
//    ReservationRepository reservationRepository;
//    CamperPlaceRepository camperPlaceRepository;
//    UserRepository userRepository;
//
//    @Transactional
//    @Before("execution(* CPSF.com.demo.controller..*(..))")
//    public void runDataChecker() {
//        this.camperPlaceRepository.findAll().forEach(c -> {
//            if (c.getCreatedAt() == null) {
//                c.setCreatedAt(new Date());
//                camperPlaceRepository.saveAndFlush(c);
//            }
//        });
//        this.reservationRepository.findAll().forEach(r -> {
//            if (r.getCreatedAt() == null) {
//                r.setCreatedAt(Date.from(r.getCheckin().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                reservationRepository.saveAndFlush(r);
//            }
//        });
//        this.userRepository.findAll().forEach(u -> {
//            if (u.getCreatedAt() == null) {
//                u.setCreatedAt(new Date());
//                userRepository.saveAndFlush(u);
//            }
//        });
//    }
//}
