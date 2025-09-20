package CPSF.com.demo.repository;

import CPSF.com.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CRUDRepository<User> {

    Optional<User> findByEmail(String email);

    @Query("""
       SELECT u 
       FROM User u 
       WHERE CONCAT(u.firstName, ' ', u.lastName) 
             LIKE %:fullName%
       """)
    Page<User> findAllByFullName(Pageable pageable, String fullName);
}
