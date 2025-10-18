package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.EmployeeDTO;
import CPSF.com.demo.service.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public EmployeeDTO getEmployee() {
        Authentication a =  SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) a.getPrincipal();
        String username = jwt.getClaimAsString("iss");
        return employeeService.getEmployeeDto(username);
    }
}
