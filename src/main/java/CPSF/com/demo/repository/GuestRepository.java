package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GuestRepository extends CRUDRepository<Guest> {

    Optional<Guest> findByEmail(String email);

    @Query("""
       select u from Guest u where concat(u.firstname, ' ', u.lastname) like lower(concat('%', :fullName, '%'))
       """)
    Page<Guest> findAllByFullName(Pageable pageable, @Param("fullName") String fullName);
}
