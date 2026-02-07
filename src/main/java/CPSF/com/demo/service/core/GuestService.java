package CPSF.com.demo.service.core;

import CPSF.com.demo.exception.ClientInputException;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.repository.GuestRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Stream;

import static CPSF.com.demo.exception.ClientInputException.checkClientInput;

@Service
@NoArgsConstructor
public class GuestService extends CRUDServiceImpl<Guest> {

    @Autowired
    private GuestRepository guestRepository;

    public Guest create(GuestDTO guestDTO) {
        checkIsEmpty(guestDTO);

        return super.create(Guest.builder()
                .firstname(guestDTO.firstName())
                .lastname(guestDTO.lastName())
                .email(mapBlankToNull(guestDTO.email()))
                .carRegistration(guestDTO.carRegistration())
                .phoneNumber(guestDTO.phoneNumber())
                .build()
        );
    }

    @Override
    public Page<Guest> findBy(Pageable pageable, String by, String value) {
            if ("fullName".equals(by)) {
                return guestRepository.findAllByFullName(pageable, value);
            }
            return super.findBy(pageable, by, value);
    }

    public Guest update(GuestDTO guestDTO) {
        if (guestDTO.id() == null) {
            return create(guestDTO);
        }

        Guest guest = findById(guestDTO.id());

        guest.setFirstname(guestDTO.firstName());
        guest.setLastname(guestDTO.lastName());
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
                guestDTO.firstName(),
                guestDTO.lastName(),
                guestDTO.phoneNumber(),
                guestDTO.email(),
                guestDTO.carRegistration()
        ).allMatch(v -> v == null || v.isBlank());
        if (guestIsEmpty) {
            throw new ClientInputException("Utwórz lub podaj istniejącego gościa");
        }
    }
}

