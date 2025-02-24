package vn.hoidanit.jobhunter.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.hoidanit.jobhunter.service.UserService;

@Component("userDetailsService")
public class UserDetailCustoms implements UserDetailsService {
    private final UserService userService;

    public UserDetailCustoms(UserService userService) {
        this.userService = userService;
    }

    // principal
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.hoidanit.jobhunter.domain.Entity.User user = this.userService.fecthUserByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException("Username/password not found");
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
