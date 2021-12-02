package maxcosta.reto2.enumeration;

import static maxcosta.reto2.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
