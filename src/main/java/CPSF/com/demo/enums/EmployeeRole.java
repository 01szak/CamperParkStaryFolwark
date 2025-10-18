package CPSF.com.demo.enums;

import org.springframework.security.core.GrantedAuthority;

public enum EmployeeRole implements GrantedAuthority {

    SUPER_ADMIN("SUPER_ADMIN"),

    ADMIN("ADMIN"),

    REGULAR("REGULAR");

    private final String value;

    EmployeeRole(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
