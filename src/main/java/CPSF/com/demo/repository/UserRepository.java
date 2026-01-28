package CPSF.com.demo.repository;

import CPSF.com.demo.entity.User;

import java.util.List;

public interface UserRepository extends CRUDRepository<User> {

    List<User> getUsersByLogin(String login);

}
