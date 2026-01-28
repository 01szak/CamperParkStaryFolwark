package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GuestRepository extends CRUDRepository<Guest> {

    Optional<Guest> findByEmail(String email);

    @Query("""
       SELECT u 
       FROM Guest u 
       WHERE CONCAT(u.firstName, ' ', u.lastName) 
             LIKE %:fullName%
       """)
    Page<Guest> findAllByFullName(Pageable pageable, String fullName);
}
