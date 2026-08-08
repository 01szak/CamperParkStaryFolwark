package CPSF.com.demo.model.constant;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

        SUPER_ADMIN("SUPER_ADMIN"),
        ADMIN("ADMIN"),
        REGULAR("REGULAR"),
        OWNER("OWNER");

        private final String value;

        UserRole(String value) {
            this.value = value;
        }

        @Override
        public String getAuthority() {
            return value;
        }
    }