package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.GuestDTO;
import CPSF.com.demo.entity.Guest;
import CPSF.com.demo.service.GuestService;
import CPSF.com.demo.util.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    public GuestService guestService;

    @GetMapping
    public Page<GuestDTO> findAll(Pageable pageable,
                                  @RequestParam(required = false) String by,
                                  @RequestParam(required = false) String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (by != null && value != null) {
            return guestService.findBy(pageable, by, value).map(DtoMapper::getGuestDTO);
        }

        return guestService.findAll(pageable).map(DtoMapper::getGuestDTO);
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid GuestDTO guestDTO){
        var g = guestService.findById(guestDTO.id());
        g.setFirstName(guestDTO.firstName());
        g.setLastName(guestDTO.lastName());
        g.setEmail(guestDTO.email());
        g.setPhoneNumber(guestDTO.phoneNumber());
        g.setCarRegistration(guestDTO.carRegistration());

        guestService.update(g);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Gość został zmieniony"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid GuestDTO guestDTO) {
        var g = Guest.builder()
                .firstName(guestDTO.firstName())
                .lastName(guestDTO.lastName())
                .email(guestDTO.email())
                .carRegistration(guestDTO.carRegistration())
                .phoneNumber(guestDTO.phoneNumber())
                .build();

        guestService.create(g);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został utworzony"));
    }

    @GetMapping("/{id}")
    public GuestDTO getGuestById(@PathVariable int id){
        return DtoMapper.getGuestDTO(guestService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGuest(@PathVariable int id){
        guestService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Gość został usunięty"));
    }
}





