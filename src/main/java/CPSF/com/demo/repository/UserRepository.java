package CPSF.com.demo.repository;

import CPSF.com.demo.model.constant.UserRole;
import CPSF.com.demo.model.entity.User;

import java.util.Optional;

public interface UserRepository extends CRUDRepository<User> {

    Optional<User> findByLogin(String login);

    Optional<User> findFirstByOrganisation_IdAndUserRole(Integer organisationId, UserRole userRole);

}
