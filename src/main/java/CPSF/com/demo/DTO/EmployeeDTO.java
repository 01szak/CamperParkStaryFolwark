package CPSF.com.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmployeeDTO extends DTO {
    private String username;
    private String email;
    private String role;
}
