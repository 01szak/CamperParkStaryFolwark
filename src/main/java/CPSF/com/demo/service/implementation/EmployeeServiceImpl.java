package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.EmployeeDTO;
import CPSF.com.demo.entity.Employee;
import CPSF.com.demo.repository.EmployeeRepository;
import CPSF.com.demo.service.EmployeeService;
import CPSF.com.demo.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Employee> findAll() {
        return null;
    }

    @Override
    public Page<EmployeeDTO> findAllDTO(Pageable pageable) {
        return null;
    }

    @Override
    public Page<EmployeeDTO> findAllDTO() {
        return null;
    }

    @Override
    public Employee findById(int id) {
        return null;
    }

    @Override
    public void update(Employee employee) {

    }

    @Override
    public void delete(Employee employee) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void create(Employee employee) {

    }

    @Override
    public Page<Employee> findBy(Pageable pageable, String fieldName, String value) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return null;
    }

    @Override
    public Page<EmployeeDTO> findDTOBy(Pageable pageable, String fieldName, String value) throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.getEmployeesByLogin(login).get(0);
    }

    @Override
    public EmployeeDTO getEmployeeDto(String username) {
        return (EmployeeDTO) Mapper.toEmployeeDTO((Employee) this.loadUserByUsername(username));
    }
}
