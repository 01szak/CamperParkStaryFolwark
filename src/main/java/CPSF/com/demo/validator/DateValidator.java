package CPSF.com.demo.validator;

import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceRepository;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class DateValidator implements StatusValidator{

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CamperPlaceRepository camperPlaceRepository;

    public DateValidator(ReservationRepository reservationRepository, UserRepository userRepository, CamperPlaceRepository camperPlaceRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.camperPlaceRepository = camperPlaceRepository;
    }

    @Override
    @Scheduled(fixedRate = 100000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void validateStatus() {
        updateCreationOrUpdate(reservationRepository.findAll(), reservationRepository);
        updateCreationOrUpdate(camperPlaceRepository.findAll(), camperPlaceRepository);
        updateCreationOrUpdate(userRepository.findAll(), userRepository);
    }

    private <T extends DbObject> void updateCreationOrUpdate(List<T> objects, CRUDRepository<T> repository) {
        for (T o : objects) {
            if (o.getCreatedAt() == null) {
                o.setCreatedAt(new Date());
                o.setUpdatedAt(new Date());
                repository.save(o);
            }
        }
    }
}
