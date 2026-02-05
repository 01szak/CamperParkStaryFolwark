package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.util.DtoMapper;
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
                                  @RequestParam(required = false) String value) {
        if (by != null && value != null) {
            return guestService.findBy(pageable, by, value).map(DtoMapper::getGuestDTO);
        }
//        TODO Do not return all data!!
        return guestService.findAll(pageable).map(DtoMapper::getGuestDTO);
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid GuestDTO guestDTO){
        guestService.update(DtoMapper.getGuest(guestDTO));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Gość został zmieniony"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid GuestDTO guestDTO) {
        guestService.create(DtoMapper.getGuest(guestDTO));
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





