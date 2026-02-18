package CPSF.com.demo.service.core;

import CPSF.com.demo.exception.ClientInputException;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class GuestService extends CRUDServiceImpl<Guest> {

    private static final String BY_FULL_NAME = "fullName";

    @Autowired
    private GuestRepository guestRepository;

    public Guest create(GuestDTO guestDTO) {
        checkIsEmpty(guestDTO);

        return super.create(Guest.builder()
                .firstname(guestDTO.firstname())
                .lastname(guestDTO.lastname())
                .email(mapBlankToNull(guestDTO.email()))
                .carRegistration(guestDTO.carRegistration())
                .phoneNumber(guestDTO.phoneNumber())
                .build()
        );
    }

    @Override
    public Page<Guest> findBy(Pageable pageable, String by, String value) {
            if (BY_FULL_NAME.equals(by)) {
                return guestRepository.findAllByFullName(pageable, value);
            }
            return super.findBy(pageable, by, value);
    }

    public Guest update(GuestDTO guestDTO) {
        if (guestDTO.id() == null) {
            return create(guestDTO);
        }

        var guest = findById(guestDTO.id());

        guest.setFirstname(guestDTO.firstname());
        guest.setLastname(guestDTO.lastname());
        guest.setEmail(mapBlankToNull(guestDTO.email()));
        guest.setPhoneNumber(guestDTO.phoneNumber());
        guest.setCarRegistration(guestDTO.carRegistration());

        return super.update(guest);
    }

    private String mapBlankToNull(String email) {
        return email == null || email.isBlank() ? null : email;
    }

    private void checkIsEmpty(GuestDTO guestDTO) {
        var guestIsEmpty = Stream.of(
                guestDTO.firstname(),
                guestDTO.lastname(),
                guestDTO.phoneNumber(),
                guestDTO.email(),
                guestDTO.carRegistration()
        ).allMatch(v -> v == null || v.isBlank());

        if (guestIsEmpty) {
            throw new ClientInputException("Utwórz lub podaj istniejącego gościa");
        }
    }
}

