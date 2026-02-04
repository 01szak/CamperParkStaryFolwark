package CPSF.com.demo.service;

import CPSF.com.demo.DTO.GuestDTO;
import CPSF.com.demo.util.DtoMapper;
import CPSF.com.demo.entity.Guest;
import CPSF.com.demo.repository.GuestRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static CPSF.com.demo.exception.ClientInputExceptionUtil.ensure;

@Service
@NoArgsConstructor
public class GuestService extends CRUDServiceImpl<Guest> {

    @Autowired
    private GuestRepository guestRepository;

    @Transactional
    public void create(GuestDTO user) {
        checkIsDataCorrect(user);

        super.create(Guest.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .carRegistration(user.carRegistration())
                .phoneNumber(user.phoneNumber())
                .createdAt(new Date())
                .build()
        );
    }

    public Page<CPSF.com.demo.DTO.GuestDTO> findDTOBy(Pageable pageable, String by, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
            if (by.equals("fullName")) {
                return guestRepository.findAllByFullName(pageable, value).map(DtoMapper::getGuestDTO);
            }
            return findBy(pageable, by, value).map(DtoMapper::getGuestDTO);
    }

    @Transactional
    public void update(int id, GuestDTO request) {

        Guest guest = findById(id);

        if (!request.email().isBlank()) {
            Guest u = guestRepository.findByEmail(request.email()).orElse(null);
            ensure(u != null && !u.equals(guest), "Ten email jest już używany");
        }

        ensure(
                guest.getFirstName().equals(request.firstName())
                        && guest.getLastName().equals(request.lastName())
                        && guest.getEmail().equals(request.email())
                        && guest.getCarRegistration().equals(request.carRegistration())
                        && guest.getPhoneNumber().equals(request.phoneNumber()),
                "Nie podano żadnych zmian"
        );

        Optional.ofNullable(request.firstName()).ifPresent(guest::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(guest::setLastName);
        Optional.ofNullable(request.email()).ifPresent(guest::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(guest::setPhoneNumber);
        Optional.ofNullable(request.carRegistration()).ifPresent(guest::setCarRegistration);

        super.update(guest);
    }

    private void checkIsDataCorrect(GuestDTO user) {
        ensure(
                user.email().isBlank()
                        && user.firstName().isBlank()
                        && user.lastName().isBlank()
                        && user.carRegistration().isBlank()
                        && user.phoneNumber().isBlank(),
                "Formularz jest pusty!"
        );
        ensure(!user.email().isBlank() && guestRepository.findByEmail(user.email()).isPresent(),
                "Ten email jest już używany");
    }
}

