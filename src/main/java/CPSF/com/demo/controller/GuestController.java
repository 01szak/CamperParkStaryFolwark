package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.GuestDTO;
import CPSF.com.demo.service.GuestService;
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

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService theUserService) {
        guestService = theUserService;
    }


    @GetMapping
    public Page<GuestDTO> findAll(Pageable pageable,
                                  @RequestParam(required = false) String by,
                                  @RequestParam(required = false) String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (by != null && value != null) {
            return guestService.findDTOBy(pageable, by, value);
        }
        return guestService.findAllDTO(pageable);
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody GuestDTO guestDTO){
        guestService.update(guestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został zmieniony"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody GuestDTO guestDTO) {
        guestService.create(guestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został utworzony"));
    }

    @GetMapping("/{id}")
    public GuestDTO getUserById(@PathVariable int id){
        return guestService.findDTOById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id){
         guestService.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został usunięty"));
    }
}





