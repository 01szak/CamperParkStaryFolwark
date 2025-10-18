package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends CRUDRepository<Employee> {

    List<Employee> getEmployeesByLogin(String login);

}
