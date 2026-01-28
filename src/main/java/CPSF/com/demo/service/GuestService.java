package CPSF.com.demo.service;

import CPSF.com.demo.DTO.GuestDTO;
import CPSF.com.demo.entity.Guest;
import CPSF.com.demo.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuestService extends CRUDService<Guest, GuestDTO> {

    void create(UserRequest user);


    void update(int id, UserRequest request);

    void update(Guest guest);

    Page<Guest> findAll(Pageable pageable);

    Page<Guest> findAll();

    void delete(Guest guest);

    void delete(int id);

    GuestDTO findDTOById(int id);
}
