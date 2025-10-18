package CPSF.com.demo.service;

import CPSF.com.demo.DTO.EmployeeDTO;
import CPSF.com.demo.entity.Employee;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface EmployeeService extends UserDetailsService, CRUDService<Employee, EmployeeDTO> {

    EmployeeDTO getEmployeeDto(String username);
}
